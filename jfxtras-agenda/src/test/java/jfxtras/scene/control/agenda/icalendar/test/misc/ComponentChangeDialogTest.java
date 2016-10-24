package jfxtras.scene.control.agenda.icalendar.test.misc;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Pair;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.EditChoiceDialog;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class ComponentChangeDialogTest extends JFXtrasGuiTest
{
//    private ComponentChangeDialog dialog;

    private ResourceBundle resources;
    private static final Map<ChangeDialogOption, Pair<Temporal,Temporal>> EXAMPLE_MAP = makeExampleMap();
    private static Map<ChangeDialogOption, Pair<Temporal,Temporal>> makeExampleMap()
    {
        Map<ChangeDialogOption, Pair<Temporal,Temporal>> exampleMap = new LinkedHashMap<>();
        exampleMap.put(ChangeDialogOption.ALL, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), null));
        exampleMap.put(ChangeDialogOption.ONE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), LocalDate.of(2016, 5, 25)));
        exampleMap.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 6, 25), null));
        return exampleMap;
    }
    
    @Override
    public Parent getRootNode()
    {
        // TODO - PUT BUNDLE NAME IN ONE PLACE SO IT CAN BE EASILY CHANGED - ITS EVERYWHERE
        resources = ResourceBundle.getBundle("jfxtras.ICalendarAgenda", Locale.getDefault());
        return new Label();
    }

    @Test
    public void canDisplayDialog()
    {
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
            EditChoiceDialog dialog = new EditChoiceDialog(EXAMPLE_MAP, resources);
            dialog.show();
        });
        Node n = find("#editChoiceDialog");
//        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 345.0, 166.0, 0.01);
        click("#changeDialogCancelButton");
    }

}
