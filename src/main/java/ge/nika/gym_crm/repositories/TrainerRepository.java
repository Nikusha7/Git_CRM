package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository
        extends JpaRepository<Trainer, Integer> {
}
