package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainer;

public interface TrainerService {
    Trainer create(TrainerDTO trainerDTO);
    Trainer select(Integer userId);
    void update(Integer userId, Trainer newTrainer);


}
