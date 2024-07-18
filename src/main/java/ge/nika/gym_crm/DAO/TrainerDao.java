package ge.nika.gym_crm.DAO;

import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;

public interface TrainerDao extends Dao<Trainer> {
    void update(Integer id, Trainer newTrainer);
    Trainer select(Integer trainerId);
}
