package pl.doctorappointmentbookingservice.appointment.mq.messages;


import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValMedicalPackageResp implements Serializable {

  boolean valid;
  UUID appointmentId;
}
