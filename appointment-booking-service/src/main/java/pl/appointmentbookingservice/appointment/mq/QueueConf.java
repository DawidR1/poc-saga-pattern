package pl.appointmentbookingservice.appointment.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConf {

  public static final String VALIDATE_MEDICAL_PACKAGE_QUEUE = "q.validate-medical-package-queue";
  public static final String VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE = "q.validate-medical-package-resp-queue";

  public static final String BOOK_APPOINTMENT_QUEUE = "q.book-appointment-queue";
  public static final String BOOK_APPOINTMENT_RESP_QUEUE = "q.book-appointment-resp-queue";

  public static final String APPOINTMENT_BOOKING_EXCHANGE = "ex.appointment-booking";
  public static final String BOOK_APPOINTMENT_KEY = "book_appointment_key";
  public static final String BOOK_APPOINTMENT_RESP_KEY = "book_appointment_resp_key";

  public static final String K_VAL_MEDICAL_PACKAGE_RESP = "val-medical-package-resp";
  public static final String K_VAL_MEDICAL_PACKAGE = "val-medical-package";

  @Bean
  public DirectExchange appointmentBookingExchange() {
    return new DirectExchange(APPOINTMENT_BOOKING_EXCHANGE);
  }

  @Bean
  public Queue validateMedicalPackageQueue() {
    return QueueBuilder.durable(VALIDATE_MEDICAL_PACKAGE_QUEUE)
        .build();
  }

  @Bean
  public Queue validateMedicalPackageRespQueue() {
    return QueueBuilder.durable(VALIDATE_MEDICAL_PACKAGE_RESP_QUEUE)
        .build();
  }

  @Bean
  public Queue bookAppointmentQueue() {
    return QueueBuilder.durable(BOOK_APPOINTMENT_QUEUE)
        .build();
  }

  @Bean
  public Queue bookAppointmentRespQueue() {
    return QueueBuilder.durable(BOOK_APPOINTMENT_RESP_QUEUE)
        .build();
  }

  @Bean
  public Binding validateMedicalPackageQueue(
      @Qualifier("appointmentBookingExchange") DirectExchange appointmentBookingExchange,
      @Qualifier("validateMedicalPackageQueue") Queue validateMedicalPackageQueue) {
    return BindingBuilder
        .bind(validateMedicalPackageQueue)
        .to(appointmentBookingExchange)
        .with(K_VAL_MEDICAL_PACKAGE);
  }

  @Bean
  public Binding validateMedicalPackageRespQueue(
      @Qualifier("appointmentBookingExchange") DirectExchange appointmentBookingExchange,
      @Qualifier("validateMedicalPackageRespQueue") Queue validateMedicalPackageRespQueue) {
    return BindingBuilder
        .bind(validateMedicalPackageRespQueue)
        .to(appointmentBookingExchange)
        .with(K_VAL_MEDICAL_PACKAGE_RESP);
  }

  @Bean
  public Binding bookAppointmentRespQueue(
      @Qualifier("appointmentBookingExchange") DirectExchange appointmentBookingExchange,
      @Qualifier("bookAppointmentRespQueue") Queue bookAppointmentRespQueue) {
    return BindingBuilder
        .bind(bookAppointmentRespQueue)
        .to(appointmentBookingExchange)
        .with(BOOK_APPOINTMENT_RESP_KEY);
  }

  @Bean
  public Binding bookAppointmentQueue(
      @Qualifier("appointmentBookingExchange") DirectExchange appointmentBookingExchange,
      @Qualifier("bookAppointmentQueue") Queue bookAppointmentQueue) {
    return BindingBuilder
        .bind(bookAppointmentQueue)
        .to(appointmentBookingExchange)
        .with(BOOK_APPOINTMENT_KEY);
  }
}

