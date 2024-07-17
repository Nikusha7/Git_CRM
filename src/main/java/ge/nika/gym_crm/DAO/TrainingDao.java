package ge.nika.gym_crm.DAO;

import ge.nika.gym_crm.entities.Training;

public interface TrainingDao extends Dao<Training, Integer> {
    Training select(Integer trainerId, Integer traineeId);
}
