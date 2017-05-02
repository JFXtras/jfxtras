package jfxtras;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.ChangeDialogOptionsTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.ExceptionDateTests;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.MiscPopupTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.PopupDeleteAllTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.PopupReviseAllTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.PopupReviseOneTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.PopupReviseThisAndFutureTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.RecurrenceRuleDescriptionTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.VJournalMakeiTIPTest;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.VTodoMakeiTIPTest;
import jfxtras.scene.control.agenda.icalendar.agenda.DeleteVEventTest;
import jfxtras.scene.control.agenda.icalendar.agenda.GraphicallyChangeTest;
import jfxtras.scene.control.agenda.icalendar.agenda.MakeNewVEventsTest;
import jfxtras.scene.control.agenda.icalendar.agenda.RenderVEventsTest;
import jfxtras.scene.control.agenda.icalendar.agenda.RevisePopupTest;
import jfxtras.scene.control.agenda.icalendar.agenda.VEventDisplayPopupTest;
import jfxtras.scene.control.agenda.icalendar.editors.deletor.DeleteAllTest;
import jfxtras.scene.control.agenda.icalendar.editors.deletor.DeleteOneTest;
import jfxtras.scene.control.agenda.icalendar.editors.deletor.DeleteThisAndFutureTest;
import jfxtras.scene.control.agenda.icalendar.editors.revisor.CancelRevisionTest;
import jfxtras.scene.control.agenda.icalendar.editors.revisor.ReviseAllTest;
import jfxtras.scene.control.agenda.icalendar.editors.revisor.ReviseNonRepeatingTest;
import jfxtras.scene.control.agenda.icalendar.editors.revisor.ReviseOneTest;
import jfxtras.scene.control.agenda.icalendar.editors.revisor.ReviseThisAndFutureTest;
import jfxtras.scene.control.agenda.icalendar.misc.ComponentChangeDialogTest;
import jfxtras.scene.control.agenda.icalendar.misc.MakeAppointmentsTest;

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
