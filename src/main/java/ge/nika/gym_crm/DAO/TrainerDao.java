package ge.nika.gym_crm.DAO;

import ge.nika.gym_crm.entities.Trainer;

public interface TrainerDao extends Dao<Trainer, Integer> {
    void update(Integer id, Trainer newTrainer);
}
