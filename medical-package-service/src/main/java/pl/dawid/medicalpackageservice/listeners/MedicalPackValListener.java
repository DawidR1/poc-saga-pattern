package pl.dawid.medicalpackageservice.listeners;


import static pl.dawid.medicalpackageservice.mq.QueueConf.VALIDATE_MEDICAL_PACKAGE_QUEUE;
import static pl.dawid.medicalpackageservice.mq.QueueConf.VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageResp;
import pl.dawid.medicalpackageservice.service.MedicalPackValiService;

@Component
@RequiredArgsConstructor
class MedicalPackValListener {

  private final MedicalPackValiService service;



  @RabbitListener(queues = VALIDATE_MEDICAL_PACKAGE_QUEUE)
  void validate(String response) throws JsonProcessingException {
    System.out.println("medical-package-service, Validate medical package consumer: " + response);
    service.validate(new ObjectMapper().readValue(response, ValMedicalPackageResp.class));
  }
}
