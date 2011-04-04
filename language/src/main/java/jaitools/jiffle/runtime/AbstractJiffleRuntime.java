/*
 * Copyright 2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package jaitools.jiffle.runtime;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jaitools.jiffle.Jiffle;


/**
 * Provides default implementations of {@link JiffleRuntime} methods plus 
 * some common fields. The fields include those involved in handling image-scope
 * variables and script options; an instance of {@link JiffleFunctions}; and an
 * integer stack used in evaluating {@code con} statements.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class AbstractJiffleRuntime implements JiffleRuntime {
    private static final double EPS = 1.0e-8d;

    private enum Dim { XDIM, YDIM };
    
    private Map<String, Jiffle.ImageRole> _imageParams;
    
    /** Processing area bounds in world units. */
    private Rectangle2D _worldBounds;
    
    /** Step distance in X direction in world units. */
    private double _xstep;
    
    /** Step distance in Y direction in world units. */
    private double _ystep;

    /** Flags whether bounds and step distances have been set. */
    private boolean _worldSet;
    
    /** Number of pixels calculated from bounds and step distances. */
    private long _numPixels;
    
    private class TransformInfo {
        CoordinateTransform transform;
        boolean isDefault;
    }

    /** 
     * A default transform to apply to all images set without an explicit
     * transform. 
     */
    private CoordinateTransform _defaultTransform = new IdentityCoordinateTransform();
    
    /** World to image coordinate transforms with image name as key. */
    private Map<String, TransformInfo> _transformLookup;

    /** Holds information about an image-scope variable. */
    public class ImageScopeVar {
        
        public String name;
        public boolean hasDefaultValue;
        public boolean isSet;
        public double value;

        public ImageScopeVar(String name, boolean hasDefaultValue) {
            this.name = name;
            this.hasDefaultValue = hasDefaultValue;
        }
    }

    // Used to size / resize the _vars array as required
    private static final int VAR_ARRAY_CHUNK = 100;
    
    /** Image-scope variables. */
    protected ImageScopeVar[] _vars = new ImageScopeVar[VAR_ARRAY_CHUNK];
    
    /** Whether the image-scope variables have been initialized. */
    protected boolean _imageScopeVarsInitialized;

    /** The number of image-scope variables defined. */
    protected int _numVars;
    
    /** Advertizes the image-scope variable getter syntax to source generators. */
    public static final String VAR_STRING = "_vars[_VAR_].value";
    
    /** Whether the <i>outside</i> option is set. */
    protected boolean _outsideValueSet;
    
    /** 
     * The value to return for out-of-bounds image data requests if the
     * <i>outside</i> option is set.
     */
    protected double _outsideValue;

    /** 
     * A stack of integer values used in the evaluation of if statements.
     */
    protected IntegerStack _stk;
    
    /** 
     * Provides runtime function support.
     */
    protected final JiffleFunctions _FN;

    /**
     * Creates a new instance of this class and initializes its 
     * {@link JiffleFunctions} and {@link IntegerStack} objects.
     */
    public AbstractJiffleRuntime() {
        _FN = new JiffleFunctions();
        _stk = new IntegerStack();
        
        _transformLookup = new HashMap<String, TransformInfo>();
        _xstep = Double.NaN;
        _ystep = Double.NaN;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setImageParams(Map imageParams) {
        this._imageParams = new HashMap<String, Jiffle.ImageRole>();
        for (Object oname : imageParams.keySet()) {
            String name = (String) oname;
            Jiffle.ImageRole role = (Jiffle.ImageRole) imageParams.get(oname);
            this._imageParams.put(name, role);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] getDestinationVarNames() {
        return doGetImageVarNames(Jiffle.ImageRole.DEST);
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] getSourceVarNames() {
        return doGetImageVarNames(Jiffle.ImageRole.SOURCE);
    }

    private String[] doGetImageVarNames(Jiffle.ImageRole role) {
        List<String> names = new ArrayList<String>();
        for (String name : _imageParams.keySet()) {
            if (_imageParams.get(name) == role) {
                names.add(name);
            }
        }

        return names.toArray(new String[0]);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setWorldByStepDistance(Rectangle2D bounds, double xstep, double ystep) {
        if (bounds == null || bounds.isEmpty()) {
            throw new IllegalArgumentException("bounds cannot be null or empty");
        }
        if (xstep < EPS || ystep < EPS) {
            throw new IllegalArgumentException("step distance but must be greater than 0");
        }
        
        doSetWorld(bounds, xstep, ystep);
    }

    /**
     * {@inheritDoc}
     */
    public void setWorldByNumSteps(Rectangle2D bounds, int nx, int ny) {
        if (bounds == null || bounds.isEmpty()) {
            throw new IllegalArgumentException("bounds cannot be null or empty");
        }
        if (nx <= 0 || ny <= 0) {
            throw new IllegalArgumentException("number of steps must be greater than 0");
        }
        
        doSetWorld(bounds, bounds.getWidth() / nx, bounds.getHeight() / ny);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isWorldSet() {
        return _worldSet;
    }

    /**
     * {@inheritDoc}
     */
    public Double getVar(String varName) {
        int index = getVarIndex(varName);
        if (index < 0) {
            return null;
        }
        
        return _vars[index].isSet ? _vars[index].value : null; 
    }

    /**
     * {@inheritDoc}
     */
    public void setVar(String varName, Double value) throws JiffleRuntimeException {
        int index = getVarIndex(varName);
        if (index < 0) {
            throw new JiffleRuntimeException("Undefined variable: " + varName);
        }
        setVarValue(index, value);
    }

    /**
     * {@inheritDoc}
     */
    public double getMinX() {
        return _worldBounds.getMinX();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxX() {
        return _worldBounds.getMaxX();
    }

    /**
     * {@inheritDoc}
     */
    public double getMinY() {
        return _worldBounds.getMinY();
    }

    /**
     * {@inheritDoc}
     */
    public double getMaxY() {
        return _worldBounds.getMaxY();
    }
    
    /**
     * {@inheritDoc}
     */
    public double getWidth() {
        return _worldBounds.getWidth();
    }
    
    /**
     * {@inheritDoc}
     */
    public double getHeight() {
        return _worldBounds.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    public double getXStep() {
        return _xstep;
    }

    /**
     * {@inheritDoc}
     */
    public double getYStep() {
        return _ystep;
    }
    
    public long getNumPixels() {
        if (!_worldSet) {
            throw new IllegalStateException("Processing area has not been set");
        }
        return _numPixels;
    }
    
    protected void setTransform(String imageVarName, CoordinateTransform tr) {
        TransformInfo info = new TransformInfo();
        
        if (tr == null) {
            info.transform = _defaultTransform;
            info.isDefault = true;
            
        } else {
            info.transform = tr;
            info.isDefault = false;
        }
        
        _transformLookup.put(imageVarName, info);
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultTransform(CoordinateTransform tr) {
        if (tr == null) {
            tr = new IdentityCoordinateTransform();
        }
        _defaultTransform = tr;
        
        for (String name : _transformLookup.keySet()) {
            TransformInfo info = _transformLookup.get(name);
            if (info.isDefault) {
                info.transform = _defaultTransform;
                _transformLookup.put(name, info);
            }
        }
    }
    
    
    
    protected CoordinateTransform getTransform(String imageVarName) {
        return _transformLookup.get(imageVarName).transform;
    }

    /**
     * Sets the value of an image-scope variable. If {@code value} is {@code null}
     * the variable is set to its default value if one is defined, otherwise an
     * exception is thrown.
     * 
     * @param index variable index
     * @param value the new value or {@code null} for default value
     * @throws JiffleRuntimeException if {@code value} is {@code null} but no default
     *         value is defined for the variable
     */
    protected void setVarValue(int index, Double value) throws JiffleRuntimeException {
        if (value == null) {
            if (!_vars[index].hasDefaultValue) {
                throw new JiffleRuntimeException(
                        "Value cannot be null for variable with no default: " + _vars[index].name);
            }
            
            _imageScopeVarsInitialized = false;
            _vars[index].isSet = false;
            
        } else {
            _vars[index].value = value;
            _vars[index].isSet = true;
        }
    }

    /**
     * Gets the index for an image-scope variable by name.
     * 
     * @param varName variable name
     * @return the index or -1 if the name is not found
     */
    protected int getVarIndex(String varName) {
        for (int i = 0; i < _numVars; i++) {
            if (_vars[i].name.equals(varName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Initializes image-scope variables. These are fields in the runtime class.
     * They are initialized in a separate method rather than the constructor
     * because they may depend on expressions involving values which are not
     * known until the processing area is set (e.g. Jiffle's width() function).
     * 
     * @throws JiffleRuntimeException if any variables do not have either a
     *         default or provided value
     */
    protected void initImageScopeVars() {
        for (int i = 0; i < _numVars; i++) {
            if (!_vars[i].isSet) {
                Double value = getDefaultValue(i);
                if (value == null) {
                    throw new JiffleRuntimeException(
                            "No default value set for " + _vars[i].name);
                }
                _vars[i].value = value;
                _vars[i].isSet = true;
            }
        }
        _imageScopeVarsInitialized = true;
    }
    
    /**
     * Gets the default value for an image-scope variable. This method is 
     * overridden as part of the generated run-time class code.
     * 
     * @param index the index of the variable
     * @return the default value or {@code null} if one is not defined
     */
    protected abstract Double getDefaultValue(int index);

    /**
     * Initializes runtime class fields related to Jiffle script options.
     */
    protected abstract void initOptionVars();

    /**
     * Registers a variable as having image scope.
     * 
     * @param name variable name
     * @param hasDefault whether the variable has a default value
     */
    protected void registerVar(String name, boolean hasDefault) {
        // check that the variable is not already registered
        if (getVarIndex(name) >= 0) {
            throw new JiffleRuntimeException("Variable already defined: " + name);
        }
        
        _numVars++ ;
        ImageScopeVar var = new ImageScopeVar(name, hasDefault);
        if (_numVars > _vars.length) {
            growVarsArray();
        }
        _vars[_numVars - 1] = var;
    }
    
    private void growVarsArray() {
        ImageScopeVar[] temp = _vars;
        _vars = new ImageScopeVar[_vars.length + VAR_ARRAY_CHUNK];
        System.arraycopy(temp, 0, _vars, 0, temp.length);
    }

    /**
     * Helper for {@link #setWorldByNumSteps(Rectangle2D, int, int)} and
     * {@link #setWorldByStepDistance(Rectangle2D, double, double)} methods.
     * 
     * @param bounds world bounds
     * @param xstep step distance in X direction
     * @param ystep step distance in Y direction
     */
    private void doSetWorld(Rectangle2D bounds, double xstep, double ystep) {
        checkStepDistance(xstep, Dim.XDIM, bounds);
        checkStepDistance(ystep, Dim.YDIM, bounds);
        
        _worldBounds = new Rectangle2D.Double(
                bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth(), bounds.getHeight());
        
        _xstep = xstep;
        _ystep = ystep;
        
        _worldSet = true;
    }
    
    /**
     * Helper method for {@link #setWorldByStepDistance(Rectangle2D, double, double)} to
     * check the validity of a step distance.
     * 
     * @param value step distance in world units
     * @param dim axis: Dim.XDIM or Dim.YDIM
     * @param bounds world area bounds
     */
    private void checkStepDistance(double value, Dim dim, Rectangle2D bounds) {
        String name = dim == Dim.XDIM ? "X step" : "Y step";
        
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " cannot be infinite");
        }
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(name + " cannot be NaN");
        }
        
        if (dim == Dim.XDIM && value > bounds.getWidth()) {
            throw new IllegalArgumentException(name + "should be less than processing area width");
            
        } else if (dim == Dim.YDIM && value > bounds.getHeight()) {
            throw new IllegalArgumentException(name + "should be less than processing area height");
        }
    }
    
}
