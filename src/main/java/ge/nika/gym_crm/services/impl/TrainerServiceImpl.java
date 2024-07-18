package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DAO.Impl.TrainerDaoImpl;
import ge.nika.gym_crm.DAO.TrainerDao;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.services.TrainerService;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private StorageTrainer storageTrainer;

    @Override
    public void create(Trainer trainer) {
        trainer.setPassword(generatePassword());
        trainer.setUserName(generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainerDao.create(trainer);
        log.info("Trainer: " + trainer + ", has been created");
    }

    @Override
    public void update(Integer userId, Trainer newTrainer) {
        if (trainerDao.select(userId) != null) {
            newTrainer.setPassword(generatePassword());
            newTrainer.setUserName(generateUniqueUsername(newTrainer.getFirstName(), newTrainer.getLastName()));
            trainerDao.update(userId, newTrainer);
            log.info("Trainer with id: " + userId + ", has been updated");
        } else {
            log.warn("Trainer with id: " + userId + " not found for update");
        }
    }

    @Override
    public Trainer select(Integer userId) {
        Trainer trainer = trainerDao.select(userId);
        if (trainer != null) {
            log.info("Trainer with id: " + userId + ", has been selected/retrieved");
            return trainer;
        } else {
            log.warn("Trainer with id: " + userId + " not found");
        }
        return null;
    }

    public String generatePassword() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> Character.isLetterOrDigit(i))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateUniqueUsername(String firstName, String lastName) {
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
