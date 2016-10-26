package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.io.IOException;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.DeleteChoiceDialog;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.SimpleDeleterFactory;

/** 
 * Base TabPane that contains two tabs for editing descriptive properties and for editing a {@link RecurrenceRule}.
 * The first tab contains a {@link EditDescriptiveVBox }.  The second contains a {@link EditRecurrenceRuleVBox}.
 * 
 * @author David Bal
 * 
 * @param <T> subclass of {@link VDisplayable}
 * @param <U> subclass of {@link EditDescriptiveVBox} associated with the subclass of {@link VDisplayable}
 */
public abstract class EditDisplayableTabPane<T extends VDisplayable<T>, U extends EditDescriptiveVBox<T>> extends TabPane
{
    U editDescriptiveVBox;
    EditRecurrenceRuleVBox<T> recurrenceRuleVBox;

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML AnchorPane descriptiveAnchorPane;
    @FXML AnchorPane recurrenceRuleAnchorPane;
    @FXML private TabPane editDisplayableTabPane;
    @FXML private Tab descriptiveTab;
    @FXML private Tab recurrenceRuleTab;
    
    @FXML private Button cancelComponentButton;
    @FXML private Button saveComponentButton;
    @FXML private Button deleteComponentButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button saveRepeatButton;

//    ObjectProperty<Boolean> isFinished = new SimpleObjectProperty<>(false);
//    /** When property value becomes true the control should be closed
//     * (i.e. Attach a listener to this property, on changing hide the control */
//    public ObjectProperty<Boolean> isFinished() { return isFinished; }

    ObjectProperty<List<VCalendar>> iTIPMessages = new SimpleObjectProperty<>();
    public ObjectProperty<List<VCalendar>> iTIPMessagesProperty() { return iTIPMessages; }
//    @Deprecated
//    ObjectProperty<List<T>> newVComponents = new SimpleObjectProperty<>();
    /** This property contains a List of new components resulting from the editing.
     * When property value becomes non-null the control should be closed.
     * (i.e. Attach a listener to this property, on changing hide the control */
//    @Deprecated
//    public ObjectProperty<List<T>> newVComponentsProperty() { return newVComponents; }
    
    public EditDisplayableTabPane( )
    {
        super();
        loadFxml(EditDescriptiveVBox.class.getResource("EditDisplayable.fxml"), this);
    }
    
    @FXML
    void handleSaveButton()
    {
        removeEmptyProperties();
    }

    void removeEmptyProperties()
    {
        if (vComponentCopy.getRecurrenceRule() != null)
        {
            if (recurrenceRuleVBox.frequencyComboBox.getValue() == FrequencyType.WEEKLY && recurrenceRuleVBox.dayOfWeekList.isEmpty())
            {
                canNotHaveZeroDaysOfWeek();
            } else if (! vComponentCopy.getRecurrenceRule().isValid())
            {
                throw new RuntimeException("Unhandled component error" + System.lineSeparator() + vComponentCopy.errors());
            }
        }
        
        if (editDescriptiveVBox.summaryTextField.getText().isEmpty())
        {
            vComponentCopy.setSummary((Summary) null); 
        }
        
        if (editDescriptiveVBox.categoryTextField.getText().isEmpty())
        {
            vComponentCopy.setCategories(null); 
        }

       // nullify Interval if value equals default (avoid unnecessary content output)
        if ((vComponentCopy.getRecurrenceRule() != null) && (recurrenceRuleVBox.intervalSpinner.getValue() == Interval.DEFAULT_INTERVAL))
        {
            vComponentCopy.getRecurrenceRule().getValue().setInterval((Interval) null); 
        }
    }
    
    @FXML private void handleCancelButton()
    {
        iTIPMessagesProperty().set(Collections.emptyList());
    }
    
    @FXML private void handleDeleteButton()
    {
        removeEmptyProperties();
        Object[] params = new Object[] {
                DeleteChoiceDialog.DELETE_DIALOG_CALLBACK,
                editDescriptiveVBox.startOriginalRecurrence
//                vComponents
        };
        List<VCalendar> result = SimpleDeleterFactory.newDeleter(vComponentCopy, params).delete();
        iTIPMessagesProperty().set(result);

        
//        T result = (T) SimpleDeleterFactory.newDeleter(vComponent, params).delete();
//        newVComponentsProperty().set(Arrays.asList(result)); // indicates control should be hidden
//        isFinished.set(result);
    }
    
    @FXML private void handlePressEnter(KeyEvent e)
    {
        if (e.getCode().equals(KeyCode.ENTER))
        {
            handleSaveButton();
        }
    }
    
    T vComponentCopy;
    T vComponentOriginal;
    public static VComponent vo;
//    List<T> vComponents;

    /**
     * Provide necessary data to setup
     * 
     * @param vComponentCopy - component to be edited
     * @param startRecurrence - start of selected recurrence
     * @param endRecurrence - end of selected recurrence
     * @param categories - list of category names
     */
    public void setupData(
            T vComponentCopy,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories
            )
    {
        this.vComponentCopy = vComponentCopy;
        vo = vComponentOriginal;
        editDescriptiveVBox.setupData(vComponentCopy, startRecurrence, endRecurrence, categories);
        
        /* 
         * Shut off repeat tab if vComponent is not a parent
         * Components with RECURRENCE-ID can't add repeat rules (only parent can have repeat rules)
         */
        if (vComponentCopy.getRecurrenceId() != null)
        {
            recurrenceRuleTab.setDisable(true);
            recurrenceRuleTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        recurrenceRuleVBox.setupData(vComponentCopy, editDescriptiveVBox.startRecurrenceProperty);
    }
    
    // Displays an alert notifying at least one day of week must be present for weekly frequency
    private static void canNotHaveZeroDaysOfWeek()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Please select at least one day of the week.");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("zero_day_of_week_alert");
        alert.getDialogPane().lookupButton(buttonTypeOk).setId("zero_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
    
    protected static void loadFxml(URL fxmlFile, Object rootController)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(Settings.resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
 
