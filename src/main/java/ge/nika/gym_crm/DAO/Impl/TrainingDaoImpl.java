package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TrainingDao;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.storages.StorageTraining;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private final StorageTraining storageTraining;

    public TrainingDaoImpl(StorageTraining storageTraining) {
        this.storageTraining = storageTraining;
    }

    @Override
    public void create(Training training) {
        storageTraining.getTrainingStorage().put(training.getTrainerId() + "" + training.getTraineeId(), training);
    }

    @Override
    public Training select(String id) {
        return storageTraining.getTrainingStorage().get(id);
    }
}
