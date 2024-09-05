package ge.nika.gym_crm.services;

import ge.nika.gym_crm.DTO.TrainingDTO;
import ge.nika.gym_crm.entities.Training;

import java.util.Date;

public interface TrainingService {
    Training create(String traineeUsername, String trainerUsername,
                    String trainingName, Date trainingDate, Integer trainingDuration);
    Training select(Integer trainingId);

}
