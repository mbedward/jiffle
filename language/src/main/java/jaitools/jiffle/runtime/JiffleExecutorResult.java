/* 
 *  Copyright (c) 2011, Michael Bedward. All rights reserved. 
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

import java.awt.image.RenderedImage;
import java.util.Map;

import jaitools.jiffle.Jiffle;

/**
 * Used by {@link JiffleExecutor} to send the results of a task to 
 * {@link JiffleEventListener}s.
 * 
 * @author Michael Bedward
 * @since 0.1
 * @version $Id$
 */
public class JiffleExecutorResult {

    private final int jobID;
    private final JiffleDirectRuntime runtime;
    private final boolean completed;

    /**
     * Creates a new result object.
     * 
     * @param taskID the task ID assigned by the executor
     * @param runtime the run-time instance
     * @param completed whether the task was completed successfully
     */
    public JiffleExecutorResult(int taskID, JiffleDirectRuntime runtime, boolean completed) {
        this.jobID = taskID;
        this.runtime = runtime;
        this.completed = completed;
    }

    /**
     * Gets source and/or destination images that were used.
     * 
     * @return the images keyed by script variable name
     */
    public Map<String, RenderedImage> getImages() {
        return runtime.getImages();
    }

    /**
     * Gets the {@link Jiffle} object
     * 
     * @return the {@link Jiffle} object
     */
    public JiffleDirectRuntime getRuntime() {
        return runtime;
    }

    /**
     * Gets the task ID assigned by the executor.
     * 
     * @return task ID
     */
    public int getTaskID() {
        return jobID;
    }

    /**
     * Gets the completion status of the task.
     * 
     * @return {@code true} if the task was completed; {@code false} otherwise
     */
    public boolean isCompleted() {
        return completed;
    }
    
}
