package pl.doctorappointmentbookingservice.appointment.listeners;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.doctorappointmentbookingservice.appointment.mq.QueuesConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.ValMedicalPackageResp;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;

@Component
@RequiredArgsConstructor
class ValMedicalPackListener {

  private final AppointmentService appointmentService;

  @RabbitListener(queues = QueuesConf.VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE)
  void validate(ValMedicalPackageResp response) {
   appointmentService.processValOfMedicalPackage(response);
  }
}
