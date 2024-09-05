package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TraineeService {
    Trainee create(TraineeDTO traineeDTO);

    Trainee select(String userName);
    List<Training> getTraineesTrainings(String username);

    Trainee login(String username, String password);

    Trainee update(String username, TraineeDTO newTraineeDTO);

    Trainee updateTrainersList(String username, List<Trainer> trainerList);

    void delete(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    void activateDeactivate(String username, Boolean isActive);

}
