package pl.doctorappointmentbookingservice.appointment.statemachine.actions;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.mq.QueuesConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentDto;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;

@RequiredArgsConstructor
@Component
public class BookAppointemntAction implements Action<AppointmentBookingStatus, AppointmentBookingEvent> {

  private final RabbitTemplate rabbitTemplate;
  private final AppointmentBookingRepository appointmentBookingRepository;

  @SneakyThrows
  @Transactional
  @Override
  public void execute(StateContext<AppointmentBookingStatus, AppointmentBookingEvent> context) {
    String id = (String) context.getMessage().getHeaders().get(APPOINTMENT_BOOKING_ID);
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(UUID.fromString(id)).orElseThrow();

    String req = BookAppointmentDto.toJson(appointmentBooking);
    rabbitTemplate.convertAndSend(
        QueuesConf.APPOINTMENT_BOOKING_EXCHANGE,
        QueuesConf.BOOK_APPOINTMENT_KEY,
        req);
    System.out.println("appointment-booking-service, Book appointment producer: " + req);
  }
}
