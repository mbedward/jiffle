/*
 * Copyright 2009-2011 Michael Bedward
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

/**
 * Jiffle is a scripting language for creating and analysing raster images.
 * The main intention of Jiffle is to free you from having to write lots
 * of Java and JAI boiler-plate code.
 * <p>
 * Jiffle scripts are compiled to bytecode. The compiler first
 * translates the script into Java source which is then passed to an
 * embedded Janino compiler to produce executable bytecode in memory. The
 * resulting run-time object can then be used by client code as a normal
 * compiled java class.
 * <p>
 * For an introduction to Jiffle see the User Guide at:
 * http://jai-tools.org/docs/jiffle/latest/html/index.html
 */

package jaitools.jiffle;
