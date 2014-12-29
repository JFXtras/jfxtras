package jfxtras.scene.control.agenda.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AgendaMouseManipulateTest.class
    		  , AgendaRenderTest.class
    		  , AgendaSelectTest.class
    		  , AgendaMenuTest.class
              , AgendaFXMLTest.class
              , AllAppointmentsTest.class
	          })
public class AllTests {

}
