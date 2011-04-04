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

package jaitools.jiffle;

import java.awt.geom.Rectangle2D;

import jaitools.jiffle.runtime.CoordinateTransform;
import jaitools.jiffle.runtime.JiffleRuntime;
import jaitools.jiffle.runtime.JiffleRuntimeException;

/**
 * A stub class used in unit tests.
 *
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class NullRuntime implements JiffleRuntime {

    public Double getVar(String varName) {
        return null;
    }

    public void setVar(String varName, Double value) throws JiffleRuntimeException {}

    public void setWorldByStepDistance(Rectangle2D bounds, double xstep, double ystep) {}

    public void setWorldByNumSteps(Rectangle2D bounds, int nx, int ny) {}

    public boolean isWorldSet() {
        return true;
    }

    public double getMinX() {
        return 0;
    }

    public double getMaxX() {
        return 0;
    }

    public double getMinY() {
        return 0;
    }

    public double getMaxY() {
        return 0;
    }

    public double getWidth() {
        return 0;
    }

    public double getHeight() {
        return 0;
    }

    public double getXStep() {
        return 0;
    }

    public double getYStep() {
        return 0;
    }

    public long getNumPixels() {
        return 0;
    }

    public void setDefaultTransform(CoordinateTransform tr) {}

}
