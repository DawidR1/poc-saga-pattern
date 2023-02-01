package pl.appointmentbookingservice.appointment.mq.messages;


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
public class BookAppointmentResult implements Serializable {

  private UUID appointmentId;
  private boolean booked;
  private String reason;

}
