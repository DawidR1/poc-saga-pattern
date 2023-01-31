package pl.doctorappointmentbookingservice.appointment.service;


import static pl.doctorappointmentbookingservice.appointment.statemachine.AppointmentBookingSMConf.APPOINTMENT_BOOKING_ID;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingEvent;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBookingStatus;
import pl.doctorappointmentbookingservice.appointment.dto.AppointmentBookingDto;
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

  private AppointmentBooking buildAppointmentBooking(AppointmentBookingDto dto) {
    return AppointmentBooking.builder()
        .fromDate(dto.getFrom())
        .toDate(dto.getTo())
        .doctorId(dto.getDoctorId())
        .patientId(dto.getPatientId())
        .status(AppointmentBookingStatus.NEW)
        .build();
  }

  private void resetMachineState(AppointmentBooking build, StateMachine<AppointmentBookingStatus, AppointmentBookingEvent> stateMachine) {
    stateMachine.stop();
    stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachneAccessor -> {
      stateMachneAccessor.addStateMachineInterceptor(interceptor);
      stateMachneAccessor.resetStateMachineReactively(new DefaultStateMachineContext<>(build.getStatus(), null, null, null))
          .subscribe();
    });
    stateMachine.start();
  }

  public void processValOfMedicalPackage(ValMedicalPackageResp response) {
    AppointmentBooking appointmentBooking = appointmentBookingRepository.findById(response.getAppointmentId()).orElseThrow();

    if(response.isValid()) {
//        stateMachineFactory.getStateMachine().
    }
  }
}
