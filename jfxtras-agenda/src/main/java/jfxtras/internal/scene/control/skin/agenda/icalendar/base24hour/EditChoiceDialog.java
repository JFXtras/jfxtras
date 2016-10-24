package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;


/**
 * Dialog to allow user to choice between ChangeDialogOption (ONE, THIS_AND_FUTURE, and ALL) edit options.
 * The constructor requires a map of matching ChangeDialogOptions and a string of date/time range affected.
 * 
 * @author David Bal
 *
 */
public class EditChoiceDialog extends ComponentChangeDialog
{
    /**
     * Callback to produce an edit choice dialog based on the options in the input argument choices.
     * Usually all or some of ONE, THIS_AND_FUTURE, and ALL.
     */
    final static public Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> EDIT_DIALOG_CALLBACK = (choices) ->
    {
        EditChoiceDialog dialog = new EditChoiceDialog(choices, Settings.resources);                
        Optional<ChangeDialogOption> result = dialog.showAndWait();
        return (result.isPresent()) ? result.get() : ChangeDialogOption.CANCEL;
    };
    /**
     * 
     * @param choiceList list of ChangeDialogOption representing the date/time range to be affected
     * @param resources ResourceBundle for internationalization
     */
    public EditChoiceDialog(Map<ChangeDialogOption, Pair<Temporal,Temporal>> choiceList, ResourceBundle resources)
    {
        super(choiceList, resources);
        getDialogPane().setId("editChoiceDialog");
        setTitle(resources.getString("dialog.edit.title"));
    }
}
