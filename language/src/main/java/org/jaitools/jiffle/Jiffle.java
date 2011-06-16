/* 
 *  Copyright (c) 2009-2011, Michael Bedward. All rights reserved. 
 *   
 *  Redistribution and use in source and binary forms, with or without modification, 
 *  are permitted provided that the following conditions are met: 
 *   
 *  - Redistributions of source code must retain the above copyright notice, this  
 *    list of conditions and the following disclaimer. 
 *   
 *  - Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.   
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */   

package org.jaitools.jiffle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import org.codehaus.janino.SimpleCompiler;

import org.jaitools.CollectionFactory;
import org.jaitools.jiffle.parser.CheckAssignments;
import org.jaitools.jiffle.parser.CheckFunctionCalls;
import org.jaitools.jiffle.parser.JiffleLexer;
import org.jaitools.jiffle.parser.JiffleParser;
import org.jaitools.jiffle.parser.JiffleParserException;
import org.jaitools.jiffle.parser.Message;
import org.jaitools.jiffle.parser.MessageTable;
import org.jaitools.jiffle.parser.OptionsBlockReader;
import org.jaitools.jiffle.parser.ParsingErrorReporter;
import org.jaitools.jiffle.parser.RuntimeSourceGenerator;
import org.jaitools.jiffle.parser.SourceGenerator;
import org.jaitools.jiffle.parser.TagVars;
import org.jaitools.jiffle.parser.TransformExpressions;
import org.jaitools.jiffle.runtime.JiffleDirectRuntime;
import org.jaitools.jiffle.runtime.JiffleIndirectRuntime;
import org.jaitools.jiffle.runtime.JiffleRuntime;

/**
 * Compiles scripts and generates Java sources and executable bytecode for
 * runtime classes.
 * <p>
 * Example of use:
 * <pre><code>
 * // A script to write sequential values to image pixels
 * String script = "images { dest=write; } dest = x() + y() * width();" ;
 *
 * Jiffle jiffle = new Jiffle();
 * jiffle.setScript(script);
 * jifle.compile();
 *
 * // Now we get the runtime object from the compiled Jiffle object
 * JiffleDirectRuntime runtime = jiffle.getRuntimeInstance();
 *
 * // Create an image to hold the results of the script
 * final int width = 10;
 * WritableRenderedImage destImg = ImageUtils.createConstantImage(width, width, 0.0d);
 * 
 * // Associate this image with the variable name used in the script
 * runtime.setDestinationImage("dest", destImg);
 *
 * // Evaluate the script for all destination image pixels
 * runtime.evaluateAll();
 * </code></pre>
 * For further examples of how to create and run Jiffle scripts see the
 * {@code org.jaitools.demo.jiffle} package in the JAI-tools demo module.
 *
 * <h4>Implementation note</h4>
 * The Jiffle compiler is actually a Jiffle to Java translator.
 * When a client requests a runtime object, the script is translated into 
 * Java source for a runtime class. This source code is then passed to an 
 * embedded Janino compiler which produces the runtime object.
 *
 * @see JiffleBuilder
 * @see org.jaitools.jiffle.runtime.JiffleExecutor
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class Jiffle {
    
    public static final Logger LOGGER = Logger.getLogger(Jiffle.class.getName());

    /** 
     * Constants for runtime model. Jiffle supports two runtime models:
     * <ol type="1">
     * <li>
     * <b>Direct</b> - where the runtime class {@code evaluate} method directly 
     * sets values in the destination image(s)
     * </li>
     * <li>
     * <b>Indirect</b> - where there is a single destination image and the runtime
     * class {@code evaluate} method returns the destination value, leaving
     * it up to the caller to set the value in the image.
     * object 
     * </li>
     * </ol>
     * The indirect model is designed for use in an image operator or similar
     * context where further processing of destination values might be required
     * before writing to an image.
     */
    public static enum RuntimeModel {
        /** The runtime class implements {@link JiffleDirectRuntime} */
        DIRECT(JiffleDirectRuntime.class),
        
        /** The runtime class implements {@link JiffleIndirectRuntime} */
        INDIRECT(JiffleIndirectRuntime.class);
        
        private Class<? extends JiffleRuntime> runtimeClass;
        
        private RuntimeModel(Class<? extends JiffleRuntime> clazz) {
            this.runtimeClass = clazz;
        }

        /**
         * Gets the runtime interface.
         *
         * @return the runtime interface
         */
        public Class<? extends JiffleRuntime> getRuntimeClass() {
            return runtimeClass;
        }
        
        /**
         * Gets the matching constant for the given runtime class.
         *
         * @param clazz a runtime class
         *
         * @return the contant or {@code null} if the class does not derive
         *         from a supported base class
         */
        public static RuntimeModel get(Class<? extends JiffleRuntime> clazz) {
            for (RuntimeModel t : RuntimeModel.values()) {
                if (t.runtimeClass.isAssignableFrom(clazz)) {
                    return t;
                }
            }
            
            return null;
        }
    }

    
    /** Number of Jiffle instances */
    private static int refCount = 0;
    

    /**
     * Used to specify the roles of images referenced in
     * a Jiffle script. An image may be either read-only
     * ({@link Jiffle.ImageRole#SOURCE}) or write-only
     * ({@link Jiffle.ImageRole#DEST}) but not both.
     */
    public static enum ImageRole {
        /** Indicates an image is used for input (read-only) */
        SOURCE,
        
        /** Indicates an image is used for output (write-only) */
        DEST;
    }

    /** A name: either a default or one set by the client */
    private String name;

    private String theScript;
    private CommonTree primaryAST;
    private Map<String, String> scriptOptions;
    private CommonTree finalAST;
    private CommonTokenStream tokens;
    private ParsingErrorReporter errorReporter;
    
    private Map<String, ImageRole> imageParams;
    private MessageTable msgTable;
    
    /**
     * Creates a new instance.
     */
    public Jiffle() {
        init();
    }
    
    /**
     * Creates a new instance by compiling the provided script. Using this
     * constructor is equivalent to:
     * <pre><code>
     * Jiffle jiffle = new Jiffle();
     * jiffle.setScript(script);
     * jiffle.setImageParams(params);
     * jiffle.compile();
     * </code></pre>
     * 
     * @param script Jiffle source code to compile
     * 
     * @param params defines the names and roles of image variables
     *        referred to in the script.
     * @throws JiffleException on compilation errors
     * 
     */
    public Jiffle(String script, Map<String, ImageRole> params)
            throws JiffleException {

        init();
        setScript(script);
        setImageParams(params);
        compile();
    }

    /**
     * Creates a new instance by compiling the script read from {@code scriptFile}. 
     * Using this constructor is equivalent to:
     * <pre><code>
     * Jiffle jiffle = new Jiffle();
     * jiffle.setScript(scriptFile);
     * jiffle.setImageParams(params);
     * jiffle.compile();
     * </code></pre>
     * 
     * @param scriptFile file containing the Jiffle script
     * 
     * @param params defines the names and roles of image variables
     *        referred to in the script.
     * 
     * @throws JiffleException on compilation errors
     */
    public Jiffle(File scriptFile, Map<String, ImageRole> params)
            throws JiffleException {

        init();
        setScript(scriptFile);
        setImageParams(params);
        compile();
    }
    
    /**
     * Sets the script. Calling this method will clear any previous script
     * and runtime objects.
     * 
     * @param script a Jiffle script
     * @throws JiffleException if the script is empty or {@code null}
     */
    public final void setScript(String script) throws JiffleException {
        if (script == null || script.trim().length() == 0) {
            throw new JiffleException("script is empty !");
        }
        
        if (theScript != null) {
            clearCompiledObjects();
        }
        
        // add extra new line just in case last statement hits EOF
        theScript = script + "\n";
    }

    /**
     * Sets the script. Calling this method will clear any previous script
     * and runtime objects.
     * 
     * @param scriptFile a file containing a Jiffle script
     * @throws JiffleException if the script is empty or {@code null}
     */
    public final void setScript(File scriptFile) throws JiffleException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(scriptFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    sb.append(line);
                    sb.append('\n');  // put the newline back on for the parser
                }
            }
            
            setScript(sb.toString());
            
        } catch (IOException ex) {
            throw new JiffleException("Could not read the script file", ex);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
    
    /**
     * Gets the Jiffle script.
     * 
     * @return the script or an empty {@code String} if none
     *         has been set
     */
    public String getScript() {
        return theScript == null ? "" : theScript;
    }
    
    /**
     * Sets the image parameters. These define which variables in
     * the script refer to images and their types (source or destination).
     * <p>
     * This may be called before or after setting the script. No check is
     * made between script and parameters until the script is compiled.
     * 
     * @param params the image parameters
     */
    public final void setImageParams(Map<String, ImageRole> params) {
        imageParams.clear();
        imageParams.putAll(params);
    }
    
    /**
     * Gets the current image parameters. The parameters are returned
     * as an unmodifiable map.
     * 
     * @return image parameters or an empty {@code Map} if none
     *         are set
     */
    public Map<String, ImageRole> getImageParams() {
        return Collections.unmodifiableMap(imageParams);
    }

    /**
     * Replaces the default name set for this object with a user-supplied name.
     * The name is solely for use by client code. No checks are made for 
     * duplication between current instances.
     * 
     * @param name the name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name assigned to this object. This will either be the
     * default name or one assigned by the client via {@link #setName(String)}
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Compiles the script into Java source for the runtime class.
     * 
     * @throws JiffleException on compilation errors
     */
    public final void compile() throws JiffleException {
        if (theScript == null) {
            throw new JiffleException("No script has been set");
        }
        
        clearCompiledObjects();
        buildPrimaryAST();
        
        if (imageParams.isEmpty()) {
            throw new JiffleException("No image parameters set");
        }
        
        checkOptions();
        reportMessages();
        
        if (!transformAndCheckVars()) {
            throw new JiffleException(messagesToString());
        }
    }
    
    /**
     * Tests whether the script has been compiled successfully.
     *
     * @return {@code true} if the script has been compiled;
     *         {@code false} otherwise
     */
    public boolean isCompiled() {
        return (finalAST != null);
    }
    
    /**
     * Creates an instance of the default runtime class. 
     * <p>
     * The default runtime class implements {@link JiffleDirectRuntime} and
     * extends an abstract base class provided by the Jiffle compiler. Objects
     * of this class evaluate the Jiffle script and write results directly to
     * the destination image(s). Client code can call either of the methods:
     * <ul>
     * <li>{@code evaluate(int x, int y)}
     * <li>{@code evaluateAll(JiffleProgressListener listener}
     * </ul>
     * The {@code Jiffle} object must be compiled before calling this method.
     * 
     * @return the runtime object
     * @throws JiffleException if the script has not been compiled or if errors
     *         occur in creating the runtime instance
     */
    public JiffleDirectRuntime getRuntimeInstance() throws JiffleException {
        return (JiffleDirectRuntime) createRuntimeInstance(
                RuntimeModel.DIRECT, 
                JiffleProperties.DEFAULT_DIRECT_BASE_CLASS);
    }
    
    /**
     * Creates a runtime object based using the class specified by {@code model}.
     * <p>
     * The {@code Jiffle} object must be compiled before calling this method.
     * 
     * @param model the {@link Jiffle.RuntimeModel}
     * @return the runtime object
     * @throws JiffleException  if the script has not been compiled or if errors
     *         occur in creating the runtime instance
     */
    public JiffleRuntime getRuntimeInstance(RuntimeModel model) throws JiffleException {
        switch (model) {
            case DIRECT:
                return createRuntimeInstance(model, 
                        JiffleProperties.DEFAULT_DIRECT_BASE_CLASS);
                
            case INDIRECT:
                return createRuntimeInstance(model, 
                        JiffleProperties.DEFAULT_INDIRECT_BASE_CLASS);
                
            default:
                throw new IllegalArgumentException("Invalid runtime class type: " + model);
        }
    }
    
    /**
     * Gets the runtime object for this script. 
     * <p>
     * The runtime object is an instance of {@link JiffleRuntime}. By default
     * it extends an abstract base class supplied JAI-tools: 
     * {@link org.jaitools.jiffle.runtime.AbstractDirectRuntime}
     * when using the direct runtiem model or 
     * {@link org.jaitools.jiffle.runtime.AbstractIndirectRuntime}
     * when using the indirect model. This method allows you to
     * specify a custom base class. The custom class must implement either 
     * {@link JiffleDirectRuntime} or {@link JiffleIndirectRuntime}.
     * 
     * @param <T> the runtime base class type
     * @param baseClass the runtime base class
     * 
     * @return the runtime object
     * @throws JiffleException  if the script has not been compiled or if errors
     *         occur in creating the runtime instance
     */
    public <T extends JiffleRuntime> T getRuntimeInstance(Class<T> baseClass) throws JiffleException {
        RuntimeModel model = RuntimeModel.get(baseClass);
        if (model == null) {
            throw new JiffleException(baseClass.getName() + 
                    " does not implement a required Jiffle runtime interface");
        }
        
        return (T) createRuntimeInstance(model, baseClass);
    }
    
    /**
     * Gets a copy of the Java source for the runtime class. The 
     * script must have been compiled before calling this method.
     * 
     * @param scriptInDocs whether to include the original Jiffle script
     *        in the class javadocs
     * 
     * @return source for the runtime class
     * 
     * @throws JiffleException  if the script has not been compiled or if errors
     *         occur in creating the runtime source code
     */
    public String getRuntimeSource(boolean scriptInDocs)
            throws JiffleException {
        return getRuntimeSource(RuntimeModel.DIRECT, scriptInDocs);
    }
        
    /**
     * Gets a copy of the Java source for the runtime class. The 
     * script must have been compiled before calling this method.
     * 
     * @param model the {@link Jiffle.RuntimeModel}
     * @param scriptInDocs whether to include the original Jiffle script
     *        in the class javadocs
     * 
     * @return source for the runtime class
     * @throws JiffleException   if the script has not been compiled or if errors
     *         occur in creating the runtime source code
     * 
     */
    public String getRuntimeSource(RuntimeModel model, boolean scriptInDocs)
            throws JiffleException {
        
        Class<? extends JiffleRuntime> baseClass = null;
        switch (model) {
            case DIRECT:
                baseClass = JiffleProperties.DEFAULT_DIRECT_BASE_CLASS;
                break;
                
            case INDIRECT:
                baseClass = JiffleProperties.DEFAULT_INDIRECT_BASE_CLASS;
                break;
        }
        return createRuntimeSource(model, baseClass.getName(), scriptInDocs);
    }
    
    /**
     * Initializes this object's name and runtime base class.
     */
    private void init() {
        Jiffle.refCount++ ;
        name = JiffleProperties.get( JiffleProperties.NAME_KEY ) + refCount;
        imageParams = CollectionFactory.map();
    }
    
    /**
     * Clears all compiler and runtime objects
     */
    private void clearCompiledObjects() {
        primaryAST = null;
        finalAST = null;
        tokens = null;
        errorReporter = null;
        msgTable = new MessageTable();
    }
    
    private void reportMessages() throws JiffleException {
        if (msgTable.hasErrors()) {
            throw new JiffleException(messagesToString());
        }
        
        if (msgTable.hasWarnings()) {
            Map<String, List<Message>> messages = msgTable.getMessages();
            System.err.println(messagesToString());
        }
    }
    
    /**
     * Write error messages to a string
     */
    private String messagesToString() {
        StringBuilder sb = new StringBuilder();
        if (msgTable != null) {
            Map<String, List<Message>> messages = msgTable.getMessages();
            for (String key : messages.keySet()) {
                for (Message msg : messages.get(key)) {
                    sb.append(msg.toString());
                    sb.append(": ");
                    sb.append(key);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * Build a preliminary AST from the jiffle script. Basic syntax and grammar
     * checks are done at this stage.
     * 
     * @throws JiffleException
     */
    private void buildPrimaryAST() throws JiffleException {
        try {
            ANTLRStringStream input = new ANTLRStringStream(theScript);
            JiffleLexer lexer = new JiffleLexer(input);
            tokens = new CommonTokenStream(lexer);

            JiffleParser parser = new JiffleParser(tokens);
            primaryAST = (CommonTree) parser.prog().getTree();
            
            loadScriptImageParameters(parser.getImageParams());

        } catch (RecognitionException ex) {
            throw new JiffleException(
                    "error in script at or around line:" +
                    ex.line + " col:" + ex.charPositionInLine);
        }
    }

    /**
     * Sets the image parameters to those read from the script (if any). If any 
     * previous parameters were set using {@link #setImageParams(java.util.Map)}
     * they are discarded and a warning message is logged.
     * 
     * @param scriptImageParams parameters read from the script (may be empty)
     */
    private void loadScriptImageParameters(Map<String, ImageRole> scriptImageParams) {
        if (!scriptImageParams.isEmpty()) {
            if (!imageParams.isEmpty()) {
                LOGGER.warning("Image parameters read from script override those previously set");
            }
            
            imageParams = scriptImageParams;
        }
    }
    
    private void checkOptions() {
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(primaryAST);
        nodes.setTokenStream(tokens);
        OptionsBlockReader reader = new OptionsBlockReader(nodes, msgTable);
        reader.downup(primaryAST);
    }

    /**
     * Transforms variable tokens to specific types and does some basic
     * error checking.
     *  
     * @return {@code true} if no errors; {@code false} otherwise
     * @throws JiffleException on unintercepted parser errors
     */
    private boolean transformAndCheckVars() throws JiffleException {
        try {
            CommonTree tree = primaryAST;

            CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);
            TagVars tag = new TagVars(nodes, imageParams, msgTable);
            tree = (CommonTree) tag.start().getTree();
            if (msgTable.hasErrors()) return false;

            nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);

            CheckAssignments assignments = new CheckAssignments(nodes, msgTable);
            assignments.start();
            if (msgTable.hasErrors()) return false;

            nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);
            TransformExpressions trexpr = new TransformExpressions(nodes);
            tree = (CommonTree) trexpr.start().getTree();
            
            nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);
            CheckFunctionCalls calls = new CheckFunctionCalls(nodes, msgTable);
            calls.downup(tree);
            if (msgTable.hasErrors()) return false;
            
            finalAST = tree;
            return true;

        } catch (RecognitionException ex) {
            throw new JiffleException(
                    "error in script at or around line:" +
                    ex.line + " col:" + ex.charPositionInLine);
            
        } catch (JiffleParserException ex) {
            throw new JiffleException(ex);
        }
    }

    /**
     * Creates an instance of the runtime class. The Java source for the
     * class is created if not already cached and then compiled using
     * Janino's {@link SimpleCompiler}.
     * 
     * @throws Exception 
     */
    private JiffleRuntime createRuntimeInstance(RuntimeModel model,
            Class<? extends JiffleRuntime> baseClass) throws JiffleException {
        if (!isCompiled()) {
            throw new JiffleException("The script has not been compiled");
        }
        
        String runtimeSource = createRuntimeSource(model, baseClass.getName(), false);

        try {
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(runtimeSource);
            
            StringBuilder sb = new StringBuilder();
            sb.append(JiffleProperties.get(JiffleProperties.RUNTIME_PACKAGE_KEY)).append(".");
            
            switch (model) {
                case DIRECT:
                    sb.append(JiffleProperties.get(JiffleProperties.DIRECT_CLASS_KEY));
                    break;
                    
                case INDIRECT:
                    sb.append(JiffleProperties.get(JiffleProperties.INDIRECT_CLASS_KEY));
                    break;
                    
                default:
                    throw new IllegalArgumentException("Internal compiler error");
            }
            
            Class<?> clazz = compiler.getClassLoader().loadClass(sb.toString());
            JiffleRuntime runtime = (JiffleRuntime) clazz.newInstance();
            runtime.setImageParams(imageParams);
            return runtime;

        } catch (Exception ex) {
            throw new JiffleException("Runtime source error", ex);
        }
    }
    
    /**
     * Creates the Java source code for the runtime class.
     * 
     * @param scriptInDocs whether to include the Jiffle script in the class
     *        javadocs
     * 
     * @throws JiffleException if an error occurs generating the source 
     */
    private String createRuntimeSource(RuntimeModel model,
            String baseClassName, boolean scriptInDocs) throws JiffleException {
        
        if (!isCompiled()) {
            throw new JiffleException("This instance has not been compiled");
        }

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(finalAST);
        nodes.setTokenStream(tokens);
        
        SourceGenerator generator = new RuntimeSourceGenerator(nodes);
        generator.setBaseClassName(baseClassName);
        generator.setRuntimeModel(model);
        String s = scriptInDocs ? null : theScript;
        return generator.getSource(s);
    }
}
