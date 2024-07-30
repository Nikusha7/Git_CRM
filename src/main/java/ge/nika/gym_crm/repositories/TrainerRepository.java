package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository
        extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUser_UserName(String userName);
    Optional<Trainer> findByUserId(Integer userId);
}
