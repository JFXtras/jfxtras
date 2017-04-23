package jfxtras.scene.layout.responsivepane;

import java.util.List;
import java.util.TreeMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * = ResponsivePane
 * 
 * This layout chooses the best fitting layout and stylesheet for a given stage size.
 * 
 * ResponsivePane is loosely based on responsive design as advocated by website designs, and implemented in for example the Twitter Bootstrap project.
 * But there is a twist in the logic; responsive design assumes a given width and unlimited vertical space with the use of a scrollbar.
 * For websites this is ok, but applications do not have unlimited vertical space. 
 * Take for example a webbrowser; it contains a webview with a scrollbar, but it does not run inside a scrollpane itself.
 * So for applications there is no such thing as unlimited vertical space!
 * It has to use scalable controls, like lists, tables ort webview, to dynamically fill its horizontal and vertical space, just like we are used to in Swing and JavaFX for ages.
 * But given large screen size differences, from 4 inch phones to 32 inch desktops, it is ridiculous to think that a single layout can adapt to all sizes: on a phone you may choose to use a TabbedPane with less controls visible, and on desktop you go all out with a MigPane and a number of gimmicks.   
 * ResponsiveLayout allows you to define reusable nodes and have specific screen size related layouts, using the reusable nodes.
 * It will then use what layout fits best.  
 * 
 * A typical use of ResponsivePane would look like this:
 * [source,java]
 * --
    @Override
    public void start(Stage primaryStage) throws Exception {
    
        // create pane with reusable nodes
        ResponsivePane lResponsivePane = new ResponsivePane();
        lResponsivePane.addReusableNode("CalendarPicker", new CalendarPicker());
        lResponsivePane.addReusableNode("TreeView", new TreeView());
        lResponsivePane.addReusableNode("TableView", new TableView());
        lResponsivePane.addReusableNode("save", new Button("save"));
        lResponsivePane.addReusableNode("saveAndTomorrow", new Button("saveAndTomorrow"));
        lResponsivePane.addReusableNode("-", new Button("-"));
        lResponsivePane.addReusableNode("+", new Button("+"));
        lResponsivePane.addReusableNode("Logo", new Button("Logo"));
        lResponsivePane.addReusableNode("version", new Label("v1.0"));

        // define custom device
        lResponsivePane.setDeviceSize("PHABLET", Diagonal.inch(9.0));

        // define layouts
        lResponsivePane.addLayout(Diagonal.inch(3.5), createPhoneLayout());
        lResponsivePane.addLayout(Diagonal.inch(12.0), createDesktopLayout());
                
        // define css
        lResponsivePane.addSceneStylesheet(Diagonal.inch(4.0), getClass().getResource("phone.css").toExternalForm());
        lResponsivePane.addSceneStylesheet(Diagonal.inch(6.0), getClass().getResource("tablet.css").toExternalForm());
        lResponsivePane.addSceneStylesheet(Diagonal.inch(12.0), getClass().getResource("desktop.css").toExternalForm());
    }
    
    private Node createDesktopLayout() {
        MigPane migPane = new MigPane();
        migPane.add(new Ref("Logo"), new CC());
        migPane.add(new Ref("version"), new CC().spanX(2).alignX("right").alignY("top").wrap());
        migPane.add(new Ref("CalendarPicker"), new CC().alignY("top"));
        migPane.add(new Ref("TreeView"), new CC().grow().pushX().spanY(3).wrap());
        ...
        
        return migPane;
    }
    
    private Node createPhoneLayout() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab("Date", "Calendar", new Ref("CalendarPicker")));
        tabPane.getTabs().add(createTab("Tree", "Projectboom", new Ref("TreeView")));
        tabPane.getTabs().add(createTab("Calc", "Totalen", new Ref("TreeView")));
        ... 
           
        return tabPane;
    }

 * --
 * 
 * The exact implementation of createPhoneLayout() and createDesktopLayout() is not relevant, except that they use "Ref" nodes to include the reusable nodes.
 *  
 * == Reusable nodes:
 * Reusable nodes are identified by their id, this is done for ease of use in FXML. 
 * They can then be used in the different layouts via a "new Ref(<id>)". 
 * Be aware that you cannot reuse Refs, they are place holders and each layout needs a Ref instance on its own. 
 * 
 * 
 * == Stylesheets:
 * Like layouts it is also possible to activate stylesheets based on the size of the stage. 
 * A stylesheet can be assigned to the scene (applicable for everything on the scene) or to the layout (applicable for the underlying node).
 * Responsive design assigned classes to the nodes, based on the size. 
 * Test have shown that this approach works ok when running the application on a desktop computer, with enough CPU power (GUI Garage's implementation does this), but on mobile these changes have a sever performance impact.
 * So ResponsivePane does not assign classes, but instead it replaces whole CSS files; for the CSS engine this is a single change and it needs to rerender only once.
 * In those CSS files you can assign behavior to the CSS classes, or not.
 * 
 * [source,java]
 * --
		// scene stylesheet
		responsivePane.addSceneStylesheet(Diagonal.inch(1.0), getClass().getResource("phone.css").toExternalForm());
			
		// pane stylesheet
		responsivePane.addMyStylesheet(Diagonal.inch(3.0), getClass().getResource("tablet.css").toExternalForm());
 * --
 *
 * There can only be one scene or layout stylesheet active at a given time.
 * 
 * == Size:
 * Determining the size is an interesting topic. 
 * Responsive design only looks at the width in pixel, but it is already explained that ResponsivePane does not assume unlimited vertical space, so the height must also be taking into account.
 * Therefore size is expressed as a diagonal instead of the width.
 * However, responsive design specifies the width in pixels, but 1000 pixels on a 100 ppi screen is something completely different on a 350 ppi screen; the first is 10 inches in real life, the second not even 3 inch!
 * ResponsivePane therefore (per default) expresses the size in inches (or cm), as we are used to do when talking about devices; a 4 inch phone, a 27 inch monitor.
 * But as a compatibility behavior it also is possible to the width; it will be at runtime converted to diagonal using the actual height of the layout. 
 * 
 * There also is a third option to set the size; ResponsivePane has some default diagonals for typical devices in the Device class, but you can also defined custom devices.
 * 
 * So sizes for layout and stylesheets can be defined like:
 * [source,java]
 * --
        responsivePane.addLayout(Diagonal.inch(3.5), ...);
        responsivePane.addLayout(Width.inch(3.0), ...);
        responsivePane.addLayout(Device.PHONE, ...);
        
        responsivePane.setDeviceSize("PHABLET", Diagonal.inch(9.0));
        responsivePane.addLayout(DeviceSize.of("PHABLET"), ...);

 * --
 * 
 * == FXML
 * ResponsivePane can of course also be used from FXML. 
 * Interesting is the how the width-based size is specified.
 *  
 * [source,xml]
 * --
	<?xml version="1.0" encoding="UTF-8"?>
	
	<?import java.lang.*?>
	<?import java.util.*?>
	<?import javafx.scene.control.*?>
	<?import javafx.scene.layout.*?>
	<?import javafx.scene.paint.*?>
	<?import jfxtras.scene.control.*?>
	<?import jfxtras.scene.layout.responsivepane.*?>
	<?import java.net.*?>
	
	<ResponsivePane xmlns:fx="http://javafx.com/fxml" fx:id="responsivePane" debug="true" trace="false">
		<reusableNodes>
			<Label text="refLabel" id="label"/>
			<Button text="refButton" id="button"/>
		</reusableNodes>
		
		<deviceSizes PHABLET="9.0in" Desktop="w:12in"/>
		
		<layouts>
			<Layout sizeAtLeast="3.0in">
				<VBox>
					<Ref to="label"/>
					<Label text="layout_3.0"/>
				</VBox>
			</Layout>
			
			<Layout sizeAtLeast="width:3.0in">
				<HBox>
					<Ref to="label" id="labelid"/>
					<Ref to="button"/>
					<Label text="layout_width3.0"/>
				</HBox>
			</Layout>		
			
			<Layout sizeAtLeast="TABLET">
				<HBox>
					<Ref to="label" id="labelid"/>
					<Ref to="button"/>
					<Label text="layout_tablet"/>
				</HBox>
			</Layout>
		
			<Layout sizeAtLeast="100.0cm">
				<HBox>
					<Ref to="label"/>
					<Label text="layout_100cm"/>
				</HBox>
			</Layout>						
		</layouts>
		
		<sceneStylesheets>
			<Stylesheet sizeAtLeast="3.0in" file="@phone.css"/>
			<Stylesheet sizeAtLeast="width:3.0in" file="@desktop.css"/>
			<Stylesheet sizeAtLeast="TABLET" file="@tablet.css"/>
		</sceneStylesheets>
		
		<myStylesheets>
			<Stylesheet sizeAtLeast="3.0in" file="@phone.css"/>
			<Stylesheet sizeAtLeast="width:3.0in" file="@desktop.css"/>
			<Stylesheet sizeAtLeast="TABLET" file="@tablet.css"/>
		</myStylesheets>
	</ResponsivePane>
 * --
 */
public class ResponsivePane extends StackPane {
	// TODO: should stylesheets also support orientation?
	// TODO: should we refactor to have a single 'SizeBasedResource' beneath both Layout and Stylesheet?
	
	
	// ==========================================================================================================================================================================================================================================
	// CONSTRUCTORS
	
	/**
	 * 
	 */
	public ResponsivePane() {
		
		// default device sizes
		setDeviceSize(Device.PHONE, Diagonal.inch(3.5));
		setDeviceSize(Device.TABLET, Diagonal.inch(7.0));
		setDeviceSize(Device.DESKTOP, Diagonal.inch(13.0));
		
		// react to changes in the available layouts and stylesheets
		layouts.addListener( (javafx.collections.ListChangeListener.Change<? extends Layout> c) -> {
			if (getTrace()) System.out.println(">>> requestLayout from changes in layouts, size=" + layouts.size());
			requestLayout();
		});
		sceneStylesheets.addListener( (javafx.collections.ListChangeListener.Change<? extends Stylesheet> c) -> {
			if (getTrace()) System.out.println(">>> requestLayout from changes in scene stylesheets, size=" + sceneStylesheets.size());
			requestLayout();
		});
		myStylesheets.addListener( (javafx.collections.ListChangeListener.Change<? extends Stylesheet> c) -> {
			if (getTrace()) System.out.println(">>> requestLayout from changes in my stylesheets, size=" + myStylesheets.size());
			requestLayout();
		});
	}
	
	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES

	/** Id */
	public ResponsivePane withId(String v) { 
		setId(v); 
		return this; 
	}

	/** Debug: show rendering hints (for Ref) and prints out changes to the layout on the console */
	public ObjectProperty<Boolean> debugProperty() { return debugProperty; }
	final private SimpleObjectProperty<Boolean> debugProperty = new SimpleObjectProperty<Boolean>(this, "debug", Boolean.FALSE);
	public Boolean getDebug() { return debugProperty.getValue(); }
	public void setDebug(Boolean value) { debugProperty.setValue(value); }
	public ResponsivePane withDebug(Boolean value) { setDebug(value); return this; } 

	/** Trace: like debug, plus show calculations determining if changes are needed on the console */
	public ObjectProperty<Boolean> traceProperty() { return traceProperty; }
	final private SimpleObjectProperty<Boolean> traceProperty = new SimpleObjectProperty<Boolean>(this, "trace", Boolean.FALSE);
	public Boolean getTrace() { return traceProperty.getValue(); }
	public void setTrace(Boolean value) { traceProperty.setValue(value); }
	public ResponsivePane withTrace(Boolean value) { setTrace(value); return this; } 

	// -----------------------------------------------------------------------------------------------
	// Reusable nodes
	
	/** refs */
	public ObservableList<Node> getReusableNodes() {
		return reusableNodes;
	}
	final private ObservableList<Node> reusableNodes = FXCollections.observableArrayList();
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @return
	 */
	public Node addReusableNode(String id, Node node) {
		node.setId(id);
		getReusableNodes().add(node);
		return node;
	}
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @return
	 */
	public Node addReusableNode(Node node) {
		if (node.getId() == null || node.getId().trim().length() == 0) {
			throw new IllegalArgumentException("A reusable node must have an id");
		}
		getReusableNodes().add(node);
		return node;
	}
	
	/**
	 * 
	 * @param refId
	 * @return
	 */
	Node findResuableNode(String refId) {
		for (Node lNode : reusableNodes) {
			if (refId.equals(lNode.getId())) {
				return lNode;
			}			
		}
		System.err.println("Could not find reference " + refId);
		return null;
	}
	
	// -----------------------------------------------------------------------------------------------
	// LAYOUTS

	/** layouts */
	public ObservableList<Layout> getLayouts() {
		return layouts;
	}
	final private ObservableList<Layout> layouts = FXCollections.observableArrayList();
	
	/**
	 * Convenience method for addLayout(Size, Node) 
	 */
	public void addLayout(Device device, Node root) {
		addLayout(device.toString(), root);
	}
	
	/**
	 * Convenience method for addLayout(Size, Node)
	 */
	public void addLayout(String device, Node root) {
		addLayout(deviceSizes.get(device), root);
	}
	
	/**
	 * Convenience method for getLayouts().add(new Layout(sizeAtLeast, root))
	 */
	public void addLayout(Size sizeAtLeast, Node root) {
		layouts.add(new Layout(sizeAtLeast, root));
	}
	
	
	/**
	 * Convenience method for addLayout(Size, Orientation, Node) 
	 */
	public void addLayout(Device device, Orientation orientation, Node root) {
		addLayout(device.toString(), orientation, root);
	}
	
	/**
	 * Convenience method for addLayout(Size, Orientation, Node)
	 */
	public void addLayout(String device, Orientation orientation, Node root) {
		addLayout(deviceSizes.get(device), orientation, root);
	}
	
	/**
	 * Convenience method for getLayouts().add(new Layout(sizeAtLeast, orientation, root))
	 */
	public void addLayout(Size sizeAtLeast, Orientation orientation, Node root) {
		layouts.add(new Layout(sizeAtLeast, orientation, root));
	}
	
	/** ActiveLayout */
	public ObjectProperty<Layout> activeLayoutProperty() { return activeLayoutProperty; }
	final private SimpleObjectProperty<Layout> activeLayoutProperty = new SimpleObjectProperty<Layout>(this, "activeLayout", null);
	public Layout getActiveLayout() { return activeLayoutProperty.getValue(); }
	public void setActiveLayout(Layout value) { activeLayoutProperty.setValue(value); }
	public ResponsivePane withActiveLayout(Layout value) { setActiveLayout(value); return this; } 

		
	// -----------------------------------------------------------------------------------------------
	// SceneStylesheets

	/** sceneStylesheets */
	public ObservableList<Stylesheet> getSceneStylesheets() {
		return sceneStylesheets;
	}
	final private ObservableList<Stylesheet> sceneStylesheets = FXCollections.observableArrayList();

	/**
	 * Convenience method for addSceneStylesheet(Size sizeAtLeast, String file)
	 */
	public void addSceneStylesheet(Device device, String file) {
		addSceneStylesheet(device.toString(), file);
	}
	
	/**
	 * Convenience method for addSceneStylesheet(Size sizeAtLeast, String file)
	 */
	public void addSceneStylesheet(String device, String file) {
		addSceneStylesheet(deviceSizes.get(device), file);
	}
	
	/**
	 * Convenience method for getSceneStylesheets().add(new Stylesheet(sizeAtLeast, file));
	 */
	public void addSceneStylesheet(Size sizeAtLeast, String file) {
		sceneStylesheets.add(new Stylesheet(sizeAtLeast, file));
	}
	
	/** ActiveSceneStylesheet */
	public ObjectProperty<Stylesheet> activeSceneStylesheetProperty() { return activeSceneStylesheetProperty; }
	final private SimpleObjectProperty<Stylesheet> activeSceneStylesheetProperty = new SimpleObjectProperty<Stylesheet>(this, "activeSceneStylesheet", null);
	public Stylesheet getActiveSceneStylesheet() { return activeSceneStylesheetProperty.getValue(); }
	public void setActiveSceneStylesheet(Stylesheet value) { activeSceneStylesheetProperty.setValue(value); }
	public ResponsivePane withActiveSceneStylesheet(Stylesheet value) { setActiveSceneStylesheet(value); return this; } 

	// -----------------------------------------------------------------------------------------------
	// MyStylesheets

	/** myStylesheets */
	public ObservableList<Stylesheet> getMyStylesheets() {
		return myStylesheets;
	}
	final private ObservableList<Stylesheet> myStylesheets = FXCollections.observableArrayList();

	/**
	 * Convenience method for addMyStylesheet(Size sizeAtLeast, String file)
	 */
	public void addMyStylesheet(Device device, String file) {
		addMyStylesheet(device.toString(), file);
	}
	
	/**
	 * Convenience method for addMyStylesheet(Size sizeAtLeast, String file)
	 */
	public void addMyStylesheet(String device, String file) {
		addMyStylesheet(deviceSizes.get(device), file);
	}
	
	/**
	 * Convenience method for getMyStylesheets().add(new Stylesheet(sizeAtLeast, file));
	 */
	public void addMyStylesheet(Size sizeAtLeast, String file) {
		myStylesheets.add(new Stylesheet(sizeAtLeast, file));
	}
	
	/** ActiveMyStylesheet */
	public ObjectProperty<Stylesheet> activeMyStylesheetProperty() { return activeMyStylesheetProperty; }
	final private SimpleObjectProperty<Stylesheet> activeMyStylesheetProperty = new SimpleObjectProperty<Stylesheet>(this, "activeMyStylesheet", null);
	public Stylesheet getActiveMyStylesheet() { return activeMyStylesheetProperty.getValue(); }
	public void setActiveMyStylesheet(Stylesheet value) { activeMyStylesheetProperty.setValue(value); }
	public ResponsivePane withActiveMyStylesheet(Stylesheet value) { setActiveMyStylesheet(value); return this; } 
	
	// ==========================================================================================================================================================================================================================================
	// DEVICE
	
	public final ObservableMap<String, Size> getDeviceSizes() {
		return deviceSizes;
	}
	final ObservableMap<String, Size> deviceSizes = FXCollections.observableMap(new TreeMap<String, Size>(String.CASE_INSENSITIVE_ORDER));
	
	/**
	 * Convenience method for deviceSizes.put(device.toString(), size)
	 */
	public void setDeviceSize(Device device, Size size) {
		setDeviceSize(device.toString(), size);
	}

	/**
	 * Convenience method for deviceSizes.put(device, size)
	 */
	public void setDeviceSize(String device, Size size) {
		deviceSizes.put(device, size);
	}

	/**
	 * Convienience method for getDeviceSize(device.toString())
	 */
	public Size getDeviceSize(Device device) {
		return getDeviceSize(device.toString());
	}

	/**
	 * Convienience method for getDeviceSize(device)
	 */
	public Size getDeviceSize(String device) {
		return deviceSizes.get(device);
	}

	// ==========================================================================================================================================================================================================================================
	// LAYOUT
	
    @Override 
    protected void layoutChildren() {
    	
   		setupLayout();
    	
    	// continue layout
    	super.layoutChildren();
    }
    
    /**
     * 
     */
	void setupLayout() {

		// recalculate ppi
		ppi = null;
		
    	// layout
		if (getTrace()) System.out.println("====================================================");
    	Layout lLayout = determineBestFittingLayout();
    	if (!lLayout.equals(getActiveLayout())) {
    		
    		if (getDebug() || getTrace()) System.out.println("Activating layout " + lLayout);
        	setActiveLayout(lLayout);

    		// switch to active layout
        	ResponsivePane.this.getChildren().clear();
        	ResponsivePane.this.getChildren().add(lLayout.getRoot());
    	}
    	
    	// scene stylesheet
		if (getTrace()) System.out.println("----------------------------------------------------");
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(sceneStylesheets);
	    	if (!lStylesheet.equals(getActiveSceneStylesheet())) {
	    		
	    		setActiveSceneStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, sceneStylesheets, getScene().getStylesheets());
	    	}
    	}
    	
    	// my stylesheet
		if (getTrace()) System.out.println("----------------------------------------------------");
    	{
	    	Stylesheet lStylesheet = determineBestFittingStylesheet(myStylesheets);
	    	if (!lStylesheet.equals(getActiveMyStylesheet())) {
	    		
	    		setActiveMyStylesheet(lStylesheet);
	
				// switch to active CSS file
				load(lStylesheet, myStylesheets, getStylesheets());
	    	}
    	}
		if (getTrace()) System.out.println("====================================================");
	}


	// ==========================================================================================================================================================================================================================================
	// SUPPORT

	/**
	 * Determine the pixels-per-inch of the screen we are on
	 * @return
	 */
	double determinePPI() {
		if (ppi != null) {
			return ppi;
		}
		
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		ppi = 100.0; // average dpi
		Window window = getScene().getWindow();
		ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight());
		if (screensForRectangle.size() > 0) {
			Screen lScreen = screensForRectangle.get(0); // we just use the first screen
			ppi = lScreen.getDpi();
			if (getTrace()) System.out.println("screens of scene: " + screensForRectangle + ", using the first then PPI=" + ppi); 
		}
		return ppi;		
	}
	Double ppi;
	
	/**
	 * 
	 * @return
	 */
	double determineActualDiagonalInInches() {
		Scene lScene = getScene();
		
		// determine the DPI factor, so the thresholds become larger on screens with a higher DPI
		double lPPI = determinePPI();
		double lWidthInInches = lScene.getWidth() / lPPI;
		double lHeightInInches = lScene.getHeight() / lPPI;
		double lDiagonalInInches = Math.sqrt( (lWidthInInches * lWidthInInches) + (lHeightInInches * lHeightInInches) );
		if (getTrace()) System.out.println("Actual scene size=" + lScene.getWidth() + "x" + lScene.getHeight() + "px, scene diagonal in inches=" + lDiagonalInInches + " (ppi=" + lPPI + ")");
		return lDiagonalInInches;
	}
	
	/**
	 * 
	 * @return
	 */
	Layout determineBestFittingLayout() {
		double actualDiagonalInInches = determineActualDiagonalInInches();
		Scene lScene = getScene();
		Orientation lSceneOrientation = (lScene.getWidth() > lScene.getHeight() ? Orientation.LANDSCAPE : Orientation.PORTRAIT);

		// find the best fitting layout
		Layout lBestFittingLayout = null;
		for (Layout lLayout : layouts) {
			if (getTrace()) System.out.println("determineBestFittingLayout, examining layout: " + lLayout.describeSizeConstraints());
			double lLayoutSizeAtLeast = lLayout.getSizeAtLeast().toInches(this);
			if (actualDiagonalInInches >= lLayoutSizeAtLeast) {
				if (getTrace()) System.out.println("determineBestFittingLayout, layout " + lLayout.describeSizeConstraints() + " is candidate based on scene diagonal: " + actualDiagonalInInches + "in");
				
				double lBestFittingLayoutSizeAtLeast = (lBestFittingLayout == null ? -1.0 : lBestFittingLayout.getSizeAtLeast().toInches(this));
				if (getTrace()) System.out.println("determineBestFittingLayout, comparing best fitting layout " + (lBestFittingLayout == null ? "null" : lBestFittingLayout.describeSizeConstraints()) + " (" + lBestFittingLayoutSizeAtLeast + ") with " + lLayout.describeSizeConstraints() + " (" + lLayoutSizeAtLeast + ")");
				if (lBestFittingLayout == null || lBestFittingLayoutSizeAtLeast <= lLayoutSizeAtLeast) {
					if (getTrace()) System.out.println("determineBestFittingLayout, layout " + lLayout.describeSizeConstraints() + " is a better or equal candidate based on diagonal vs best fitting so far: " + (lBestFittingLayout == null ? "null" : lBestFittingLayout.describeSizeConstraints()));
					
					// Based on the width, this layout is a candidate. But is it also based on the orientation?
					Orientation lBestFittingLayoutOrientation = (lBestFittingLayout == null ? null : lBestFittingLayout.getOrientation()); 
					Orientation lLayoutOrientation = lLayout.getOrientation(); 
					if ( lBestFittingLayout == null || lLayoutOrientation == null || lSceneOrientation.equals(lLayoutOrientation)) {
						if (getTrace()) System.out.println("determineBestFittingLayout, layout " + lLayout.describeSizeConstraints() + " is also candidate based on orientation");
						
						// Orientation also matches, do we prefer the one orientation of the other?
						if ( (lBestFittingLayoutOrientation == null && lLayoutOrientation == null) 
						  || (lBestFittingLayoutOrientation == null && lLayoutOrientation != null) // Prefer a layout with orientation above one without
						   ) {
							if (getTrace()) System.out.println("determineBestFittingLayout, layout " + lLayout.describeSizeConstraints() + " is also a better candidate based on orientation vs best fitting so far: " + (lBestFittingLayout == null ? "null" : lBestFittingLayout.describeSizeConstraints()));
							lBestFittingLayout = lLayout;
							if (getTrace()) System.out.println("determineBestFittingLayout, best fitting so far: " + lBestFittingLayout.describeSizeConstraints());
						}
					}
				}
			}
		}
		
		// if none found, use the default layout
		if (lBestFittingLayout == null) {
			lBestFittingLayout = SINGULARITY_LAYOUT;
		}
		
		// done
		if (getTrace()) System.out.println("determineBestFittingLayout=" + lBestFittingLayout.describeSizeConstraints());
		return lBestFittingLayout;
	}
	private final Layout SINGULARITY_LAYOUT = new Layout(Size.ZERO, new Label("?") );
	
	/**
	 * 
	 * @return
	 */
	Stylesheet determineBestFittingStylesheet(List<Stylesheet> availableStylesheets) {
		double actualSizeInInches = determineActualDiagonalInInches();
		
		// find the best fitting stylesheet
		Stylesheet lBestFittingStylesheet = null;
		for (Stylesheet lStylesheet : availableStylesheets) {
			if (actualSizeInInches >= lStylesheet.getSizeAtLeast().toInches(this)) {
				if (lBestFittingStylesheet == null || lBestFittingStylesheet.getSizeAtLeast().toInches(ResponsivePane.this) < lStylesheet.getSizeAtLeast().toInches(ResponsivePane.this)) {
					lBestFittingStylesheet = lStylesheet;
				}
			}
		}
		
		// if none found, use the default Stylesheet
		if (lBestFittingStylesheet == null) {
			lBestFittingStylesheet = SINGULAR_STYLESHEET;
		}
		
		// done
		if (getTrace()) System.out.println("determineBestFittingStylesheet=" + lBestFittingStylesheet.describeSizeConstraints() + " -> " + lBestFittingStylesheet.getFile());
		return lBestFittingStylesheet;
	}
	final private Stylesheet SINGULAR_STYLESHEET = new Stylesheet(Size.ZERO, null);
	
	/**
	 * 
	 * @param stylesheet
	 */	
	void load(Stylesheet stylesheet, List<Stylesheet> availableStylesheets, List<String> activeStylesheetUrls) {
		String lStylesheetFile = stylesheet.getFile();
		
		// remove all of the available stylesheets
		for (Stylesheet lStylesheet : availableStylesheets) {
			String lActiveStylesheetUrl = lStylesheet.getFile();
			if (activeStylesheetUrls.remove(lActiveStylesheetUrl)) {
				if (getDebug() || getTrace()) System.out.println("Removed stylesheet " + lStylesheet.getSizeAtLeast() + " -> " + lActiveStylesheetUrl);
			}			
		}
		
		// load new
		if (lStylesheetFile != null) {
			if (getDebug() || getTrace()) System.out.println("Loading stylesheet " + stylesheet.getSizeAtLeast() + " -> " + lStylesheetFile);
			activeStylesheetUrls.add(lStylesheetFile);
		}
	}


}
