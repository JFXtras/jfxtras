package jfxtras.scene.control.test;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.layout.VBox;
import jfxtras.test.JFXtrasGuiTest;
import org.junit.Assert;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;

import java.util.List;

/**
 * Created by bblonski on 9/12/14.
 */
public class CalendarTextFieldExtendedTest extends JFXtrasGuiTest {

    private static Pane root;
    final private List<String> expected = FXCollections.observableArrayList("CalendarTextField");
    private volatile boolean pass = true;

    @Test
    public void TestAnonymousCalendarPicker() throws Exception {
        final CalendarTextField spinner = new CalendarTextField() {};
        Assert.assertEquals(expected, spinner.getStyleClass());
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                root.getChildren().add(spinner);
            }
        }, 1);
        Assert.assertTrue(pass);
    }

    @Test
    public void TestExtendedCalendarPicker() throws Exception {
        final TestCalendarTextField picker = new TestCalendarTextField();
        Assert.assertEquals(expected, picker.getStyleClass());
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                    root.getChildren().add(picker);
            }
        }, 1);
        Assert.assertTrue(pass);
    }

    @Override
    protected Parent getRootNode() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                pass = false;
            }
        });
        root = new VBox();
        return root;
    }

    private class TestCalendarTextField extends CalendarTextField {};
}