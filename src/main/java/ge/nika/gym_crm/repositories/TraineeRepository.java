package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository
        extends JpaRepository<Trainee, Integer> {
}
