package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;

import java.util.List;

public interface TrainerService {
    Trainer create(TrainerDTO trainerDTO);
    Trainer select(String username);
    List<Training> getTrainersTrainings(String username);
    List<Trainer> getNotAssignedTrainers(String username);
    Trainer login(String username, String password);
    Trainer update(String username, TrainerDTO newTrainerDTO);
    void changePassword(String username, String oldPassword, String newPassword);
    void activateDeactivate(String username, Boolean isActive);

}
