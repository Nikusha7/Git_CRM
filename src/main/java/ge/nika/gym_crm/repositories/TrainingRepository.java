package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository
        extends JpaRepository<Training, Integer> {
}
