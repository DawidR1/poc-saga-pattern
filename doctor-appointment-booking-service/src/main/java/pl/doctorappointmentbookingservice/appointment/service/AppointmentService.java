package pl.doctorappointmentbookingservice.appointment.service;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.dto.AppointmentBookingDto;
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentResult;
import pl.doctorappointmentbookingservice.appointment.mq.messages.ValMedicalPackageResult;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;
import pl.doctorappointmentbookingservice.appointment.statemachine.StateInterceptor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class AppointmentService {

  private final StateMachineFactory<AppointmentBookingStatus, AppointmentBookingEvent> stateMachineFactory;
  private final AppointmentBookingRepository appointmentBookingRepository;
  private final StateInterceptor interceptor;

  @Transactional
  public void bookAppointment(AppointmentBookingDto dto) {
    AppointmentBooking appointmentBooking = buildAppointmentBooking(dto);
    AppointmentBooking appointmentBookingSaved = appointmentBookingRepository.saveAndFlush(appointmentBooking);

    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(appointmentBookingSaved.getId());

    resetMachineState(appointmentBooking, stateMachine);

    Message<AppointmentBookingEvent> message = MessageBuilder
        .withPayload(AppointmentBookingEvent.VALIDATE_MEDICAL_PACKAGE)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();

    stateMachine.sendEvent(Mono.just(message)).subscribe(appointmentBookingStatusAppointmentBookingEventStateMachineEventResult -> {
      log.info(appointmentBookingStatusAppointmentBookingEventStateMachineEventResult.getMessage().toString());
    });
  }

  @Transactional
  public void processValOfMedicalPackageResult(ValMedicalPackageResult response) {
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(response.getAppointmentId()).orElseThrow();
    if (response.isValid()) {
      processValidPackage(appointmentBooking);
    } else {
      processInvalidPackage(appointmentBooking);
    }
  }

  @Transactional
  public void processBookingAppointmentResult(BookAppointmentResult dto) {
    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(dto.getAppointmentId());
    if (dto.isBooked()) {
      processBookedResult(dto, stateMachine);
    } else {
      processNotBookedResult(dto, stateMachine);
    }
  }

  public List<AppointmentBooking> getAppointment() {
    return appointmentBookingRepository.findAll();
  }


  private void processInvalidPackage(AppointmentBooking appointmentBooking) {
    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(appointmentBooking.getId());
    resetMachineState(appointmentBooking, stateMachine);
    Message<AppointmentBookingEvent> message = getPackageRejectedMessage(appointmentBooking);
    stateMachine.sendEvent(Mono.just(message)).subscribe();
  }

  private void processValidPackage(AppointmentBooking appointmentBooking) {
    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(appointmentBooking.getId());
    resetMachineState(appointmentBooking, stateMachine);
    Message<AppointmentBookingEvent> tMessageBuilder = getPackageApprovedMessage(appointmentBooking);
    stateMachine.sendEvent(Mono.just(tMessageBuilder)).subscribe();

    Message<AppointmentBookingEvent> message = getBookAppointmentMessage(appointmentBooking);
    stateMachine.sendEvent(Mono.just(message)).subscribe();
  }


  private void processBookedResult(BookAppointmentResult dto,
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine) {
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(dto.getAppointmentId()).orElseThrow();
    resetMachineState(appointmentBooking, stateMachine);
    Message<AppointmentBookingEvent> message = getBooked(appointmentBooking);
    stateMachine.sendEvent(Mono.just(message)).subscribe();
  }

  private void processNotBookedResult(BookAppointmentResult dto,
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine) {
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(dto.getAppointmentId()).orElseThrow();
    resetMachineState(appointmentBooking, stateMachine);
    Message<AppointmentBookingEvent> message = getNotBooked(appointmentBooking);
    stateMachine.sendEvent(Mono.just(message)).subscribe();
  }

  private Message<AppointmentBookingEvent> getBooked(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.APPROVE_APPOINTMENT)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

  private Message<AppointmentBookingEvent> getNotBooked(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.REJECT_APPOINTMENT)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

  private AppointmentBooking buildAppointmentBooking(AppointmentBookingDto dto) {
    return AppointmentBooking.builder()
        .fromDate(dto.getFrom())
        .toDate(dto.getTo())
        .doctorId(dto.getDoctorId())
        .patientId(dto.getPatientId())
        .status(AppointmentBookingStatus.NEW)
        .build();
  }

  private void resetMachineState(AppointmentBooking build,
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine) {
    stateMachine.stopReactively().subscribe();
    stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachneAccessor -> {
      stateMachneAccessor.addStateMachineInterceptor(interceptor);
      stateMachneAccessor.resetStateMachineReactively(new DefaultStateMachineContext<>(build.getStatus(), null, null, null))
          .subscribe();
    });
    stateMachine.startReactively().subscribe();
  }


  private Message<AppointmentBookingEvent> getBookAppointmentMessage(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.BOOK_APPOINTMENT)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }


  private Message<AppointmentBookingEvent> getPackageApprovedMessage(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.APPROVE_MEDICAL_PACKAGE)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

  private Message<AppointmentBookingEvent> getPackageRejectedMessage(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.REJECT_MEDICAL_PACKAGE)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

}
