package pl.doctorappointmentbookingservice.appointment.statemachine.actions;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.mq.QueueConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentReq;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class BookAppointemntAction implements Action<AppointmentBookingStatus, AppointmentBookingEvent> {

  private final RabbitTemplate rabbitTemplate;
  private final AppointmentBookingRepository appointmentBookingRepository;
  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Transactional
  @Override
  public void execute(StateContext<AppointmentBookingStatus, AppointmentBookingEvent> context) {
    String id = (String) context.getMessage().getHeaders().get(APPOINTMENT_BOOKING_ID);
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(UUID.fromString(id)).orElseThrow();

    String req = BookAppointmentReq.toJson(appointmentBooking, objectMapper);
    rabbitTemplate.convertAndSend(
        QueueConf.APPOINTMENT_BOOKING_EXCHANGE,
        QueueConf.BOOK_APPOINTMENT_KEY,
        req);
    log.info("Service: appointment-booking-service; booking appointment; producer: " + req);
  }
}
