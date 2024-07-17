package ge.nika.gym_crm.services;

import ge.nika.gym_crm.entities.Trainer;

public interface TrainerService {
    void create(Trainer trainer);

    void update(Integer userId, Trainer newTrainer);

    Trainer select(Integer userId);
}
