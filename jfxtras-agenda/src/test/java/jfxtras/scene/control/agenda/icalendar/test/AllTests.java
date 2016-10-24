package jfxtras.scene.control.agenda.icalendar.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.ChangeDialogOptionsTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.ExceptionDateTests;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.MiscPopupTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.PopupDeleteAllTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.PopupReviseAllTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.PopupReviseOneTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.PopupReviseThisAndFutureTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.RecurrenceRuleDescriptionTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.VJournalMakeiTIPTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test.VTodoMakeiTIPTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.DeleteVEventTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.GraphicallyChangeTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.MakeNewVEventsTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.RenderVEventsTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.RevisePopupTest;
import jfxtras.scene.control.agenda.icalendar.test.agenda.VEventDisplayPopupTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.deletor.DeleteAllTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.deletor.DeleteOneTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.deletor.DeleteThisAndFutureTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.revisor.CancelRevisionTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.revisor.ReviseAllTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.revisor.ReviseNonRepeatingTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.revisor.ReviseOneTest;
import jfxtras.scene.control.agenda.icalendar.test.editors.revisor.ReviseThisAndFutureTest;
import jfxtras.scene.control.agenda.icalendar.test.misc.ComponentChangeDialogTest;
import jfxtras.scene.control.agenda.icalendar.test.misc.MakeAppointmentsTest;

@RunWith(Suite.class)
@SuiteClasses({ 

    // misc tests
    ComponentChangeDialogTest.class,
    MakeAppointmentsTest.class,
    
    // revise tests
    CancelRevisionTest.class,
    ReviseAllTest.class,
    ReviseNonRepeatingTest.class,
    ReviseThisAndFutureTest.class,
    ReviseOneTest.class,
    
    // delete tests
    DeleteAllTest.class,
    DeleteThisAndFutureTest.class,
    DeleteOneTest.class,
    
    // popup tests
    ChangeDialogOptionsTest.class,
    ExceptionDateTests.class,
    MiscPopupTest.class,
    PopupDeleteAllTest.class,
    PopupReviseAllTest.class,
    PopupReviseThisAndFutureTest.class,
    PopupReviseOneTest.class,
    RecurrenceRuleDescriptionTest.class,
    VJournalMakeiTIPTest.class,
    VTodoMakeiTIPTest.class,
    
    // agenda tests
    DeleteVEventTest.class,
    GraphicallyChangeTest.class,
    MakeNewVEventsTest.class,
    RenderVEventsTest.class,
    RevisePopupTest.class,
    VEventDisplayPopupTest.class,
    
              })
public class AllTests {

}
