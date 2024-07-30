package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TraineeRepository
        extends JpaRepository<Trainee, Integer> {
    Optional<Trainee> findByUser_UserName(String userName);
    Optional<Trainee> findByUserId(Integer userId);

}
