package pl.dawid.scheduleservice.mq.messages;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAppointmentResDto implements Serializable {

  private UUID patientId;
  private UUID doctorId;
  private UUID appointmentId;
  private LocalDate fromDate;
  private LocalDate toDate;

}
