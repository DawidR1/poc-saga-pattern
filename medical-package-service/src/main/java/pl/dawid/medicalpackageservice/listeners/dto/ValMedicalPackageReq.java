package pl.dawid.medicalpackageservice.listeners.dto;


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
public class ValMedicalPackageReq implements Serializable {

  boolean valid;
  UUID appointmentId;

}
