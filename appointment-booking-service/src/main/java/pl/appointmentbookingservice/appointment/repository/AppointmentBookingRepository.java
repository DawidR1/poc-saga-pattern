package pl.appointmentbookingservice.appointment.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.appointmentbookingservice.appointment.domain.AppointmentBooking;

@Repository
public interface AppointmentBookingRepository extends JpaRepository<AppointmentBooking, UUID> {

}
