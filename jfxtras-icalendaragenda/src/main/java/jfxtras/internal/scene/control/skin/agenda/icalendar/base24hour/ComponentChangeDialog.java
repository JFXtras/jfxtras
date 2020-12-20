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
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
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
    public ComponentChangeDialog(
    		Map<ChangeDialogOption,
    		Pair<Temporal,Temporal>> choiceMap,
    		ResourceBundle resources)
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
