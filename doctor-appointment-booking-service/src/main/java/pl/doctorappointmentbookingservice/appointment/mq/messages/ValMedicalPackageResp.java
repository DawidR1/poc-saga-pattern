package pl.doctorappointmentbookingservice.appointment.mq.messages;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValMedicalPackageResp {

  boolean valid;
  UUID appointmentId;
}
