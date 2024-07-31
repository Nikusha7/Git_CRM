package ge.nika.gym_crm.services;

import ge.nika.gym_crm.entities.Training;

public interface TrainingService {
    void create(Training training);
    Training select(Integer trainingId);

}
