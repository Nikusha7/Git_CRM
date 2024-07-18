package ge.nika.gym_crm.DAO;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;

public interface TraineeDao extends Dao<Trainee> {
    void update(Integer id, Trainee newTrainee);
    void delete(Integer id);
    Trainee select(Integer traineeId);
}
