package pl.dawid.medicalpackageservice.listeners;


import static pl.dawid.medicalpackageservice.mq.QueueConf.VALIDATE_MEDICAL_PACKAGE_QUEUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dawid.medicalpackageservice.mq.messages.ValMedicalPackageReq;
import pl.dawid.medicalpackageservice.service.MedicalPackValiService;

@Component
@RequiredArgsConstructor
@Slf4j
class MedicalPackValListener {

  private final MedicalPackValiService service;


  @RabbitListener(queues = VALIDATE_MEDICAL_PACKAGE_QUEUE)
  void validate(String message) throws JsonProcessingException {
    log.info("Service: medical-package-service; medical package consumer: " + message);
    service.validate(new ObjectMapper().readValue(message, ValMedicalPackageReq.class));
  }
}
