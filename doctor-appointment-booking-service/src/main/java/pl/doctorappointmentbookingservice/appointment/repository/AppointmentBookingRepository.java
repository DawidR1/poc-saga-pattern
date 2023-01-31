package pl.doctorappointmentbookingservice.appointment.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.doctorappointmentbookingservice.appointment.domain.AppointmentBooking;

@Repository

public interface AppointmentBookingRepository extends JpaRepository<AppointmentBooking, UUID> {

}
