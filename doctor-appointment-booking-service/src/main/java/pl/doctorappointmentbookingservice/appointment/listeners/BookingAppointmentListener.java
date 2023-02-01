package pl.doctorappointmentbookingservice.appointment.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.mq.QueueConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentResult;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;

@Component
@RequiredArgsConstructor
@Slf4j
class BookingAppointmentListener {

  private final AppointmentService appointmentService;

  @RabbitListener(queues = QueueConf.BOOK_APPOINTMENT_RESP_QUEUE)
  void validate(String message) throws JsonProcessingException {
    log.info("Service: appointment-booking-service; booking appointment; consumer: " + message);
    BookAppointmentResult dto = new ObjectMapper().readValue(message, BookAppointmentResult.class);
    appointmentService.processBookingAppointmentResult(dto);
  }
}
