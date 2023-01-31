package pl.doctorappointmentbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "pl.doctorappointmentbookingservice")
//@ComponentScan({"pl.doctorappointmentbookingservice"})
//@EntityScan("pl.doctorappointmentbookingservice")
//@EnableJpaRepositories("pl.doctorappointmentbookingservice")
public class DoctorAppointmentBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorAppointmentBookingServiceApplication.class, args);
	}

}
