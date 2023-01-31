package pl.dawid.medicalpackageservice.listeners;


import static pl.dawid.medicalpackageservice.mq.QueueConf.VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.dawid.medicalpackageservice.listeners.dto.ValMedicalPackageResp;
import pl.dawid.medicalpackageservice.service.MedicalPackValiService;

@Component
@RequiredArgsConstructor
class MedicalPackValListener {

  private final MedicalPackValiService service;



  @RabbitListener(queues = VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE)
  void validate(ValMedicalPackageResp response) {
    service.validate(response);
  }
}
