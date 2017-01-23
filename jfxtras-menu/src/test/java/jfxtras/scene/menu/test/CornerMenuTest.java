/**
 * CornerMenuTest.java
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

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.menu.CornerMenu;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.TestUtil;
import jfxtras.util.Implements;
import jfxtras.util.NodeUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Tom Eugelink
 *
 */
public class CornerMenuTest extends JFXtrasGuiTest {

	@Override
	protected Parent getRootNode() {
		// use a pane to force the scene large enough
		stackPane = new StackPane();
		stackPane.setMinSize(600, 600);
		
		// place label
		label = new Label();
		stackPane.getChildren().add(label);
		
		return stackPane;
	}
	private StackPane stackPane = null;
	private Label label = null;
	private CornerMenu cornerMenu = null;
	final private MenuItem facebookMenuItem = new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png"))));
	final private MenuItem googleMenuItem = new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png"))));
	final private MenuItem skypeMenuItem = new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png"))));
	final private MenuItem twitterMenuItem = new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png"))));
	final private MenuItem windowsMenuItem = new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png"))));
	
	@Test
	public void topLeft() {
		setLabel("topLeft");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.TOP_LEFT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		//generateSource();
		assertWH(findCircularPane(), 165.4913053420834, 165.49130534208345);
		new AssertNode(findCircularPane().getChildren().get(0)).assertXYWH(126.86388834411386, 6.627416997969533, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(1)).assertXYWH(112.87937556175157, 49.667321763662784, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(2)).assertXYWH(86.2792515439946, 86.2792515439946, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(3)).assertXYWH(49.667321763662784, 112.87937556175163, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(4)).assertXYWH(6.627416997969533, 126.86388834411392, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
	}

	@Test
	public void topRight() {
		setLabel("topRight");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.TOP_RIGHT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		//generateSource();
		assertWH(findCircularPane(), 165.49130534208342, 165.49130534208342);
		new AssertNode(findCircularPane().getChildren().get(0)).assertXYWH(126.86388834411389, 126.86388834411389, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(1)).assertXYWH(83.82398357842062, 112.8793755617516, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(2)).assertXYWH(47.21205379808883, 86.27925154399458, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(3)).assertXYWH(20.611929780331806, 49.66732176366281, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(4)).assertXYWH(6.627416997969522, 6.627416997969533, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
	}

	@Test
	public void bottomRight() {
		setLabel("bottomRight");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.BOTTOM_RIGHT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		//generateSource();
		assertWH(findCircularPane(), 165.49130534208342, 165.49130534208342);
		new AssertNode(findCircularPane().getChildren().get(0)).assertXYWH(6.627416997969522, 126.86388834411386, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(1)).assertXYWH(20.611929780331806, 83.82398357842062, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(2)).assertXYWH(47.21205379808882, 47.21205379808883, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(3)).assertXYWH(83.82398357842061, 20.611929780331806, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(4)).assertXYWH(126.86388834411389, 6.627416997969522, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
	}

	@Test
	public void bottomLeft() {
		setLabel("bottomLeft");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.BOTTOM_LEFT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		//generateSource();
		assertWH(findCircularPane(), 165.49130534208345, 165.49130534208342);
		new AssertNode(findCircularPane().getChildren().get(0)).assertXYWH(6.627416997969533, 6.627416997969522, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(1)).assertXYWH(49.66732176366273, 20.61192978033175, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(2)).assertXYWH(86.2792515439946, 47.21205379808879, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(3)).assertXYWH(112.87937556175163, 83.8239835784206, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
		new AssertNode(findCircularPane().getChildren().get(4)).assertXYWH(126.86388834411392, 126.86388834411386, 32.0, 32.0, 0.01).assertClassName("jfxtras.scene.menu.CornerMenu$CornerMenuNode");
	}

	@Test
	public void isClickHandled() {
		setLabel("isClickHandled");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.TOP_RIGHT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		Assert.assertEquals(0, menuItemClickAtomicInteger.get());
		facebookMenuItem.setOnAction(event -> menuItemClickAtomicInteger.incrementAndGet()); // this should be #1
		clickOn("#CornerMenuNode#1");
		clickOn("#CornerMenuNode#2"); // this has no action handler attached
		Assert.assertEquals(1, menuItemClickAtomicInteger.get());
	}
	private final AtomicInteger menuItemClickAtomicInteger = new AtomicInteger();

	@Test
	public void isClickedThrough() {
		setLabel("isClickedThrough");
		
		// setup
		AtomicInteger underlyingClickAtomicInteger = new AtomicInteger();
		TestUtil.runThenWaitForPaintPulse( () -> {
			Button lUnderlyingButton = new Button("click me");
			lUnderlyingButton.setId("UnderlyingButton");
			stackPane.getChildren().add(lUnderlyingButton);
			lUnderlyingButton.setOnAction((actionEvent) -> {
				underlyingClickAtomicInteger.incrementAndGet();
			}); 
			
			cornerMenu = new CornerMenu(CornerMenu.Location.TOP_RIGHT, this.stackPane, true)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(false);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		Assert.assertEquals(0, underlyingClickAtomicInteger.get());
		clickOn("#UnderlyingButton");
		Assert.assertEquals(1, underlyingClickAtomicInteger.get());
	}


	@Test
	public void autoShowAndHide() {
		setLabel("autoShowAndHide");
		
		// setup
		TestUtil.runThenWaitForPaintPulse( () -> {
			cornerMenu = new CornerMenu(CornerMenu.Location.TOP_LEFT, this.stackPane, false)
				.withAnimationInterpolation(null)
				.withAutoShowAndHide(true);
			cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		});

		// not visible 
		Assert.assertFalse(cornerMenu.isShown());
		
		// move mouse to top left corner
		moveMouseToLeftCorner();
		Assert.assertTrue(cornerMenu.isShown());
		
		// move mouse to center
		moveMouseToCenter();
		Assert.assertFalse(cornerMenu.isShown());
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
		return (CircularPane)stackPane.getChildren().get(1);		
	}
	
	private void moveMouseToCenter() {
		moveTo(NodeUtil.screenX(stackPane) + (stackPane.getWidth() / 2), NodeUtil.screenY(stackPane) + (stackPane.getHeight() / 2));
	}
	
	private void moveMouseToLeftCorner() {
		moveTo(NodeUtil.screenX(stackPane) + 5,  NodeUtil.screenY(stackPane) + 5);
	}
}
