package pl.doctorappointmentbookingservice.appointment.statemachine;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;

@Component
@RequiredArgsConstructor
public class StateInterceptor extends StateMachineInterceptorAdapter<AppointmentBookingStatus, AppointmentBookingEvent> {

  private final AppointmentBookingRepository appointmentBookingRepository;

  @Override
  public void preStateChange(State<AppointmentBookingStatus, AppointmentBookingEvent> state,
      Message<AppointmentBookingEvent> message, Transition<AppointmentBookingStatus, AppointmentBookingEvent> transition,
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine,
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> rootStateMachine) {
    Optional.ofNullable(message)
        .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(APPOINTMENT_BOOKING_ID, " ")))
        .flatMap(uuid -> appointmentBookingRepository.findById(UUID.fromString(uuid)))
        .ifPresent(appointmentBooking -> {
          appointmentBooking.setStatus(state.getId());
          appointmentBookingRepository.saveAndFlush(appointmentBooking);
        });
  }
}
