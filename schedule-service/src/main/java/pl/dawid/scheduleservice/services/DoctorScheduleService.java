package pl.dawid.scheduleservice.services;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid.scheduleservice.domain.Schedule;
import pl.dawid.scheduleservice.mq.QueueConf;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentResult;
import pl.dawid.scheduleservice.mq.messages.BookAppointmentReq;
import pl.dawid.scheduleservice.repository.ScheduleRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class DoctorScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final RabbitTemplate rabbitTemplate;

  @SneakyThrows
  @Transactional
  public void makeAnAppointment(BookAppointmentReq readValue) {
    boolean isScheduleBusy = scheduleRepository.existsByDoctorIdAndDateRange(
        readValue.getDoctorId(),
        readValue.getFromDate(),
        readValue.getToDate());

    if (isScheduleBusy) {
      String message = BookAppointmentResult
          .toBookingRejected(readValue.getAppointmentId(), "The schedule is busy")
          .toJson();

      sendMessage(message);
      log.info("Service: schedule-service; booking appointment ;producer; not booked: " + message);

    } else {
      Schedule schedule = buildNewSchedule(readValue);
      scheduleRepository.saveAndFlush(schedule);

      String message = BookAppointmentResult
          .toBookingAccepted(readValue.getAppointmentId())
          .toJson();

      sendMessage(message);
      log.info("Service: schedule-service; booking appointment ;producer; booked: " + message);
    }
  }

  private Schedule buildNewSchedule(BookAppointmentReq readValue) {
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
  }
}
