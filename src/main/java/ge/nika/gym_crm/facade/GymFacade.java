package ge.nika.gym_crm.facade;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.services.TraineeService;
import ge.nika.gym_crm.services.TrainerService;
import ge.nika.gym_crm.services.TrainingService;
import org.springframework.stereotype.Component;

@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    //    @Autowired (As I know if we have 1 constructor, annotation isn't a 'must')
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    // Trainee related methods
    public void createTrainee(Trainee trainee) {
        traineeService.create(trainee);
    }

    public void updateTrainee(Integer userId, Trainee newTrainee) {
        traineeService.update(userId, newTrainee);
    }

    public void deleteTrainee(Integer userId) {
        traineeService.delete(userId);
    }

    public Trainee getTrainee(Integer userId) {
        return traineeService.select(userId);
    }

    // Trainer related methods
    public void createTrainer(Trainer trainer) {
        trainerService.create(trainer);
    }

    public void updateTrainer(Integer userId, Trainer newTrainer) {
        trainerService.update(userId, newTrainer);
    }

    public Trainer getTrainer(Integer userId) {
        return trainerService.select(userId);
    }

    // Training related methods
    public void createTraining(Training training) {
        trainingService.create(training);
    }

    public Training getTraining(Integer trainerId, Integer traineeId) {
        return trainingService.select(trainerId, traineeId);
    }

}
