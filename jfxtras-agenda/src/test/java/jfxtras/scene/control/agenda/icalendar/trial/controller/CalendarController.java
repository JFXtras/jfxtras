package jfxtras.scene.control.agenda.icalendar.trial.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;


/**
 * @author David Bal
 *
 * Instantiates and setups the Agenda.
 * Contains listeners to write changes due to calendar interaction.  Properties are in Agenda class.
 */
public class CalendarController
{
     VCalendar vCalendar = new VCalendar();
     public ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);

    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private BorderPane agendaBorderPane;

    final ToggleGroup skinGroup = new ToggleGroup();
    @FXML private Label dateLabel;
    @FXML private ToggleButton daySkinButton;
    @FXML private ToggleButton weekSkinButton;
    @FXML private ToggleButton monthSkinButton;
    @FXML private ToggleButton agendaSkinButton;
    
    final private LocalDatePicker localDatePicker = new LocalDatePicker(LocalDate.now());
    
    public final ObjectProperty<LocalDate> selectedLocalDateProperty = new SimpleObjectProperty<LocalDate>();
    public final ObjectProperty<LocalDateTime> selectedLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    private Period shiftDuration = Period.ofWeeks(1);
    public final TemporalField dayOfWeekField = WeekFields.of(Locale.getDefault()).dayOfWeek();
    
    boolean editDone = false;
    
    @FXML public void initialize()
    {
       
        daySkinButton.setToggleGroup(skinGroup);
        weekSkinButton.setToggleGroup(skinGroup);
        monthSkinButton.setToggleGroup(skinGroup);
        agendaSkinButton.setToggleGroup(skinGroup);
        weekSkinButton.selectedProperty().set(true);
        
//        agenda.setOneAllThisAndFutureDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK);

        // accept new appointments
        agenda.setNewAppointmentCallback((LocalDateTimeRange dateTimeRange) -> 
        {
            Temporal s = dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault());
            Temporal e = dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault());
            return new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(s)
                    .withEndTemporal(e)
                    .withSummary("New")
                    .withDescription("")
                    .withAppointmentGroup(agenda.appointmentGroups().get(0));
        });
        
        agendaBorderPane.setCenter(agenda);
        dateLabel.textProperty().bind(makeLocalDateBindings(localDatePicker.localDateProperty()));
        
        localDatePicker.setPadding(new Insets(20, 0, 5, 0)); //(top/right/bottom/left)
        agendaBorderPane.setLeft(localDatePicker);

      localDatePicker.localDateProperty().addListener((observable, oldSelection, newSelection)
      -> {
          if (newSelection != null) agenda.setDisplayedLocalDateTime(newSelection.atStartOfDay());
      });

      // Enable month and year changing to move calendar
      localDatePicker.displayedLocalDateProperty().addListener((observable, oldSelection, newSelection)
              -> {
                  int dayOfMonth = localDatePicker.getLocalDate().getDayOfMonth();
                  localDatePicker.setLocalDate(newSelection.withDayOfMonth(dayOfMonth));
              });

        agenda.setPadding(new Insets(0, 0, 0, 5)); //(top/right/bottom/left)            
    }

    public void setupData(LocalDate startDate, LocalDate endDate)
    {
        VEvent vEventSplit = new VEvent()
                .withDateTimeEnd(LocalDateTime.of(endDate, LocalTime.of(5, 45)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(endDate.minusDays(1), LocalTime.of(15, 45)))
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(8).getDescription())
                .withDescription("Split Description")
                .withSummary(Summary.parse("Split"))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withUniqueIdentifier("20150110T080000-00@jfxtras.org");
        agenda.getVCalendar().getVEvents().add(vEventSplit);
        
        VEvent vEventZonedUntil = new VEvent()
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(10).getDescription())
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(9, 45)), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")))
                .withDescription("WeeklyZoned Description")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(15), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")).withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
                .withSummary(Summary.parse("WeeklyZoned Ends"))
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withUniqueIdentifier("20150110T080000-01@jfxtras.org");
        agenda.getVCalendar().getVEvents().add(vEventZonedUntil);
               
        VEvent vEventZonedInfinite = new VEvent()
            .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(3).getDescription())
            .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(12, 00)), ZoneId.of("America/Los_Angeles")))
            .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
            .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(7, 30)), ZoneId.of("America/Los_Angeles")))
            .withDescription("WeeklyZoned Description")
            .withRecurrenceRule(new RecurrenceRule2()
                    .withFrequency(FrequencyType.WEEKLY)
                    .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
            .withSummary(Summary.parse("WeeklyZoned Infinite"))
            .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
            .withUniqueIdentifier("20150110T080000-02@jfxtras.org");
        agenda.getVCalendar().getVEvents().add(vEventZonedInfinite);
        
        VEvent vEventLocalDate = new VEvent()
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(15).getDescription())
                .withDateTimeStart(startDate)
                .withDateTimeEnd(startDate.plusDays(1))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("LocalDate Description")
                .withSummary(Summary.parse("LocalDate"))
                .withUniqueIdentifier("20150110T080000-3@jfxtras.org")
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(3));
        agenda.getVCalendar().getVEvents().add(vEventLocalDate);

        VEvent vEventLocalDateTime = new VEvent()
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(2).getDescription())
                .withDateTimeStart(LocalDateTime.of(startDate, LocalTime.of(11, 00)))
                .withDateTimeEnd(LocalDateTime.of(startDate, LocalTime.of(13, 0)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("LocalDateTime Daily Description")
                .withSummary(Summary.parse("LocalDateTime Daily"))
                .withUniqueIdentifier("20150110T080000-4@jfxtras.org")
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY));
        agenda.getVCalendar().getVEvents().add(vEventLocalDateTime); 
        
        VEvent vEventLocalDateTimeMonthly = new VEvent()
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(17).getDescription())
                .withDateTimeStart(LocalDateTime.of(startDate, LocalTime.of(14, 00)))
                .withDateTimeEnd(LocalDateTime.of(startDate, LocalTime.of(15, 0)))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("Monthly Description")
                .withSummary(Summary.parse("Monthly"))
                .withUniqueIdentifier("20150110T080000-5@jfxtras.org")
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY));
        agenda.getVCalendar().getVEvents().add(vEventLocalDateTimeMonthly); 
        
        DayOfWeek dayOfWeek = DayOfWeek.from(startDate.plusDays(2));
        int ordinalWeekNumber = DateTimeUtilities.weekOrdinalInMonth(startDate.plusDays(2));
        VEvent vEventLocalDateMonthlyOrdinal = new VEvent()
                .withCategories(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS.get(5).getDescription())
                .withDateTimeStart(startDate.plusDays(2))
                .withDateTimeEnd(startDate.plusDays(3))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("Monthly Ordinal Description " + dayOfWeek + "#" + ordinalWeekNumber + " in month")
                .withSummary(Summary.parse("Monthly Ordinal"))
                .withUniqueIdentifier("20150110T080000-6@jfxtras.org")
                .withOrganizer("ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.MONTHLY)
                        .withByRules(new ByDay(new ByDay.ByDayPair(dayOfWeek, ordinalWeekNumber))));
        agenda.getVCalendar().getVEvents().add(vEventLocalDateMonthlyOrdinal);
        
        // replace Agenda's appointmentGroups with the ones used in the test events.
        agenda.appointmentGroups().clear();
        agenda.appointmentGroups().addAll(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
    }

    @FXML private void handleToday()
    {
        LocalDate today = LocalDate.now();
        localDatePicker.localDateProperty().set(today);
    }
    
    @FXML private void handleDateIncrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.plus(shiftDuration));
    }
    
    @FXML private void handleDateDecrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.minus(shiftDuration));
    }
    
    @FXML private void handleWeekSkin() {
        shiftDuration = Period.ofWeeks(1);
        agenda.setSkin(new AgendaWeekSkin(agenda));
    }

    @FXML private void handleDaySkin() {
        shiftDuration = Period.ofDays(1);
        agenda.setSkin(new AgendaDaySkin(agenda));
    }
    
    public static StringBinding makeLocalDateBindings(ObjectProperty<LocalDate> p)
    {
        final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return Bindings.createStringBinding(() -> DATE_FORMAT2.format(p.get()), p);
    }
    
}
