package pl.doctorappointmentbookingservice.appointment.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  void validate(String response) throws JsonProcessingException {
    ValMedicalPackageResp valMedicalPackageResp = new ObjectMapper().readValue(response, ValMedicalPackageResp.class);
    System.out.println("appointment-booking-service, Validate medical package resp consumer: " + response);

    appointmentService.processValOfMedicalPackage(valMedicalPackageResp);
  }
}
