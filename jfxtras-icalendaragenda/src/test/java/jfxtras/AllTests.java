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
import jfxtras.scene.control.agenda.icalendar.agenda.NotifyCalendarUpdate;
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
    NotifyCalendarUpdate.class,
    RenderVEventsTest.class,
    RevisePopupTest.class,
    VEventDisplayPopupTest.class,
    
              })
public class AllTests {

}
