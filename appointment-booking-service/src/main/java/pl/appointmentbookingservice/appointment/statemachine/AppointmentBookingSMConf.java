package pl.appointmentbookingservice.appointment.statemachine;


import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import pl.appointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.appointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.appointmentbookingservice.appointment.statemachine.actions.BookAppointemntAction;
import pl.appointmentbookingservice.appointment.statemachine.actions.ValidateMedicalPackageAction;

@RequiredArgsConstructor
@Configuration
@EnableStateMachineFactory
public class AppointmentBookingSMConf extends StateMachineConfigurerAdapter<AppointmentBookingStatus, AppointmentBookingEvent> {

  public static final String APPOINTMENT_BOOKING_ID = "APPOINTMENT_BOOKING_ID";
  private final ValidateMedicalPackageAction validateMedicalPackageAction;
  private final BookAppointemntAction bookAppointemntAction;

  @Override
  public void configure(StateMachineStateConfigurer<AppointmentBookingStatus, AppointmentBookingEvent> states) throws Exception {
    states.withStates()
        .initial(AppointmentBookingStatus.NEW)
        .states(EnumSet.allOf(AppointmentBookingStatus.class))
        .end(AppointmentBookingStatus.APPOINTMENT_REJECTED)
        .end(AppointmentBookingStatus.APPOINTMENT_APPROVED)
        .end(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_REJECTED);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<AppointmentBookingStatus, AppointmentBookingEvent> transitions)
      throws Exception {
    transitions.withExternal()
        .source(AppointmentBookingStatus.NEW)
        .target(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_PENDING)
        .event(AppointmentBookingEvent.VALIDATE_MEDICAL_PACKAGE)
        .action(validateMedicalPackageAction)

        .and()
        .withExternal()
        .source(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_PENDING)
        .target(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_APPROVED)
        .event(AppointmentBookingEvent.APPROVE_MEDICAL_PACKAGE)

        .and()
        .withExternal()
        .source(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_PENDING)
        .target(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_REJECTED)
        .event(AppointmentBookingEvent.REJECT_MEDICAL_PACKAGE)

        .and()
        .withExternal()
        .source(AppointmentBookingStatus.VAL_MEDICAL_PACKAGE_APPROVED)
        .target(AppointmentBookingStatus.APPOINTMENT_BOOKING_PENDING)
        .event(AppointmentBookingEvent.BOOK_APPOINTMENT)
        .action(bookAppointemntAction)

        .and()
        .withExternal()
        .source(AppointmentBookingStatus.APPOINTMENT_BOOKING_PENDING)
        .target(AppointmentBookingStatus.APPOINTMENT_APPROVED)
        .event(AppointmentBookingEvent.APPROVE_APPOINTMENT)

        .and()
        .withExternal()
        .source(AppointmentBookingStatus.APPOINTMENT_BOOKING_PENDING)
        .target(AppointmentBookingStatus.APPOINTMENT_REJECTED)
        .event(AppointmentBookingEvent.REJECT_APPOINTMENT);

  }
}
