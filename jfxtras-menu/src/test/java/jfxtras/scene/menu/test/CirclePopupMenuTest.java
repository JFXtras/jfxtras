/**
 * CirclePopupMenuTest.java
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

package jfxtras.scene.menu.test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.menu.CirclePopupMenu;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;
import jfxtras.util.Implements;
import jfxtras.util.NodeUtil;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class CirclePopupMenuTest extends JFXtrasGuiTest {

	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough
		stackPane = new StackPane();
		stackPane.setMinSize(600, 600);
		
		// place label
		label = new Label();
		stackPane.getChildren().add(label);
		
		// create menu
		circlePopupMenu = new CirclePopupMenu(this.stackPane, MouseButton.SECONDARY)
			.withAnimationInterpolation(null);
		circlePopupMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);

		return stackPane;
	}
	private StackPane stackPane = null;
	private Label label = null;
	private CirclePopupMenu circlePopupMenu = null;
	final private MenuItem facebookMenuItem = new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png"))));
	final private MenuItem googleMenuItem = new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png"))));
	final private MenuItem skypeMenuItem = new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png"))));
	final private MenuItem twitterMenuItem = new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png"))));
	final private MenuItem windowsMenuItem = new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png"))));
	
	@Test
	public void showAndHide() {
		setLabel("showAndHide");

		// not visible
		Assert.assertFalse(circlePopupMenu.isShown());
		
		// click in the center
		moveMouseToCenter();
		clickOn(MouseButton.SECONDARY);
		Assert.assertTrue(circlePopupMenu.isShown());
		//generateSource();
		assertWH(findCircularPane(), 118.47869355660265, 114.89486277938934);
		new AssertNode(findCircularPane().getChildren().get(0)).assertXYWH(65.86676377627084, 6.627416997969522, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CirclePopupMenu$CirclePopupMenuNode");
		new AssertNode(findCircularPane().getChildren().get(1)).assertXYWH(79.85127655863312, 49.66732176366279, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CirclePopupMenu$CirclePopupMenuNode");
		new AssertNode(findCircularPane().getChildren().get(2)).assertXYWH(43.239346778301325, 76.26744578141981, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CirclePopupMenu$CirclePopupMenuNode");
		new AssertNode(findCircularPane().getChildren().get(3)).assertXYWH(6.627416997969522, 49.66732176366279, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CirclePopupMenu$CirclePopupMenuNode");
		new AssertNode(findCircularPane().getChildren().get(4)).assertXYWH(20.611929780331792, 6.627416997969526, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CirclePopupMenu$CirclePopupMenuNode");
		
		// move out of the menu
		moveMouseToLeftCorner();
		Assert.assertFalse(circlePopupMenu.isShown());
	}


	@Test
	public void isClickHandled() {
		setLabel("isClickHandled");

		// show menu
		moveMouseToCenter();
		clickOn(MouseButton.SECONDARY);

		// click first item
		Assert.assertEquals(0, menuItemClickAtomicInteger.get());
		facebookMenuItem.setOnAction(event -> menuItemClickAtomicInteger.incrementAndGet()); // this should be #1
		clickOn("#CirclePopupMenuNode#1");
		Assert.assertEquals(1, menuItemClickAtomicInteger.get());
		Assert.assertFalse(circlePopupMenu.isShown());
		
		// show menu
		moveMouseToCenter();
		clickOn(MouseButton.SECONDARY);
		
		// click second menu item
		clickOn("#CirclePopupMenuNode#2"); // this has no action handler attached
		Assert.assertEquals(1, menuItemClickAtomicInteger.get());
		Assert.assertFalse(circlePopupMenu.isShown());
	}
	private final AtomicInteger menuItemClickAtomicInteger = new AtomicInteger();


	@Test
	public void isClickedThrough() {
		setLabel("isClickedThrough");
		
		// create underlying button
		AtomicInteger underlyingClickAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			Button lUnderlyingButton = new Button("click me but a bit longer");
			lUnderlyingButton.setId("UnderlyingButton");
			stackPane.getChildren().add(1, lUnderlyingButton); // index = 1: below menu, above label
			lUnderlyingButton.setOnAction((actionEvent) -> {
				underlyingClickAtomicInteger.incrementAndGet();
			}); 
		});

		// click underlying button
		Assert.assertEquals(0, underlyingClickAtomicInteger.get());
		clickOn("#UnderlyingButton");
		Assert.assertEquals(1, underlyingClickAtomicInteger.get());

		// show menu (positioned over the button)
		moveMouseToCenter();
		clickOn(MouseButton.SECONDARY);
		
		// click item that is placed over the button
		clickOn("#CirclePopupMenuNode#4");
		Assert.assertFalse(circlePopupMenu.isShown());

		// click underlying button again
		clickOn("#UnderlyingButton");
		Assert.assertEquals(2, underlyingClickAtomicInteger.get());
	}

	// =============================================================================================================================================================================================================================
	// SUPPORT

	private void assertWH(Pane pane, double w, double h) {
		Assert.assertEquals(w, pane.getWidth(), 0.01);
		Assert.assertEquals(h, pane.getHeight(), 0.01);
	}
	
	private void setLabel(String s) {
		TestUtil.runThenWaitForPaintPulse( () -> {
			label.setText(s);
		});
	}

	private void generateSource() {
		Pane pane = findCircularPane();
		System.out.println("> " + label.getText()); 
		System.out.println("assertWH(findCircularPane(), " + pane.getWidth() + ", " + pane.getHeight() + ");");
		AssertNode.generateSource("findCircularPane()", pane.getChildren(), EXCLUDED_CLASSES, false, A.XYWH, A.CLASSNAME);
		TestUtil.sleep(3000);
	}
	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList(new String[]{"jfxtras.scene.layout.CircularPane$Bead", "jfxtras.scene.layout.CircularPane$Connector"});
	
	private CircularPane findCircularPane() {
		return (CircularPane)findPopup(null).getContent().get(0);
	}
	
	private void moveMouseToCenter() {
		moveTo(NodeUtil.screenX(stackPane) + (stackPane.getWidth() / 2), NodeUtil.screenY(stackPane) + (stackPane.getHeight() / 2));
	}
	
	private void moveMouseToLeftCorner() {
		moveTo(NodeUtil.screenX(stackPane) + 5,  NodeUtil.screenY(stackPane) + 5);
	}
	
	protected Popup findPopup(Node ownedBy) {
		TestUtil.waitForPaintPulse();
		for (Window w : listWindows() ) {
			if (w instanceof Popup) {
				Popup lPopup = (Popup)w;
				if (ownedBy == null || ownedBy.equals(lPopup.getOwnerNode())) {
					return lPopup;
				}
			}
		}
		return null; 
	}

}
