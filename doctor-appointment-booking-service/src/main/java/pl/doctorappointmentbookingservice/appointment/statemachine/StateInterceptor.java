package pl.doctorappointmentbookingservice.appointment.statemachine;


import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;

@Component
public class StateInterceptor extends StateMachineInterceptorAdapter<AppointmentBookingStatus, AppointmentBookingEvent> {

 @Override
 public void preStateChange(State<AppointmentBookingStatus, AppointmentBookingEvent> state,
     Message<AppointmentBookingEvent> message, Transition<AppointmentBookingStatus, AppointmentBookingEvent> transition,
     StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine,
     StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> rootStateMachine) {
  System.out.println();
 }
}
