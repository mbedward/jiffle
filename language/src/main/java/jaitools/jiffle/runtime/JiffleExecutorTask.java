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
     * @param id job ID allocated by the {@link JiffleExecutor}.
     * @param runtime the {@link JiffleDirectRuntime} instance
     * @param images a {@code Map} with image variable name as key and the
     *        corresponding source or destination image as value 
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

