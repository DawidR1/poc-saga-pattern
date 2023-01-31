package pl.doctorappointmentbookingservice.appointment.dto;


import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class AppointmentBookingDto {

  private UUID patientId;
  private UUID doctorId;
  private LocalDate from;
  private LocalDate to;

  public static AppointmentBookingDto toDto(AppointmentBooking appointmentBooking) {
    return AppointmentBookingDto.builder()
        .doctorId(appointmentBooking.getDoctorId())
        .from(appointmentBooking.getFromDate())
        .to(appointmentBooking.getToDate())
        .patientId(appointmentBooking.getPatientId())
        .build();

  }
}
