package pl.dawid.medicalpackageservice.listeners.dto;


import java.io.Serializable;
import java.util.UUID;
import lombok.Value;

@Value
public class ValMedicalPackageResp implements Serializable {

  UUID patientId;
  UUID appointmentId;

}
