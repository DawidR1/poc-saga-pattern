package pl.dawid.medicalpackageservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.dawid.medicalpackageservice.mq.messages.ValMedicalPackageResult;
import pl.dawid.medicalpackageservice.mq.messages.ValMedicalPackageReq;
import pl.dawid.medicalpackageservice.mq.QueueConf;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalPackValiService {

  private final RabbitTemplate rabbitTemplate;

  @SneakyThrows
  public void validate(ValMedicalPackageReq dto) {

    if (new Random().nextInt(10) < 7) {
      ValMedicalPackageResult build = ValMedicalPackageResult.builder()
          .appointmentId(dto.getAppointmentId())
          .valid(true)
          .build();
      sendMessage(build);
      log.info("Service: medical-package-service; medical package validation; producer; valid: " + build);

    } else {
      ValMedicalPackageResult req = ValMedicalPackageResult.builder()
          .appointmentId(dto.getAppointmentId())
          .valid(false)
          .build();
      sendMessage(req);
      log.info("Service: medical-package-service; medical package validation; producer; invalid: " + req);
    }
  }

  private void sendMessage(ValMedicalPackageResult build) throws JsonProcessingException {
    String payload = new ObjectMapper().writeValueAsString(build);
    rabbitTemplate.convertAndSend(QueueConf.APPOINTMENT_BOOKING_EXCHANGE, QueueConf.K_VAL_MEDICAL_PACKAGE_RESP, payload);
  }
}
