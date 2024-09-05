package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingRepository
        extends JpaRepository<Training, Integer> {
    List<Training> findByTrainee_Id(@Param("trainee_id") Integer traineeId);
    List<Training> findByTrainer_Id(@Param("trainer_id") Integer trainerId);
}

