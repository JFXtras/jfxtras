/**
 * PlatformUtil.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;

import com.sun.javafx.tk.Toolkit;

/**
 * Created by Tom Eugelink on 26-12-13.
 */
public class PlatformUtil {

	/**
	 * Invokes a Runnable in JFX Thread and waits until it's finished. 
	 * Similar to SwingUtilities.invokeAndWait.
	 * This method is not intended to be called from the FAT, but when this happens the runnable is executed synchronously. 
	 *
	 * @param runnable
	 *            The Runnable that has to be executed on JFX application thread.
	 * @throws RuntimeException which wraps a possible InterruptedException or ExecutionException 
	 */
	static public void runAndWait(final Runnable runnable) {
		// running this from the FAT 
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		
		// run from a separate thread
		try {
			FutureTask<Void> future = new FutureTask<>(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invokes a Callable in JFX Thread and waits until it's finished. 
	 * Similar to SwingUtilities.invokeAndWait.
	 * This method is not intended to be called from the FAT, but when this happens the callable is executed synchronously. 
	 *
	 * @param callable
	 *            The Callable that has to be executed on JFX application thread.
	 * @return the result of callable.call();
	 * @throws RuntimeException which wraps a possible Exception, InterruptedException or ExecutionException 
	 */
	static public <V> V runAndWait(final Callable<V> callable) {
		// running this from the FAT 
		if (Platform.isFxApplicationThread()) {
			try {
				return callable.call();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		// run from a separate thread
		try {
			FutureTask<V> future = new FutureTask<>(callable);
			Platform.runLater(future);
			return future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	static public void waitForPaintPulse() {
		PlatformUtil.runAndWait( () -> {
			Toolkit.getToolkit().firePulse();
		});
	}
}

