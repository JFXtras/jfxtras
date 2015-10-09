/**
 * AgendaWeekSkin.java
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

package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.css.converters.IntegerConverter;
import jfxtras.internal.scene.control.skin.agenda.base24hour.AgendaSkinTimeScale24HourAbstract;
import jfxtras.scene.control.agenda.Agenda;

/**
 * @author Tom Eugelink
 */
public class AgendaDaysFromDisplayedSkin extends AgendaSkinTimeScale24HourAbstract<AgendaDaysFromDisplayedSkin> {
	
	/**
	 * 
	 */
	public AgendaDaysFromDisplayedSkin(Agenda control) {
		super(control);
		
		construct();
	}
	
	/**
	 * 
	 */
	private void construct() {
		// days back
		daysBeforeSlider = new Slider(-20.0, 0.0, (double)daysBackDefault); 
		daysBeforeSlider.setId("daysBeforeSlider");
		daysBeforeSlider.minProperty().bind(daysBeforeFurthestProperty);
		daysBeforeSlider.snapToTicksProperty().set(true);
		daysBeforeSlider.majorTickUnitProperty().set(1.0);
		daysBeforeSlider.minorTickCountProperty().set(0);
		daysBeforeSlider.showTickLabelsProperty().set(true);
		daysBeforeSlider.prefWidthProperty().bind(borderPane.widthProperty().divide(2.0));
// Until JDK-8133008 is fixed we use the valueChangingProperty and Math.round:		
//		daysBeforeSlider.valueProperty().addListener( (observable) -> {
//			System.out.println("back " + daysBeforeSlider.getValue());
//			if (daysBeforeSlider.valueChangingProperty().get() == false) {
//				System.out.println("back reconstruct"); // TBEERNOT: min and max slider positions are not processed
//				reconstruct();
//			}
//		});
		daysBeforeSlider.valueChangingProperty().addListener( (observable) -> {
			if (!daysBeforeSlider.valueChangingProperty().get()) {
				reconstruct();
			}
		});

		
		// days forward
		daysAfterSlider = new Slider(0.0, 20.0, (double)daysForwardDefault); 
		daysAfterSlider.setId("daysAfterSlider");
		daysAfterSlider.maxProperty().bind(daysAfterFurthestProperty);
		daysAfterSlider.snapToTicksProperty().set(true);
		daysAfterSlider.majorTickUnitProperty().set(1.0);
		daysAfterSlider.minorTickCountProperty().set(0);
		daysAfterSlider.showTickLabelsProperty().set(true);
		daysAfterSlider.prefWidthProperty().bind(borderPane.widthProperty().divide(2.0));
// Until JDK-8133008 is fixed we use the valueChangingProperty and Math.round:
//		daysAfterSlider.valueProperty().addListener( (observable) -> {
//			System.out.println("forward " + daysAfterSlider.getValue());
//			if (daysAfterSlider.valueChangingProperty().get() == false) {
//				System.out.println("forward reconstruct"); // TBEERNOT: min and max slider positions are not processed
//				reconstruct();
//			}
//		});
		daysAfterSlider.valueChangingProperty().addListener( (observable) -> {
			if (!daysAfterSlider.valueChangingProperty().get()) {
				reconstruct();
			}
		});
		
		// put the sliders at the bottom
		borderPane.setBottom(new HBox(daysBeforeSlider, daysAfterSlider));
	}
	private Slider daysBeforeSlider;
	private Slider daysAfterSlider;
	final private int daysBackDefault = -1;
	final private int daysForwardDefault = 6;
	
	/**
	 * 
	 */
	protected void reconstruct() {
		super.reconstruct();
		
		// put the sliders at the bottom
		borderPane.setBottom(new HBox(daysBeforeSlider, daysAfterSlider));
	}

	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 */
	protected List<LocalDate> determineDisplayedLocalDates()
	{
		// get slider positions
		int lStartOffset = (daysBeforeSlider == null ? daysBackDefault : (int)Math.round(daysBeforeSlider.valueProperty().get())); // Until JDK-8133008 is fixed we use the valueChangingProperty and Math.round: 
		int lEndOffset = (daysAfterSlider == null ? daysForwardDefault : (int)Math.round(daysAfterSlider.valueProperty().get())); // Until JDK-8133008 is fixed we use the valueChangingProperty and Math.round:
		LocalDate lStartLocalDate = getSkinnable().getDisplayedLocalDateTime().toLocalDate();
		
		// determine displayed calendars
		String lKey = lStartOffset + " / "  + lEndOffset + " / " + lStartLocalDate;
		if (!lKey.equals(displayedLocalDatesKey)) {
			
			// determine displayed calendars
			displayedLocalDates = new ArrayList<>();
			for (int i = lStartOffset; i < lEndOffset + 1; i++) { // + 1 = always show today
				displayedLocalDates.add(lStartLocalDate.plusDays(i));
			}
			displayedLocalDatesKey = lKey;
		}
		
		// done
		return displayedLocalDates;
	}
	private String displayedLocalDatesKey = "";
	private List<LocalDate> displayedLocalDates;
	
	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * daysBeforeFurthestProperty
     */
    public final ObjectProperty<Integer> daysBeforeFurthestProperty() { return daysBeforeFurthestProperty; }
    private ObjectProperty<Integer> daysBeforeFurthestProperty = new SimpleStyleableObjectProperty<Integer>(StyleableProperties.DAYS_BEFORE_FURTHEST_CSSMETADATA, StyleableProperties.DAYS_BEFORE_FURTHEST_CSSMETADATA.getInitialValue(null)) {
//		{ // anonymous constructor
//			addListener( (invalidationEvent) -> {
//				if (daysBeforeFurthestProperty.get() > 0) {
//					daysBeforeFurthestProperty.set(0);
//				}
//			});
//		}
	};
    public final void setDaysBeforeFurthest(int value) { daysBeforeFurthestProperty.set(value); }
    public final int getDaysBeforeFurthest() { return daysBeforeFurthestProperty.get(); }
    public final AgendaDaysFromDisplayedSkin withDaysBeforeFurthest(int value) { setDaysBeforeFurthest(value); return this; }
    
    /**
     * daysAfterFurthestProperty
     */
    public final ObjectProperty<Integer> daysAfterFurthestProperty() { return daysAfterFurthestProperty; }
    private ObjectProperty<Integer> daysAfterFurthestProperty = new SimpleStyleableObjectProperty<Integer>(StyleableProperties.DAYS_AFTER_FURTHEST_CSSMETADATA, StyleableProperties.DAYS_AFTER_FURTHEST_CSSMETADATA.getInitialValue(null)) {
//		{ // anonymous constructor
//			addListener( (invalidationEvent) -> {
//				if (daysAfterFurthestProperty.get() < 0) {
//					daysAfterFurthestProperty.set(0);
//				}
//			});
//		}
	};
    public final void setDaysAfterFurthest(int value) { daysAfterFurthestProperty.set(value); }
    public final int getDaysAfterFurthest() { return daysAfterFurthestProperty.get(); }
    public final AgendaDaysFromDisplayedSkin withDaysAfterFurthest(int value) { setDaysAfterFurthest(value); return this; }
    

    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<Agenda, Integer> DAYS_BEFORE_FURTHEST_CSSMETADATA = new CssMetaDataForSkinProperty<Agenda, AgendaDaysFromDisplayedSkin, Integer>("-fxx-days-before-furthest", IntegerConverter.getInstance(), -9 ) {
        	@Override 
        	protected ObjectProperty<Integer> getProperty(AgendaDaysFromDisplayedSkin s) {
            	return s.daysBeforeFurthestProperty;
            }
        };
        
        private static final CssMetaData<Agenda, Integer> DAYS_AFTER_FURTHEST_CSSMETADATA = new CssMetaDataForSkinProperty<Agenda, AgendaDaysFromDisplayedSkin, Integer>("-fxx-days-after-furthest", IntegerConverter.getInstance(), 9 ) {
        	@Override 
        	protected ObjectProperty<Integer> getProperty(AgendaDaysFromDisplayedSkin s) {
            	return s.daysAfterFurthestProperty;
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> classCssMetaData = new ArrayList<CssMetaData<? extends Styleable, ?>>(AgendaSkinTimeScale24HourAbstract.getClassCssMetaData());
        	classCssMetaData.add(DAYS_BEFORE_FURTHEST_CSSMETADATA);
        	classCssMetaData.add(DAYS_AFTER_FURTHEST_CSSMETADATA);
            STYLEABLES = Collections.unmodifiableList(classCssMetaData);                
        }
    }
    
    /** 
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */    
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * This method should delegate to {@link Node#getClassCssMetaData()} so that
     * a Node's CssMetaData can be accessed without the need for reflection.
     * @return The CssMetaData associated with this node, which may include the
     * CssMetaData of its super classes.
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
        
}
