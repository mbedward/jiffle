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

import jaitools.jiffle.JiffleException;
import java.util.ArrayList;
import java.util.List;

/**
 * The default abstract base class for runtime classes that implement
 * indirect evaluation.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public abstract class AbstractIndirectRuntime extends AbstractJiffleRuntime implements JiffleIndirectRuntime {
    
    /* 
     * Note: not using generics here because they are not
     * supported by the Janino compiler.
     */
    List sourceImageNames = new ArrayList();
    String destImageName;

    /**
     * Creates a new instance and initializes script-option variables.
     */
    public AbstractIndirectRuntime() {
        initOptionVars();
    }

    public void setDestinationImage(String varName) {
        try {
            doSetDestinationImage(varName, null);
        } catch (WorldNotSetException ex) {
            // Passing a null transform does not cause an Exception
        }
    }

    public void setDestinationImage(String varName, CoordinateTransform tr) 
            throws JiffleException {
        try {
            doSetDestinationImage(varName, tr);
        } catch (WorldNotSetException ex) {
            throw new JiffleException(String.format(
                    "Setting a coordinate tranform for a source (%s) without"
                    + "having first set the world bounds and resolution", varName));
        }
    }
    
    private void doSetDestinationImage(String varName, CoordinateTransform tr)
            throws WorldNotSetException {
        
        destImageName = varName;
        setTransform(varName, tr);
    }

    public void setSourceImage(String varName) {
        try {
            doSetSourceImage(varName, null);
        } catch (WorldNotSetException ex) {
            // Passing a null transform does not cause an Exception
        }
    }

    public void setSourceImage(String varName, CoordinateTransform tr) 
            throws JiffleException {
        
        try {
            doSetSourceImage(varName, tr);
        } catch (WorldNotSetException ex) {
            throw new JiffleException(String.format(
                    "Setting a coordinate tranform for a source (%s) without"
                    + "having first set the world bounds and resolution", varName));
        }
    }

    private void doSetSourceImage(String varName, CoordinateTransform tr)
            throws WorldNotSetException {
        
        sourceImageNames.add(varName);
        setTransform(varName, tr);
    }

}
