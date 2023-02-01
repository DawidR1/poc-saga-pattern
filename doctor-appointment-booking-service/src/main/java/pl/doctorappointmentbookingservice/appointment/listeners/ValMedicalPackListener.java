package pl.doctorappointmentbookingservice.appointment.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.mq.QueueConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.ValMedicalPackageResult;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;

@Component
@RequiredArgsConstructor
@Slf4j
class ValMedicalPackListener {

  private final AppointmentService appointmentService;

  @RabbitListener(queues = QueueConf.VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE)
  void validate(String message) throws JsonProcessingException, InterruptedException {
    log.info("Service: appointment-booking-service; validate medical package; consumer: " + message);
    ValMedicalPackageResult valMedicalPackageResult = new ObjectMapper().readValue(message, ValMedicalPackageResult.class);
    Thread.sleep(1000);
    appointmentService.processValOfMedicalPackageResult(valMedicalPackageResult);
  }
}
