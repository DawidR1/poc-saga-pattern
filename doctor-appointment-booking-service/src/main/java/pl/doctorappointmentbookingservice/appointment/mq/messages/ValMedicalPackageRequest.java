package pl.doctorappointmentbookingservice.appointment.mq.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.UUID;
import lombok.Value;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;

@Value
public class ValMedicalPackageRequest implements Serializable {

   UUID patientId;
   UUID appointmentId;


  public static String toDto(AppointmentBooking appointmentBooking) throws JsonProcessingException {
    ValMedicalPackageRequest valMedicalPackageRequest = new ValMedicalPackageRequest(appointmentBooking.getPatientId(),
        appointmentBooking.getId());
    return new ObjectMapper().writeValueAsString(valMedicalPackageRequest);
  }
}
