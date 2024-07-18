package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DAO.Impl.TrainingDaoImpl;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.services.TrainingService;
import ge.nika.gym_crm.storages.StorageTraining;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);
    @Autowired
    private TrainingDaoImpl trainingDao;
    @Autowired
    private StorageTraining storageTraining;
    @Autowired
    private TraineeServiceImpl traineeService;
    @Autowired
    private TrainerServiceImpl trainerService;

    @Override
    public void create(Training training) {
        Map<String, Training> trainings = storageTraining.getTrainingStorage();
        if (traineeService.select(training.getTraineeId()) != null || trainerService.select(training.getTrainerId()) != null) {
            log.warn("Training can not be created! because Trainee with id:" + training.getTraineeId() + ", " +
                    "or Trainer with id:" + training.getTrainerId() + ", does not exists!");
        } else {
            trainingDao.create(training);
            log.info("Training has been successfully created!");
        }
    }

    @Override
    public Training select(Integer trainerId, Integer traineeId) {
        Training training = trainingDao.select(trainerId + "" + traineeId);
        if (training != null) {
            log.info("Successfully selected: " + training);
            return training;
        } else {
            log.warn("Training does not exists with TrainerId:" + trainerId + ", and TraineeId:" + traineeId);
        }
        return null;
    }
}
