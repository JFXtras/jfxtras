package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.TemporalUtilities;

/**
 * Added dateTimeEnd or dateTimeDue to {@link EditDescriptiveVBox}
 * 
 * @author David Bal
 *
 * @param <T> subclass of {@link VComponentLocatable}
 */
public abstract class EditDescriptiveLocatableVBox<T extends VLocatable<T>> extends EditDescriptiveVBox<T>
{
    private static final Duration DEFAULT_DURATION = Duration.ofHours(1);
    protected LocalDateTimeTextField endDateTimeTextField = new LocalDateTimeTextField(); // end of recurrence
    protected LocalDateTextField endDateTextField = new LocalDateTextField(); // end of recurrence when wholeDayCheckBox is selected
    
    public EditDescriptiveLocatableVBox()
    {
        super();
        endDateTimeTextField.setId("endDateTimeTextField");
        endDateTextField.setId("endDateTextField");
    }
    
    @Override
    void synchStartDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        super.synchStartDateTime(oldValue, newValue);
        LocalDateTime end = endDateTimeTextField.getLocalDateTime();
        if ((oldValue != null) && (end != null))
        {
            TemporalAmount duration = Duration.between(oldValue, end);
            endDateTimeTextField.setLocalDateTime(newValue.plus(duration));
        }
    }
    
    @Override
    void synchStartDate(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDate(oldValue, newValue);
        LocalDate end = endDateTextField.getLocalDate();
        if ((oldValue != null) && (end != null))
        {
            TemporalAmount duration = Period.between(oldValue, end);
            endDateTextField.setLocalDate(newValue.plus(duration));
        }
    }
    
    final private ChangeListener<? super LocalDate> endDateTextListener = (observable, oldValue, newValue) -> synchEndDate(oldValue, newValue);

    /** Update endDateTimeTextField when endDateTextField changes */
    void synchEndDate(LocalDate oldValue, LocalDate newValue)
    {
        endNewRecurrence = newValue;
        endDateTimeTextField.localDateTimeProperty().removeListener(endDateTimeTextListener);
        LocalDateTime newDateTime = endDateTimeTextField.getLocalDateTime().with(newValue.minusDays(1));
        endDateTimeTextField.setLocalDateTime(newDateTime);
        endDateTimeTextField.localDateTimeProperty().addListener(endDateTimeTextListener);
    }

    final private ChangeListener<? super LocalDateTime> endDateTimeTextListener = (observable, oldValue, newValue) -> synchEndDateTime(oldValue, newValue);

    /** Update endDateTextField when endDateTimeTextField changes */
    void synchEndDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        if (startOriginalRecurrence.isSupported(ChronoUnit.NANOS)) // ZoneDateTime, LocalDateTime
        {
            endNewRecurrence = startOriginalRecurrence.with(newValue);            
        } else // LocalDate - use ZonedDateTime at system default ZoneId
        {
            endNewRecurrence = ZonedDateTime.of(newValue, ZoneId.systemDefault());
        }
        endDateTextField.localDateProperty().removeListener(endDateTextListener);
        LocalDate newDate = LocalDate.from(endDateTimeTextField.getLocalDateTime()).plusDays(1);
        endDateTextField.setLocalDate(newDate);
        endDateTextField.localDateProperty().addListener(endDateTextListener);
    }

    Temporal endNewRecurrence; // bound to endTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in endTextListener
//    private Temporal endRecurrenceOriginal;

    @Override
    public void setupData(
//            Appointment appointment,
            T vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
//            List<T> vComponents,
            List<String> categories)
    {
//        endRecurrenceOriginal = appointment.getEndTemporal();
        
        if (vComponent.getDescription() == null)
        {
            vComponent.setDescription(Description.parse(""));
        }
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescription().valueProperty());
        if (vComponent.getLocation() == null)
        {
            vComponent.setLocation(Location.parse(""));
        }
        locationTextField.textProperty().bindBidirectional(vComponent.getLocation().valueProperty());
                
        /*
         * END DATE/TIME
         */
        endDateTimeTextField.setLocale(Locale.getDefault());
        endDateTextField.setLocale(Locale.getDefault());
        endDateTimeTextField.setParseErrorCallback(errorCallback);
        endDateTextField.setParseErrorCallback(errorCallback);
        final LocalDateTime end1;
        final LocalDate end2;
        if (vComponent.isWholeDay())
        {
            end1 = LocalDate.from(endRecurrence).minusDays(1).atTime(DEFAULT_START_TIME).plus(DEFAULT_DURATION);
            end2 = LocalDate.from(endRecurrence);
        } else
        {
            end1 = TemporalUtilities.toLocalDateTime(endRecurrence);
            end2 = LocalDate.from(endRecurrence).plusDays(1);
        }
        endDateTimeTextField.setLocalDateTime(end1);
        endDateTextField.setLocalDate(end2);
        endDateTimeTextField.localDateTimeProperty().addListener(endDateTimeTextListener);
        endDateTextField.localDateProperty().addListener(endDateTextListener);
        
        // Ensure end date/time is not before start date/time
        endDateTimeTextField.localDateTimeProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if ((startDateTimeTextField.getLocalDateTime() != null) && newSelection.isBefore(startDateTimeTextField.getLocalDateTime()))
            {
                tooEarlyDateAlert(newSelection, startDateTimeTextField.getLocalDateTime());
                endDateTimeTextField.setLocalDateTime(oldSelection);
            }
        });
        // Ensure end date is not before start date
        endDateTextField.localDateProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if ((startDateTextField.getLocalDate() != null) && newSelection.minusDays(1).isBefore(startDateTextField.getLocalDate()))
            {
                tooEarlyDateAlert(newSelection, startDateTextField.getLocalDate());
                endDateTextField.setLocalDate(oldSelection);
            }
        });
        
        super.setupData(vComponent, startRecurrence, endRecurrence, categories);
    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private void tooEarlyDateAlert(Temporal t1, Temporal t2)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("End must be after start");
        alert.setContentText(t1 + " is not after" + System.lineSeparator() + t2);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    /*
     * Change end date/time when whole day is changed
     */
    @Override
    void handleWholeDayChange(T vComponent, Boolean newSelection)
    {
        endDateTimeTextField.localDateTimeProperty().removeListener(endDateTimeTextListener);
        endDateTextField.localDateProperty().removeListener(endDateTextListener);
        super.handleWholeDayChange(vComponent, newSelection);
        if (newSelection)
        {
            timeGridPane.getChildren().remove(endDateTimeTextField);
            timeGridPane.add(endDateTextField, 1, 1);
            endNewRecurrence = endDateTextField.getLocalDate();
        } else
        {
            timeGridPane.getChildren().remove(endDateTextField);
            timeGridPane.add(endDateTimeTextField, 1, 1);
            if (startOriginalRecurrence instanceof LocalDate)
            {
                endNewRecurrence = endDateTimeTextField.getLocalDateTime().atZone(DEFAULT_ZONE_ID);
            } else if (startOriginalRecurrence instanceof LocalDateTime)
            {
                endNewRecurrence = endDateTimeTextField.getLocalDateTime();
            } else if (startOriginalRecurrence instanceof ZonedDateTime)
            {
                ZoneId originalZoneId = ((ZonedDateTime) startOriginalRecurrence).getZone();
                endNewRecurrence = endDateTimeTextField.getLocalDateTime().atZone(originalZoneId);
            } else
            {
                throw new DateTimeException("Unsupported Temporal type:" + startOriginalRecurrence.getClass());
            }
        }
        endDateTextField.localDateProperty().addListener(endDateTextListener);
        endDateTimeTextField.localDateTimeProperty().addListener(endDateTimeTextListener);
//        System.out.println("endNewRecurrence:" + endNewRecurrence);
    }
    
//    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
//     * endRecurrence to closest valid values
//     */
//    @Override
//    Runnable validateStartRecurrence()
//    {
////        Temporal actualRecurrence = vEvent.streamRecurrences(startRecurrence).findFirst().get();
//        if (! vComponentEdited.isRecurrence(startRecurrenceProperty.get()))
//        {
//            Temporal recurrenceBefore = vComponentEdited.previousStreamValue(startRecurrenceProperty.get());
//            Optional<Temporal> optionalAfter = vComponentEdited.streamRecurrences(startRecurrenceProperty.get()).findFirst();
//            Temporal newStartRecurrence = (optionalAfter.isPresent()) ? optionalAfter.get() : recurrenceBefore;
//            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startRecurrenceProperty.get(), endRecurrence);
//            Temporal newEndRecurrence = newStartRecurrence.plus(duration);
//            Temporal startRecurrenceBeforeChange = startRecurrenceProperty.get();
//            startDateTimeTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newStartRecurrence));
//            endDateTimeTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newEndRecurrence));
//            startOriginalRecurrence = startRecurrenceProperty.get();
//            return () -> startRecurrenceChangedAlert(startRecurrenceBeforeChange, newStartRecurrence);
//        }
//        return null;
//    }
}
