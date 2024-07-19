package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TrainerDao;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.storages.StorageTrainee;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private final StorageTrainer storageTrainer;

    public TrainerDaoImpl(StorageTrainer storageTrainer) {
        this.storageTrainer = storageTrainer;
    }

    @Override
    public void create(Trainer trainer) {
        storageTrainer.getTrainerStorage().put(trainer.getUserId(), trainer);
    }

    @Override
    public Trainer select(Integer id) {
        return storageTrainer.getTrainerStorage().get(id);
    }

    @Override
    public void update(Integer id, Trainer newTrainer) {
        storageTrainer.getTrainerStorage().put(id, newTrainer);
    }

}