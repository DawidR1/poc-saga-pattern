package pl.dawid.medicalpackageservice.listeners.dto;


import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValMedicalPackageReq implements Serializable {

  private UUID patientId;
  private UUID appointmentId;

}
