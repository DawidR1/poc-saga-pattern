package pl.doctorappointmentbookingservice.appointment.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.mq.QueuesConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentResDto;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;

@Component
@RequiredArgsConstructor
class BookingAppointmentListener {

  private final AppointmentService appointmentService;

  @RabbitListener(queues = QueuesConf.BOOK_APPOINTMENT_RESP_QUEUE)
  void validate(String response) throws JsonProcessingException {
    BookAppointmentResDto dto = new ObjectMapper().readValue(response, BookAppointmentResDto.class);
    System.out.println("appointment-booking-service, Booking appointment resp consumer: " + response);

    appointmentService.processBookingAppointmentResult(dto);
  }
}
