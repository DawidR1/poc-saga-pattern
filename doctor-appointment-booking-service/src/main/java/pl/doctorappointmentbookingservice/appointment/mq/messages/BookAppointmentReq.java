package pl.doctorappointmentbookingservice.appointment.mq.messages;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;

@Value
@Builder
public class BookAppointmentReq implements Serializable {

  UUID patientId;
  UUID doctorId;
  UUID appointmentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate fromDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate toDate;


  public static String toJson(AppointmentBooking appointmentBooking, ObjectMapper objectMapper) throws JsonProcessingException {
    BookAppointmentReq dto = BookAppointmentReq.builder()
        .appointmentId(appointmentBooking.getId())
        .fromDate(appointmentBooking.getFromDate())
        .doctorId(appointmentBooking.getDoctorId())
        .patientId(appointmentBooking.getPatientId())
        .toDate(appointmentBooking.getToDate())
        .build();
    return objectMapper.writeValueAsString(dto);
  }
}
