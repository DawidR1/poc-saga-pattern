package pl.dawid.scheduleservice.mq.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public static BookAppointmentResult toBookingRejected(UUID appointmentId, String reason) {
    return BookAppointmentResult.builder()
        .appointmentId(appointmentId)
        .reason(reason)
        .booked(false)
        .build();
  }

  public static BookAppointmentResult toBookingAccepted(UUID appointmentId) throws JsonProcessingException {
    return BookAppointmentResult.builder()
        .appointmentId(appointmentId)
        .booked(true)
        .build();
  }

  public String toJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(this);
  }
}
