package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.time.temporal.Temporal;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;


/**
 * Dialog that can be either an edit or a delete choice dialog.
 * Choice options include ONE, THIS_AND_FUTURE and ALL
 * 
 * @author David Bal
 *
 */
public class ComponentChangeDialog extends Dialog<ChangeDialogOption>
{
    ComboBox<ChangeDialogOption> comboBox = new ComboBox<>();
    /**
     * 
     * @param choiceMap - map of ChangeDialogOption and StartEndRange pairs representing the choices available
     * @param resources
     */
    public ComponentChangeDialog(Map<ChangeDialogOption, Pair<Temporal,Temporal>> choiceMap, ResourceBundle resources)
    {
        Settings.REPEAT_CHANGE_CHOICES.get(this);
        ChangeDialogOption initialSelection = choiceMap.entrySet().iterator().next().getKey();
//        if (! choiceMap.containsKey(initialSelection)) throw new RuntimeException("choicesAndDateRanges must contain: ChangeDialogOption." + initialSelection);
        getDialogPane().getStyleClass().add("choice-dialog");

        // Buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).setId("changeDialogOkButton");
        getDialogPane().lookupButton(ButtonType.CANCEL).setId("changeDialogCancelButton");
        
        // new Label bound to Dialog's contentTextProperty 
        Label label = new Label();
        setContentText(resources.getString("dialog.content"));
        label.textProperty().bind(getDialogPane().contentTextProperty());
        
        // Choices
//        ComboBox<ChangeDialogOption> comboBox = new ComboBox<>();
        comboBox.setId("changeDialogComboBox");
        comboBox.getItems().addAll(choiceMap.keySet());
        comboBox.getSelectionModel().select(initialSelection);
        
        comboBox.setConverter(new StringConverter<ChangeDialogOption>()
        {
            @Override public String toString(ChangeDialogOption selection)
            {
                return Settings.REPEAT_CHANGE_CHOICES.get(selection);
            }
            @Override public ChangeDialogOption fromString(String string)
            {
                throw new RuntimeException("not required for non editable ComboBox");
            }
        });

        // grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.add(comboBox, 1, 0);
        grid.add(label, 0, 0);
        getDialogPane().setContent(grid);
        
        // Match header with range string
        String range = AgendaDateTimeUtilities.formatRange(choiceMap.get(initialSelection));
        setHeaderText(Settings.REPEAT_CHANGE_CHOICES.get(initialSelection) + ":" + System.lineSeparator() + range); // initial header text
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
        {
            String range2 = AgendaDateTimeUtilities.formatRange(choiceMap.get(newSelection));
            setHeaderText(Settings.REPEAT_CHANGE_CHOICES.get(newSelection) + ":" + System.lineSeparator() + range2);
        });
        
        setResultConverter((dialogButton) ->
        {
            ButtonData data = (dialogButton == null) ? null : dialogButton.getButtonData();
            return data == ButtonData.OK_DONE ? comboBox.getSelectionModel().getSelectedItem() : null;
        });
    }
}
