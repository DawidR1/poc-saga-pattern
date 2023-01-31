package pl.doctorappointmentbookingservice.appointment.statemachine.actions;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.dto.AppointmentBookingDto;
import pl.doctorappointmentbookingservice.appointment.mq.QueuesConf;
import pl.doctorappointmentbookingservice.appointment.mq.messages.ValMedicalPackageRequest;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;

@RequiredArgsConstructor
@Component
public class ValidateMedicalPackageAction implements Action<AppointmentBookingStatus, AppointmentBookingEvent> {

  private final RabbitTemplate rabbitTemplate;
  private final AppointmentBookingRepository appointmentBookingRepository;

  @SneakyThrows
  @Transactional
  @Override
  public void execute(StateContext<AppointmentBookingStatus, AppointmentBookingEvent> context) {
    String id = (String) context.getMessage().getHeaders().get(APPOINTMENT_BOOKING_ID);
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(UUID.fromString(id)).orElseThrow();

    rabbitTemplate.convertAndSend(
        QueuesConf.APPOINTMENT_BOOKING_EXCHANGE,
        QueuesConf.K_VAL_MEDICAL_PACKAGE,
        ValMedicalPackageRequest.toDto(appointmentBooking));
  }
}
