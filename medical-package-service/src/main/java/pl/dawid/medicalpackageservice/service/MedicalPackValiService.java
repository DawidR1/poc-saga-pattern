package pl.dawid.medicalpackageservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageReq;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageResp;
import pl.dawid.medicalpackageservice.mq.QueueConf;

@Service
@RequiredArgsConstructor
public class MedicalPackValiService {

  private final RabbitTemplate rabbitTemplate;

  @SneakyThrows
  public void validate(ValMedicalPackageResp response) {

//    if() custom logic
    ValMedicalPackageReq build = ValMedicalPackageReq.builder()
        .appointmentId(response.getAppointmentId())
        .valid(true)
        .build();
    String payload = new ObjectMapper().writeValueAsString(build);
    rabbitTemplate.convertAndSend(QueueConf.APPOINTMENT_BOOKING_EXCHANGE, QueueConf.K_VAL_MEDICAL_PACKAGE_RESP, payload);
    System.out.println("medical-package-service, Validate medical package resp producer: " + payload);
  }
}
