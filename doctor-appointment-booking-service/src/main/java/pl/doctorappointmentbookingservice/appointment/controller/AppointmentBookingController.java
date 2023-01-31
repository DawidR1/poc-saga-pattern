package pl.doctorappointmentbookingservice.appointment.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
class AppointmentBookingController {

 private final AppointmentService appointmentService;

 @PostMapping()
 public void createAppointment(@RequestBody AppointmentBookingDto dto) {
  appointmentService.bookAppointment(dto);
 }

 @GetMapping("")
 public List<AppointmentBooking> getAppointment() {
  return appointmentService.getAppointment();
 }
}
