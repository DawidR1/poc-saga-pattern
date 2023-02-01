package pl.doctorappointmentbookingservice.appointment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.dto.AppointmentBookingDto;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;


@RestController
@RequiredArgsConstructor
@RequestMapping("appointment")
@Slf4j
class AppointmentBookingController {

  private final AppointmentService appointmentService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void createAppointment(@RequestBody AppointmentBookingDto dto) {
    appointmentService.bookAppointment(dto);
    appointmentService.getAppointment();
  }

  @GetMapping()
  public List<AppointmentBooking> getAppointment() {
    return appointmentService.getAppointment();
  }
}
