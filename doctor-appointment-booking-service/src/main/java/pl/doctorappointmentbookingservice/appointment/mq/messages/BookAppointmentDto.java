package pl.doctorappointmentbookingservice.appointment.mq.messages;


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
public class BookAppointmentDto implements Serializable {

  UUID patientId;
  UUID doctorId;
  UUID appointmentId;
  LocalDate fromDate;
  LocalDate toDate;


  public static String toJson(AppointmentBooking appointmentBooking) throws JsonProcessingException {
    BookAppointmentDto dto = BookAppointmentDto.builder()
        .appointmentId(appointmentBooking.getId())
        .fromDate(appointmentBooking.getFromDate())
        .doctorId(appointmentBooking.getDoctorId())
        .patientId(appointmentBooking.getPatientId())
        .toDate(appointmentBooking.getToDate())
        .build();
    return new ObjectMapper().writeValueAsString(dto);
  }
}
