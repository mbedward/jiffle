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

package jaitools.jiffle.runtime;

import java.util.concurrent.Callable;


/**
 * Executes a runtime object in a thread provided by a {@link JiffleExecutor}.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleExecutorTask implements Callable<JiffleExecutorResult> {
    
    private final JiffleExecutor executor;
    private final int id;
    private final JiffleDirectRuntime runtime;
    private final JiffleProgressListener progressListener;
    
    private boolean completed;

    
    /**
     * Creates a new task. The image variable names (keys) in {@code images}
     * must correspond to those known by the runtime object.
     * 
     * @param executor the {@code JiffleExecutor} running this task
     * @param id job ID allocated by the {@link JiffleExecutor}.
     * @param runtime the {@link JiffleDirectRuntime} instance
     * @param progressListener  
     */
    public JiffleExecutorTask(
            JiffleExecutor executor,
            int id, 
            JiffleDirectRuntime runtime, 
            JiffleProgressListener progressListener) {
        
        this.executor = executor;
        this.id = id;
        this.runtime = runtime;
        this.progressListener = progressListener;
        
        completed = false;
    }

    /**
     * Called by the system to execute this task on a thread provided by the
     * {@link JiffleExecutor}.
     * 
     * @return a result object with references to the {@code Jiffle} object,
     *         the images, and the job completion status
     */
    public JiffleExecutorResult call() {
        boolean gotEx = false;
        try {
            runtime.evaluateAll(progressListener);
            
        } catch (Exception ex) {
            gotEx = true;
        }

        completed = !gotEx;
        return new JiffleExecutorResult(id, runtime, completed);
    }

}

