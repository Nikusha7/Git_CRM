package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DAO.Impl.TraineeDaoImpl;
import ge.nika.gym_crm.DAO.Impl.TrainerDaoImpl;
import ge.nika.gym_crm.DAO.TrainerDao;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.services.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeDaoImpl traineeDao;

    public TraineeServiceImpl(TraineeDaoImpl traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Override
    public void create(Trainee trainee) {
        traineeDao.create(trainee);
        logger.info("Trainee: "+trainee+", Has been created");
    }

    @Override
    public void update(Integer userId, Trainee newTrainee) {
        if (traineeDao.select(userId) != null) {
            // Update logic here if needed
            traineeDao.update(userId, newTrainee);
            logger.info("Trainee with id: " + userId + ", has been updated");
        } else {
            logger.warn("Trainee with id: " + userId + " not found for update");
        }
    }

    @Override
    public void delete(Integer userId) {
        traineeDao.delete(userId);
        logger.info("Trainee with id: " + userId + ", has been deleted");
    }

    @Override
    public Trainee select(Integer userId) {
        Trainee trainee = traineeDao.select(userId);
        if (trainee != null) {
            logger.info("Trainee with id: " + userId + ", has been selected/retrieved");
        } else {
            logger.warn("Trainee with id: " + userId + " not found");
        }
        return trainee;
    }

}