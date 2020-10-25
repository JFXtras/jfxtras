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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;


/**
 * Dialog that adds a CheckBox for selecting if special recurrences (VComponents with RECURRENCEID) should
 * be changed (when selected) or ignored (when not selected).
 * 
 * @author David Bal
 *
 */
public class EditWithRecurrencesChoiceDialog extends EditChoiceDialog
{
    /**
     * Callback to produce an edit choice dialog based on the options in the input argument choices.
     * Usually all or some of ONE, THIS_AND_FUTURE, and ALL.
     */
    final static public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> EDIT_DIALOG_CALLBACK = (choices) ->
    {
        EditWithRecurrencesChoiceDialog dialog = new EditWithRecurrencesChoiceDialog(choices, Settings.resources);                
        Optional<ChangeDialogOption> result = dialog.showAndWait();
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    };
    
    /**
     * 
     * @param choiceList list of ChangeDialogOption representing the date/time range to be affected
     * @param resources ResourceBundle for internationalization
     */
    public EditWithRecurrencesChoiceDialog(Map<ChangeDialogOption, Pair<Temporal,Temporal>> choiceList, ResourceBundle resources)
    {
        super(choiceList, resources);
        getDialogPane().setId("editWithRecurrencesChoiceDialog");
        setTitle(resources.getString("dialog.edit.title"));

        CheckBox includeRecurrenceCheckBox = new CheckBox("include special recurrences");
        includeRecurrenceCheckBox.setSelected(true);
        handleRecurrenceCheckBoxVisibility(includeRecurrenceCheckBox, comboBox.getValue());
        includeRecurrenceCheckBox.setVisible(false);
        GridPane g = (GridPane) getDialogPane().getContent();
        g.add(includeRecurrenceCheckBox, 0, 1);
        comboBox.valueProperty().addListener((obs, oldValue, newValue) -> handleRecurrenceCheckBoxVisibility(includeRecurrenceCheckBox, newValue));

        setResultConverter((dialogButton) ->
        {
            ButtonData data = (dialogButton == null) ? null : dialogButton.getButtonData();
            final ChangeDialogOption selectedItem;
            switch (comboBox.getSelectionModel().getSelectedItem())
            {
            case ALL:
                selectedItem = (includeRecurrenceCheckBox.isSelected()) ? ChangeDialogOption.ALL : ChangeDialogOption.ALL_IGNORE_RECURRENCES;
                break;
            case THIS_AND_FUTURE:
                selectedItem = (includeRecurrenceCheckBox.isSelected()) ? ChangeDialogOption.THIS_AND_FUTURE : ChangeDialogOption.THIS_AND_FUTURE_IGNORE_RECURRENCES;
                break;
            default:
                selectedItem = comboBox.getSelectionModel().getSelectedItem();
                break;
            };
            return data == ButtonData.OK_DONE ? selectedItem : null;
        });
    }

    private void handleRecurrenceCheckBoxVisibility(CheckBox includeRecurrenceCheckBox, ChangeDialogOption newValue)
    {
        switch (newValue)
        {
        case ALL:
        case THIS_AND_FUTURE:
            includeRecurrenceCheckBox.setVisible(true);
            break;
        case ONE:
            includeRecurrenceCheckBox.setVisible(false);
            break;
        default:
            break;
        }
    }
}
