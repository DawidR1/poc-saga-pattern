package pl.doctorappointmentbookingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "pl.doctorappointmentbookingservice")
public class DoctorAppointmentBookingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DoctorAppointmentBookingServiceApplication.class, args);
  }


  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .build();
  }

  @Bean
  public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setChannelTransacted(true);
    return rabbitTemplate;
  }
}
