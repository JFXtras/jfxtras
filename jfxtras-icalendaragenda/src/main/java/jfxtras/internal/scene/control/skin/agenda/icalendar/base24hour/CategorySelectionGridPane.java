package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/** makes a group of colored squares used to select appointment group */
/**
 * A 12 x 2 {@link GridPane} of 24 colored rectangles representing categories.  When a rectangle
 * is selected it is marked by a check icon.
 * 
 * @author David Bal
 *
 */
public class CategorySelectionGridPane extends GridPane
{
    private Pane[] icons;
    final private ImageView checkIcon = new ImageView();
    
    /** Index of selected String */
    public IntegerProperty categorySelectedProperty() { return categorySelected; }
    private IntegerProperty categorySelected = new SimpleIntegerProperty(-1);
    public void setCategorySelected(Integer i) { categorySelected.set(i); }
    public Integer getCategorySelected() { return categorySelected.getValue(); }   
     
    /*
     * Constructor
     */
    public CategorySelectionGridPane()
    {
        checkIcon.getStyleClass().add("check-icon");
    }
    
    /**
     * Provide necessary data to initialize
     * 
     * @param initialCategory - string name of initial category
     * @param categories - list of all category names
     */
    public void setupData(String initialCategory, List<String> categories)
    {
         setHgap(3);
         setVgap(3);
         icons = new Pane[categories.size()];
         
         List<Integer> categoriesIndices = IntStream
                 .range(0, categories.size())
                 .mapToObj(i -> new Integer(i))
                 .collect(Collectors.toList());
         for (Integer lCnt : categoriesIndices)
         {
             Pane icon = new Pane();
             icon.setPrefSize(24, 24);
             Rectangle rectangle = new Rectangle(24, 24);
             rectangle.setArcWidth(6);
             rectangle.setArcHeight(6);
             rectangle.getStyleClass().add("group" + lCnt);
             icon.getChildren().add(rectangle);
             icons[lCnt] = icon;
             this.add(icons[lCnt], lCnt % 12, lCnt / 12 );
    
             // tooltip
             updateToolTip(lCnt, categories.get(lCnt));
    
             // mouse 
             setupMouseOverAsBusy(icons[lCnt]);
             icons[lCnt].setOnMouseClicked( (mouseEvent) ->
             {
                 mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
                 categorySelected.set(lCnt);
             });
         }
    
         int index = categories.indexOf(initialCategory);
         if (index >= 0)
         {
             setCategorySelected(index);
             setLPane(index);
         }
         
         // change listener - fires when new icon is selected
         categorySelectedProperty().addListener((observable, oldSelection, newSelection) ->
         {
           int oldS = (int) oldSelection;
           int newS = (int) newSelection;
           setLPane(newS);
           unsetLPane(oldS);
         });
     }
    
     // blue border in selection
     private void unsetLPane(int i)
     {
         if (i >= 0)
         {
             icons[i].getChildren().remove(checkIcon);
         }
     }
     private void setLPane(int i)
     {
         icons[i].getChildren().add(checkIcon);
     }
     
     /** Update ToolTip when category name changes
      * 
      * @param i - index number of category in grid
      * @param category - category name
      */
     public void updateToolTip(int i, String category)
     {
         if (category != null)
         {
             Tooltip.install(icons[i], new Tooltip(category));
         } 
     }
    
     private void setupMouseOverAsBusy(final Node node)
     {
         // play with the mouse pointer to show something can be done here
         node.setOnMouseEntered( (mouseEvent) -> {
             if (!mouseEvent.isPrimaryButtonDown()) {                        
                 node.setCursor(Cursor.HAND);
                 mouseEvent.consume();
             }
         });
         node.setOnMouseExited( (mouseEvent) -> {
             if (!mouseEvent.isPrimaryButtonDown()) {
                 node.setCursor(Cursor.DEFAULT);
                 mouseEvent.consume();
             }
         });
     }
}
