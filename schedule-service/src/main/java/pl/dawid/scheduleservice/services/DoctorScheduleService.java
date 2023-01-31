package pl.dawid.scheduleservice.services;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid.scheduleservice.domain.Schedule;
import pl.dawid.scheduleservice.mq.QueueConf;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentReqDto;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentResDto;
import pl.dawid.scheduleservice.repository.ScheduleRepository;

@RequiredArgsConstructor
@Component
public class DoctorScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final RabbitTemplate rabbitTemplate;

  @SneakyThrows
  @Transactional
  public void makeAnAppointment(BookAppointmentResDto readValue) {
    boolean isScheduleBusy = scheduleRepository.existsByDoctorIdAndDateRange(
        readValue.getDoctorId(),
        readValue.getFromDate(),
        readValue.getToDate());

    if (isScheduleBusy) {
      String message = BookAppointmentReqDto
          .toBookingRejected(readValue.getAppointmentId(), "The schedule is busy")
          .toJson();
      sendMessage(message);

    } else {
      Schedule build = buildNewSchedule(readValue);
      scheduleRepository.saveAndFlush(build);

      String message = BookAppointmentReqDto
          .toBookingAccepted(readValue.getAppointmentId())
          .toJson();

      sendMessage(message);
    }
  }

  private Schedule buildNewSchedule(BookAppointmentResDto readValue) {
    return Schedule.builder()
        .patientId(readValue.getPatientId())
        .doctorId(readValue.getDoctorId())
        .fromDate(readValue.getFromDate())
        .toDate(readValue.getToDate())
        .build();
  }

  private void sendMessage(String message) {
    rabbitTemplate.convertAndSend(
        QueueConf.APPOINTMENT_BOOKING_EXCHANGE,
        QueueConf.BOOK_APPOINTMENT_RESP_KEY,
        message);
    System.out.println("appointment-booking-service, Book appointment producer: " + message);
  }
}
