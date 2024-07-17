package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TraineeDao;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.storages.StorageTrainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDaoImpl.class);
    private final StorageTrainee storageTrainee;

    public TraineeDaoImpl(StorageTrainee storageTrainee) {
        this.storageTrainee = storageTrainee;
    }

    @Override
    public void create(Trainee trainee) {
        Map<Integer, Trainee> traineeMap = storageTrainee.getTraineeStorage();
        traineeMap.put(trainee.getUserId(), trainee);
    }

    @Override
    public Trainee select(Integer id) {
        Map<Integer, Trainee> traineeMap = storageTrainee.getTraineeStorage();
        return traineeMap.get(id);
    }

    @Override
    public void update(Integer id, Trainee newTrainee) {
        Map<Integer, Trainee> traineeMap = storageTrainee.getTraineeStorage();
        traineeMap.put(id, newTrainee);
    }

    @Override
    public void delete(Integer id) {
        Map<Integer, Trainee> traineeMap = storageTrainee.getTraineeStorage();
        traineeMap.remove(id);
    }

}
