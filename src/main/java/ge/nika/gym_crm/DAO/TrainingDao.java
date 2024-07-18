package ge.nika.gym_crm.DAO;

import ge.nika.gym_crm.entities.Training;

public interface TrainingDao extends Dao<Training> {
    Training select(String id);
}
