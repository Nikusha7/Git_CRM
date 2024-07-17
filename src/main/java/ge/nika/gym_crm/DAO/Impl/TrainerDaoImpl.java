package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TrainerDao;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private static final Logger log = LoggerFactory.getLogger(TrainerDaoImpl.class);
    private final StorageTrainer storageTrainer;

    public TrainerDaoImpl(StorageTrainer storageTrainer) {
        this.storageTrainer = storageTrainer;
    }

    @Override
    public void create(Trainer trainer) {
        Map<Integer, Trainer> trainerMap = storageTrainer.getTrainerStorage();
        trainer.setUserName(generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainerMap.put(trainer.getUserId(), trainer);
    }

    @Override
    public Trainer select(Integer id) {
        Map<Integer, Trainer> trainerMap = storageTrainer.getTrainerStorage();
        return trainerMap.get(id);
    }

    @Override
    public void update(Integer id, Trainer newTrainer) {
        Map<Integer, Trainer> trainerMap = storageTrainer.getTrainerStorage();
        trainerMap.put(id, newTrainer);
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (checkUsernameExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }

    private boolean checkUsernameExists(String username) {
        return storageTrainer.getTrainerStorage().values().stream()
                .anyMatch(trainer -> trainer.getUserName().equals(username));
    }

}