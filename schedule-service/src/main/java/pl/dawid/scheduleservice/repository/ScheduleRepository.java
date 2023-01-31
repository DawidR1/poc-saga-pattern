package pl.dawid.scheduleservice.repository;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.dawid.scheduleservice.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {


  @Query("select case when (count(s) > 0) then true else false end from Schedule s "
      + "where s.doctorId =:doctorId "
      + "and s.fromDate between :fromDate and :fromDate "
      + "or s.toDate between :fromDate and :toDate")
  boolean existsByDoctorIdAndDateRange(UUID doctorId, LocalDate fromDate, LocalDate toDate);
}
