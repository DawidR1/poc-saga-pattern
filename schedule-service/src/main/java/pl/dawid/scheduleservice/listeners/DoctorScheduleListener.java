package pl.dawid.scheduleservice.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dawid.scheduleservice.mq.QueueConf;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentResDto;
import pl.dawid.scheduleservice.services.DoctorScheduleService;
@RequiredArgsConstructor
@Component
class DoctorScheduleListener {

 private final DoctorScheduleService service;

 @RabbitListener(queues = QueueConf.BOOK_APPOINTMENT_QUEUE)
 void validate(String response) throws JsonProcessingException {
  System.out.println("schedule-service, Book appointment: " + response);
  service.makeAnAppointment(new ObjectMapper().readValue(response, BookAppointmentResDto.class));
 }

}
