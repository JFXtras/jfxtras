package jfxtras.scene.control.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CalendarPickerTest.class
              , CalendarPickerFXMLTest.class
              , CalendarTextFieldTest.class
	          , CalendarTextFieldFXMLTest.class
	          , ListSpinnerArrowTest.class
	          , ListSpinnerEditableTest.class
	          , LocalDatePickerTest.class
	          , LocalDatePickerFXMLTest.class
	          , LocalDateTimePickerTest.class 
	          })
public class AllTests {

}
