package jfxtras.scene.control.agenda.icalendar.trial;

import org.junit.Test;

import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;
import jfxtras.test.TestUtil;

public class DemoTest extends AgendaTestAbstract
{
    @Test
    public void canMakeGraphicalDemo() // Can't figure out how to easily make video from test so not useful
    {
        // Add VComponents, listener in ICalendarAgenda makes Appointments
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1()
                    .withSummary("Example"));
            
            // set scrollbar into middle
            for (Node node: agenda.lookupAll(".scroll-bar"))
            {
                final ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation() == Orientation.VERTICAL)
                {
                    bar.setValue(.4);
                }
            }
            
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            Stage primaryStage = (Stage) vbox.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2.7);
        });

        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine13");
        release(MouseButton.PRIMARY);

        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.THIS_AND_FUTURE);
        });
        click("#changeDialogOkButton");
        
        move("#AppointmentRegularBodyPane2015-11-13/0 .DurationDragger"); 
        press(MouseButton.PRIMARY);
        moveBy(0, 30);
        release(MouseButton.PRIMARY);
        click("#changeDialogOkButton");
    }
}
