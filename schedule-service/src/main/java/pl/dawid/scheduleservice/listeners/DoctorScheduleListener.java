package pl.dawid.scheduleservice.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dawid.scheduleservice.mq.QueueConf;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentReq;
import pl.dawid.scheduleservice.services.DoctorScheduleService;

@RequiredArgsConstructor
@Component
@Slf4j
class DoctorScheduleListener {

  private final DoctorScheduleService service;
  private final ObjectMapper mapper;

  @RabbitListener(queues = QueueConf.BOOK_APPOINTMENT_QUEUE)
  void validate(String message) throws JsonProcessingException {
    log.info("Service: schedule-service; booking appointment ;consumer: " + message);
    service.makeAnAppointment(mapper.readValue(message, BookAppointmentReq.class));
  }

}
