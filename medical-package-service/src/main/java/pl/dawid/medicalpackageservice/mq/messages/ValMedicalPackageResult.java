package pl.dawid.medicalpackageservice.mq.messages;


import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValMedicalPackageResult implements Serializable {

  boolean valid;
  UUID appointmentId;

}
