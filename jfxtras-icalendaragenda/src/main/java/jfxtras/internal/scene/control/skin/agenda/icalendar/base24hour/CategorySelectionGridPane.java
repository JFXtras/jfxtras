/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
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
