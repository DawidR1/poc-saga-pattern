package pl.appointmentbookingservice.appointment.statemachine.actions;


import static pl.appointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.appointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.appointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.appointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.appointmentbookingservice.appointment.mq.QueueConf;
import pl.appointmentbookingservice.appointment.mq.messages.ValMedicalPackageRequest;
import pl.appointmentbookingservice.appointment.repository.AppointmentBookingRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class ValidateMedicalPackageAction implements Action<AppointmentBookingStatus, AppointmentBookingEvent> {

  private final RabbitTemplate rabbitTemplate;
  private final AppointmentBookingRepository appointmentBookingRepository;

  @SneakyThrows
  @Transactional
  @Override
  public void execute(StateContext<AppointmentBookingStatus, AppointmentBookingEvent> context) {
    String id = (String) context.getMessage().getHeaders().get(APPOINTMENT_BOOKING_ID);
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(UUID.fromString(id)).orElseThrow();
    String req = ValMedicalPackageRequest.toDto(appointmentBooking);
    rabbitTemplate.convertAndSend(
        QueueConf.APPOINTMENT_BOOKING_EXCHANGE,
        QueueConf.K_VAL_MEDICAL_PACKAGE,
        req);
    log.info("Service: appointment-booking-service; validate medical package; producer ; " + req);
  }
}
