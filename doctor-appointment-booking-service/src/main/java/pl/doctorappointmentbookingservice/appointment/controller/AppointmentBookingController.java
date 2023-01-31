package pl.doctorappointmentbookingservice.appointment.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.doctorappointmentbookingservice.appointment.dto.AppointmentBookingDto;
import pl.doctorappointmentbookingservice.appointment.service.AppointmentService;


@RestController
@RequiredArgsConstructor
@RequestMapping("appointment")
class AppointmentBookingController {

 private final AppointmentService appointmentService;

 @PostMapping()
 public void createAppointment(AppointmentBookingDto dto) {

//  throw new RuntimeException();
  appointmentService.bookAppointment(dto);
 }
}
