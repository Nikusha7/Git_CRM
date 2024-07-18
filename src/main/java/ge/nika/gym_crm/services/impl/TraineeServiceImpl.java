package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DAO.Impl.TraineeDaoImpl;
import ge.nika.gym_crm.DAO.TraineeDao;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.services.TraineeService;
import ge.nika.gym_crm.storages.StorageTrainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private StorageTrainee storageTrainee;

    @Override
    public void create(Trainee trainee) {
        traineeDao.create(trainee);
        log.info("Trainee: " + trainee + ", Has been created");
    }

    @Override
    public void update(Integer userId, Trainee newTrainee) {
        if (traineeDao.select(userId) != null) {
            // Update logic here if needed
            traineeDao.update(userId, newTrainee);
            log.info("Trainee with id: " + userId + ", has been updated");
        } else {
            log.warn("Trainee with id: " + userId + " not found for update");
        }
    }

    @Override
    public void delete(Integer userId) {
        traineeDao.delete(userId);
        log.info("Trainee with id: " + userId + ", has been deleted");
    }

    @Override
    public Trainee select(Integer userId) {
        Trainee trainee = traineeDao.select(userId);
        if (trainee != null) {
            log.info("Trainee with id: " + userId + ", has been selected/retrieved");
            return trainee;
        } else {
            log.warn("Trainee with id: " + userId + " not found");
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
        return storageTrainee.getTraineeStorage().values().stream()
                .anyMatch(trainee -> trainee.getUserName().equals(username));
    }

}