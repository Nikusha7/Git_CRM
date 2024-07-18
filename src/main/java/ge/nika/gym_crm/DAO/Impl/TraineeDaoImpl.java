package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TraineeDao;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.storages.StorageTrainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private static final Logger log = LoggerFactory.getLogger(TraineeDaoImpl.class);
    @Autowired
    private StorageTrainee storageTrainee;

    @Override
    public void create(Trainee trainee) {
        storageTrainee.getTraineeStorage().put(trainee.getUserId(), trainee);
    }

    @Override
    public Trainee select(Integer id) {
        return storageTrainee.getTraineeStorage().get(id);
    }

    @Override
    public void update(Integer id, Trainee newTrainee) {
        storageTrainee.getTraineeStorage().put(id, newTrainee);
    }

    @Override
    public void delete(Integer id) {
        storageTrainee.getTraineeStorage().remove(id);
    }

}
