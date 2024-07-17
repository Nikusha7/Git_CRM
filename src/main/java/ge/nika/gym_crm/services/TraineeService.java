package ge.nika.gym_crm.services;

import ge.nika.gym_crm.entities.Trainee;

import java.util.List;

public interface TraineeService {
    void create(Trainee trainee);
    void update(Integer userId, Trainee newTrainee);
    void delete(Integer userId);
    Trainee select(Integer userId);
}
