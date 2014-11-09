/**
 * JFXtrasGuiTest.java
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

import java.util.Locale;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.stage.Window;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.exceptions.NoNodesFoundException;

/**
 * 
 * @author Tom Eugelink
 * https://github.com/SmartBear/TestFX/blob/master/src/main/java/org/loadui/testfx/GuiTest.java
 */
abstract public class JFXtrasGuiTest extends org.loadui.testfx.GuiTest {
	
	@Rule public TestName testName = new TestName();
	
	public JFXtrasGuiTest() {
	}
	
	@Before
	public void before() {
		System.out.println("========================================================================\n" + testName.getMethodName());
		
		// default we're in US locale: keep (re)setting this for each test
		Locale.setDefault(Locale.US);
	}
	
	@After
	public void after() {
		// this is required, otherwise a popup from a previous test may influence the active test
		forceCloseAllPopups();
	}
	
	/**
	 * Click with a qualifier pressed
	 * @param matcher
	 * @param keyCode
	 * @param keyCodes
	 */
	public GuiTest click(String matcher, KeyCode keyCode, KeyCode... keyCodes) {
		press(keyCode);
		for (int i = 0; i < keyCodes.length; i++) {
			press(keyCodes[i]);
		}
		click(matcher);
		for (int i = keyCodes.length - 1; i >=0 ; i--) {
			release(keyCodes[i]);
		}
		return release(keyCode);
	}
	
	protected void assertPopupIsNotVisible(Node ownedBy) {
		TestUtil.waitForPaintPulse();
		for (Window w : getWindows() ) {
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
		for (Window w : getWindows() ) {
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
		for (Window w : getWindows() ) {
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
		click(textField);
		push(KeyCode.CONTROL, KeyCode.A);
		push(KeyCode.DELETE);
	}
	

	protected void assertNotFind(String string) {
		try {
			find(string);
			Assert.assertTrue("Should not have found '" + string + "', but did anyway", false);
		}
		catch (NoNodesFoundException e) {
			// all is well
		}
	}

	protected void assertFind(String string) {
		find(string);
	}
}
