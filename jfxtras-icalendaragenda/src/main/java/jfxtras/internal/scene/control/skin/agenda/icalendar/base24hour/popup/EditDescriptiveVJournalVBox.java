package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.icalendarfx.components.VJournal;

/**
 *  Controller for editing descriptive properties in a {@link VJournal}
 * 
 * @author David Bal
 */
public class EditDescriptiveVJournalVBox extends EditDescriptiveVBox<VJournal>
{
    public EditDescriptiveVJournalVBox()
    {
        super();
        // remove unavailable elements
        endLabel.setVisible(false);
        endLabel = null;
        locationLabel.setVisible(false);
        locationLabel = null;
        locationTextField.setVisible(false);
        locationTextField = null;
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VJournal vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<String> categories)
    {
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);

        // Journal supports multiple descriptions, but this control only supports one description
        if (vComponent.getDescriptions() == null)
        {
            vComponent.withDescriptions("");
        }
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescriptions().get(0).valueProperty());
    }
}
