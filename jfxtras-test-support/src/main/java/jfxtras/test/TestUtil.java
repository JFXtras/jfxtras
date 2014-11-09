/**
 * TestUtil.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

package jfxtras.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import com.sun.javafx.tk.Toolkit;

public class TestUtil {

	/**
	 * 
	 * @param s
	 */
	static public void printHierarchy(Stage s) {
		StringBuilder lStringBuilder = new StringBuilder();
		lStringBuilder.append("Stage\n|   Scene (window=" + s.getScene().getWindow() + ")");
		printHierarchy(lStringBuilder, s.getScene().getRoot(), 2);
		if (lStringBuilder.length() > 0) {
			lStringBuilder.append("\n");
		}
		System.out.println(lStringBuilder.toString());
	}
	
	/**
	 * 
	 * @param s
	 */
	static public void printHierarchy(Scene s) {
		StringBuilder lStringBuilder = new StringBuilder();
		lStringBuilder.append("Scene (window=" + s.getWindow() + ")");
		printHierarchy(lStringBuilder, s.getRoot(), 1);
		if (lStringBuilder.length() > 0) {
			lStringBuilder.append("\n");
		}
		System.out.println(lStringBuilder.toString());
	}
	
	/**
	 * 
	 * @param p
	 */
	static public void printHierarchy(Popup p) {
		StringBuilder lStringBuilder = new StringBuilder();
		lStringBuilder.append("Popup (owner=" + p.getOwnerNode() + ")");
		for (Object lChild : p.getContent()) {
			printHierarchy(lStringBuilder, (Node)lChild, 1);
		}
		if (lStringBuilder.length() > 0) {
			lStringBuilder.append("\n");
		}
		System.out.println(lStringBuilder.toString());
	}
	
	/**
	 * 
	 * @param n
	 */
	static public void printHierarchy(Node n) {
		StringBuilder lStringBuilder = new StringBuilder();
		printHierarchy(lStringBuilder, n, 0);
		if (lStringBuilder.length() > 0) {
			lStringBuilder.append("\n");
		}
		System.out.println(lStringBuilder.toString());
	}
	
	/**
	 * 
	 * @param stringBuilder
	 * @param n
	 * @param offset
	 */
	static private void printHierarchy(StringBuilder stringBuilder, Node n, int offset) {
		if (stringBuilder.length() > 0) {
			stringBuilder.append("\n");
		}
		for (int i = 0; i < offset; i++) stringBuilder.append("|   ");		
		stringBuilder.append(n.getClass().getSimpleName());
		if (n.getId() != null) {
			stringBuilder.append(" id='" + n.getId() + "'");
		}
		if (n.getStyle() != null && n.getStyle().length() > 0) {
			stringBuilder.append(" style='" + n.getStyle() + "'");
		}
		if (n.getStyleClass() != null && n.getStyleClass().size() > 0) {
			stringBuilder.append(" styleClass='" + n.getStyleClass() + "'");
		}
		
		// scan children
		if (n instanceof Control) {
			Control lControl = (Control)n;
			Skin lSkin = lControl.getSkin();
			stringBuilder.append(" skin=" + (lSkin == null ? "null" : lSkin.getClass().getSimpleName()) );
			if (lSkin instanceof SkinBase) {
				SkinBase lSkinBase = (SkinBase)lSkin;
				for (Object lChild : lSkinBase.getChildren()) {
					printHierarchy(stringBuilder, (Node)lChild, offset + 1);
				}
			}
			if (lControl instanceof Label) {
				Label lLabel = (Label)lControl;
				stringBuilder.append(" text=" + lLabel.getText() );
			}
		}
		else if (n instanceof Pane) {
			Pane lPane = (Pane)n;
			for (Node lChild : lPane.getChildren()) {
				printHierarchy(stringBuilder, lChild, offset + 1);
			}
		}
	}
	
	/**
	 * 
	 */
	static public String quickFormatCalendarAsDate(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 */
	static public String quickFormatCalendarAsTime(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 */
	static public String quickFormatCalendarAsDateTime(Calendar value) {
		if (value == null) return "null";
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return value == null ? "null" : lSimpleDateFormat.format(value.getTime());
	}
	
	/**
	 * 
	 */
	static public Calendar quickParseCalendarFromDateTime(String value) {
		try {
			if (value == null) return null;
			SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Calendar c = Calendar.getInstance();
			c.setTime( lSimpleDateFormat.parse(value) );
			return c;
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static LocalDateTime quickParseLocalDateTimeYMDhm(String value) {
		if (value == null) return null;
		DateTimeFormatter lDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime dt = LocalDateTime.parse(value, lDateTimeFormatter);
		return dt;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendarsAsDate(List<Calendar> value) {
		if (value == null) return "null";
		String s = "[";
		for (Calendar lCalendar : value)
		{
			if (s.length() > 1) s += ", ";
			s += quickFormatCalendarAsDate(lCalendar);
		}
		s += "]";
		return s;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	static public String quickFormatCalendarsAsDateTime(List<Calendar> value) {
		if (value == null) return "null";
		String s = "[";
		for (Calendar lCalendar : value)
		{
			if (s.length() > 1) s += ", ";
			s += quickFormatCalendarAsDateTime(lCalendar);
		}
		s += "]";
		return s;
	}
	
	
	/**
	 * 
	 * @param ms
	 */
	static public void sleep(int ms) {
		try { 
			Thread.sleep(ms); 
		} 
		catch (InterruptedException e) { 
			throw new RuntimeException(e); 
		}
	}
	
	
	/**
	 * This method also exist in PlatformUtil in commons, but we can't use that here
	 */
	static public void runAndWait(final Runnable runnable) {
		try {
			FutureTask future = new FutureTask(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method also exist in PlatformUtil in commons, but we can't use that here
	 */
	static public <V> V runAndWait(final Callable<V> callable) throws InterruptedException, ExecutionException {
		FutureTask<V> future = new FutureTask<>(callable);
		Platform.runLater(future);
		return future.get();
	}
	
	/**
	 * This method also exist in PlatformUtil in commons, but we can't use that here
	 */
	static public void waitForPaintPulse() {
		runAndWait( () -> {
			Toolkit.getToolkit().firePulse();
		});
	}

	/**
	 * 
	 * @param r
	 */
	static public void runThenWaitForPaintPulse(Runnable r) {
		runAndWait(r);
		waitForPaintPulse();
	}
	
	/**
	 * 
	 * @param r
	 * @return
	 */
	static public <T> T runThenWaitForPaintPulse(Callable<T> r)  {
		try {
			T t = runAndWait(r);
			waitForPaintPulse();
			return t;
		}
		catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
