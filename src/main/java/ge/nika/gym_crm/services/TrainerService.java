package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainer;

public interface TrainerService {
    Trainer create(TrainerDTO trainerDTO);
    Trainer select(String username);
    Trainer update(Integer userId, Trainer newTrainer);


}
