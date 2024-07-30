package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TraineeService {
    Trainee create(TraineeDTO traineeDTO);
    Trainee select(String userName);
    Trainee update(Integer id, Trainee newTrainee);
    void delete(String username);
}
