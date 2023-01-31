package pl.doctorappointmentbookingservice.appointment.service;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
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
import pl.doctorappointmentbookingservice.appointment.mq.messages.BookAppointmentResDto;
import pl.doctorappointmentbookingservice.appointment.mq.messages.ValMedicalPackageResp;
import pl.doctorappointmentbookingservice.appointment.repository.AppointmentBookingRepository;
import pl.doctorappointmentbookingservice.appointment.statemachine.StateInterceptor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AppointmentService {

  private final StateMachineFactory<AppointmentBookingStatus, AppointmentBookingEvent> stateMachineFactory;
  private final AppointmentBookingRepository appointmentBookingRepository;
  private final StateInterceptor interceptor;

  @Transactional
  public void bookAppointment(AppointmentBookingDto dto) {

    AppointmentBooking build = buildAppointmentBooking(dto);

    AppointmentBooking appointmentBooking = appointmentBookingRepository.saveAndFlush(build);

    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(appointmentBooking.getId());

    resetMachineState(build, stateMachine);

    Message<AppointmentBookingEvent> message = MessageBuilder
        .withPayload(AppointmentBookingEvent.VALIDATE_MEDICAL_PACKAGE)
        .setHeader(APPOINTMENT_BOOKING_ID, build.getId().toString())
        .build();

    stateMachine.sendEvent(Mono.just(message)).doOnError(throwable -> {
      System.out.println(throwable);
    }).subscribe();
//    stateMachine.sendEvent(message);
  }

  @Transactional
  public void processBookingAppointmentResult(BookAppointmentResDto dto) {
    StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
        = stateMachineFactory.getStateMachine(dto.getAppointmentId());
    if(dto.isBooked()) {
      AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(dto.getAppointmentId()).orElseThrow();
      resetMachineState(appointmentBooking, stateMachine);
      Message<AppointmentBookingEvent> message = getBooked(appointmentBooking);
      stateMachine.sendEvent(Mono.just(message))
          .doOnError(processError(AppointmentBookingEvent.APPROVE_APPOINTMENT))
          .subscribe();
    } else {

    }
  }

  private Message<AppointmentBookingEvent> getBooked(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.APPROVE_APPOINTMENT)
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
    stateMachine.stop();
    stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachneAccessor -> {
      stateMachneAccessor.addStateMachineInterceptor(interceptor);
      stateMachneAccessor.resetStateMachineReactively(new DefaultStateMachineContext<>(build.getStatus(), null, null, null))
          .subscribe();
    });
    stateMachine.start();
  }

  @Transactional
  public void processValOfMedicalPackageResult(ValMedicalPackageResp response) {
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(response.getAppointmentId()).orElseThrow();
    if (response.isValid()) {
      StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine
          = stateMachineFactory.getStateMachine(appointmentBooking.getId());
      resetMachineState(appointmentBooking, stateMachine);
      Message<AppointmentBookingEvent> tMessageBuilder = getPackageApprovedMessage(appointmentBooking);
      stateMachine.sendEvent(Mono.just(tMessageBuilder))
          .doOnError(processError(AppointmentBookingEvent.APPROVE_MEDICAL_PACKAGE))
          .subscribe();


      Message<AppointmentBookingEvent> message = getBookAppointmentMessage(appointmentBooking);
      stateMachine.sendEvent(Mono.just(message))
          .doOnError(processError(AppointmentBookingEvent.BOOK_APPOINTMENT))
          .subscribe();

    } else {

    }
  }

  private Message<AppointmentBookingEvent> getBookAppointmentMessage(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.BOOK_APPOINTMENT)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

  private Consumer<Throwable> processError(AppointmentBookingEvent valMedicalPackageApproved) {
    return x -> {
      System.out.println("Error, message: " + valMedicalPackageApproved);
      System.out.println(x.getMessage());
    };
  }

  private Message<AppointmentBookingEvent> getPackageApprovedMessage(AppointmentBooking appointmentBooking) {
    return MessageBuilder
        .withPayload(AppointmentBookingEvent.APPROVE_MEDICAL_PACKAGE)
        .setHeader(APPOINTMENT_BOOKING_ID, appointmentBooking.getId().toString())
        .build();
  }

  public List<AppointmentBooking> getAppointment() {
    return appointmentBookingRepository.findAll();
  }
}
