/**
 * JFXtrasGuiTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxRobot;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * @author Tom Eugelink
 */
abstract public class JFXtrasGuiTest extends org.testfx.framework.junit.ApplicationTest {
	
	@Rule public TestName testName = new TestName();

	// TODO: let's implement this as a rule so we can keep using the @Before
	@Before
	public void before() throws Throwable {
		System.out.println("========================================================================\n" + this.getClass().getSimpleName() + "." + testName.getMethodName());
		
		// default we're in US locale: keep (re)setting this for each test
		Locale.setDefault(Locale.US);

		// catch any exception during rendering
		Thread.currentThread().setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Assert.fail("This should not occur\n" + e);
			}
		});
		
		// small delay to get things going
		TestUtil.waitForPaintPulse();
	}
	
	@After
	public void after() {
		// this is required, otherwise a popup from a previous test may influence the active test
		forceCloseAllPopups();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Scene scene = new Scene(getRootNode());
        stage.setScene(scene);
        stage.show();
	}
	protected Stage stage;
	
	/**
	 * @return
	 */
	protected Parent getRootNode() {
		if (1 == 1) {
			throw new IllegalStateException("This method should not be called");
		}
		return null;
	}

	
	/**
	 * Click with a qualifier pressed
	 * @param matcher
	 * @param keyCode
	 * @param keyCodes
	 */
	public FxRobot clickOn(String matcher, KeyCode keyCode, KeyCode... keyCodes) {
		press(keyCode);
		for (int i = 0; i < keyCodes.length; i++) {
			press(keyCodes[i]);
		}
		clickOn(matcher);
		for (int i = keyCodes.length - 1; i >=0 ; i--) {
			release(keyCodes[i]);
		}
		return release(keyCode);
	}
	
	protected void assertPopupIsNotVisible(Node ownedBy) {
		TestUtil.waitForPaintPulse();
		for (Window w : listWindows() ) {
			if (w instanceof Popup) {
				Popup lPopup = (Popup)w;
				if (ownedBy.equals(lPopup.getOwnerNode())) {
					throw new IllegalStateException("Popup is visible (and should not be), owner = " + lPopup.getOwnerNode());
				}
			}
		}
	}
	
	protected void assertPopupIsVisible(Node ownedBy) {
		TestUtil.waitForPaintPulse();
		for (Window w : listWindows() ) {
			if (w instanceof Popup) {
				Popup lPopup = (Popup)w;
				if (ownedBy.equals(lPopup.getOwnerNode())) {
					return;
				}
			}
		}
		throw new IllegalStateException("Popup is not visible (and should be)"); 
	}

	protected void forceCloseAllPopups() {
		TestUtil.waitForPaintPulse();
		for (Window w : listWindows() ) {
			if (w instanceof Popup) {
				Popup lPopup = (Popup)w;
				System.out.println("force closing popup: " + lPopup);
				TestUtil.runThenWaitForPaintPulse( () -> {
					lPopup.hide();
				});
			}
		}
	}
	
	protected void clear(Node textField) {
		clickOn(textField);
		push(KeyCode.CONTROL, KeyCode.A);
		push(KeyCode.DELETE);
	}
	

	protected void assertNotFind(String string) {
		if (find(string) != null) {
			Assert.fail("Expected not to find: " + string);
		}
	}

	protected void assertNotVisible(String string) {
		Node lNode = find(string);
		if (lNode.isVisible()) {
			Assert.fail("Should not have found '" + string + "', but did anyway");
		}
	}

	protected void assertFind(String string) {
		if (find(string) == null) {
			Assert.fail("Not found: " + string);
		}
	}
	
	protected <T extends Node> T find(String matcher) {
		Set<Node> lNodes = lookup(matcher).queryAll();
		if (lNodes.isEmpty()) {
			return null;
		}
		return (T)lNodes.iterator().next();
	}
	
	/**
	 * Use clickOn
	 */
	@Deprecated
	protected void click(String matcher) {
		clickOn(matcher);
	}

	/**
	 * Use clickOn
	 */
	public void click(String matcher, KeyCode keyCode, KeyCode... keyCodes) {
		clickOn(matcher, keyCode, keyCodes);
	}

	/**
	 * Use moveTo
	 */
	@Deprecated
	protected void move(String matcher) {
		moveTo(matcher);
	}
	
	/**
	 * Use write
	 */
	@Deprecated
	protected void type(String text) {
		write(text);
	}
}
