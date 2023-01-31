package pl.dawid.medicalpackageservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageReq;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageResp;
import pl.dawid.medicalpackageservice.mq.QueueConf;

@Service
@RequiredArgsConstructor
public class MedicalPackValiService {

  private final RabbitTemplate rabbitTemplate;

  public void validate(ValMedicalPackageResp response) {

//    if() custom logic
    ValMedicalPackageReq build = ValMedicalPackageReq.builder()
        .appointmentId(response.getAppointmentId())
        .valid(true)
        .build();
    rabbitTemplate.convertAndSend(QueueConf.APPOINTMENT_BOOKING_EXCHANGE, QueueConf.K_VAL_MEDICAL_PACKAGE_RESP, build);
  }
}
