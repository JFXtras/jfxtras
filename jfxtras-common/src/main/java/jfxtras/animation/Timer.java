/**
 * Timer.java
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

package jfxtras.animation;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

/**
 * A timer class in the spirit of java.swing.Timer but using JavaFX properties.
 * 
 * @author Tom Eugelink
 *
 */
public class Timer
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 * @param runnable
	 */
	public Timer(Runnable runnable)
	{
		this(true, runnable);
	}
	
	/**
	 * 
	 * @param isDaemon
	 * @param runnable
	 */
	public Timer(boolean isDaemon, final Runnable runnable)
	{
		this.runnable = runnable;
		this.isDaemon = isDaemon;
	}
	final private Runnable runnable;
	final private boolean isDaemon;
	volatile private java.util.Timer timer;
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** delay: initial delay */
	public ObjectProperty<Duration> delayProperty() { return this.delayObjectProperty; }
	final private ObjectProperty<Duration> delayObjectProperty = new SimpleObjectProperty<Duration>(this, "delay", Duration.millis(0));
	public Duration getDelay() { return this.delayObjectProperty.getValue(); }
	public void setDelay(Duration value) { this.delayObjectProperty.setValue(value); }
	public Timer withDelay(Duration value) { setDelay(value); return this; }
	
	/** cycleDuration: time between fires */
	public ObjectProperty<Duration> cycleDurationProperty() { return this.cycleDurationObjectProperty; }
	final private ObjectProperty<Duration> cycleDurationObjectProperty = new SimpleObjectProperty<Duration>(this, "cycleDuration", Duration.millis(1000));
	public Duration getCycleDuration() { return this.cycleDurationObjectProperty.getValue(); }
	public void setCycleDuration(Duration value) { this.cycleDurationObjectProperty.setValue(value); }
	public Timer withCycleDuration(Duration value) { setCycleDuration(value); return this; }
	
	/** repeats: If flag is false, instructs the Timer to send only one action event to its listeners. */
	public ObjectProperty<Boolean> repeatsProperty() { return this.repeatsObjectProperty; }
	final private ObjectProperty<Boolean> repeatsObjectProperty = new SimpleObjectProperty<Boolean>(this, "repeats", Boolean.TRUE);
	public boolean getRepeats() { return this.repeatsObjectProperty.getValue(); }
	public void setRepeats(boolean value) { this.repeatsObjectProperty.setValue(value); }
	public Timer withRepeats(boolean value) { setRepeats(value); return this; }
	
	
	// ==================================================================================================================
	// TIMER
	
	/**
	 * Start the timer
	 */
	synchronized public Timer start()
	{
		// check if the timer is already running
		if (timerTaskAtomicReference.get() != null) throw new IllegalStateException("Timer already started");
		
		// create a task and schedule it
		final TimerTask lTimerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				Platform.runLater(runnable);
				if (repeatsObjectProperty.getValue().booleanValue() == false)
				{
					stop();
				}
			}
		};
		if (timer == null) {
			timer = new java.util.Timer(isDaemon);
		}
		timer.schedule(lTimerTask, (long)this.delayObjectProperty.getValue().toMillis(), (long)this.cycleDurationObjectProperty.getValue().toMillis());
		
		// remember for future reference
		timerTaskAtomicReference.set(lTimerTask);
		
		// for chaining
		return this;
	}
	final private AtomicReference<TimerTask> timerTaskAtomicReference = new AtomicReference<TimerTask>(null);
	
	/**
	 * stop the timer if running
	 */
	public Timer stop()
	{
		TimerTask lTimerTask = timerTaskAtomicReference.getAndSet(null);
		if (lTimerTask != null)
		{
			lTimerTask.cancel();
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		
		// for chaining
		return this;
	}
	
	/**
	 * restart the timer
	 */
	public Timer restart()
	{
		stop();
		start();
		
		// for chaining
		return this;
	}
}
