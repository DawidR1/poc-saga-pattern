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
public class BookAppointmentReqDto implements Serializable {

  private UUID appointmentId;
  private boolean booked;
  private String reason;

  public static BookAppointmentReqDto toBookingRejected(UUID appointmentId, String reason) {
    return BookAppointmentReqDto.builder()
        .appointmentId(appointmentId)
        .reason(reason)
        .booked(false)
        .build();
  }

  public static BookAppointmentReqDto toBookingAccepted(UUID appointmentId) throws JsonProcessingException {
    return BookAppointmentReqDto.builder()
        .appointmentId(appointmentId)
        .booked(true)
        .build();
  }

  public String toJson() throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(this);
  }
}
