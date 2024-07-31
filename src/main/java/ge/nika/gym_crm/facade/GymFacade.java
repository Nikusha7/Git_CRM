package ge.nika.gym_crm.facade;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.TrainerDTO;
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
    public void createTrainee(TraineeDTO traineeDTO) {
        traineeService.create(traineeDTO);
    }

    public Trainee getTrainee(String username) {
        return traineeService.select(username);
    }

    public void updateTrainee(Integer id, Trainee newTrainee) {
        traineeService.update(id, newTrainee);
    }

    public void changePasswordTrainee(String username, String password) {
        traineeService.changePassword(username, password);
    }

    public void activeDeactivateTrainee(String username, Boolean isActive) {
        traineeService.activateDeactivate(username, isActive);
    }

    public void deleteTrainee(String username) {
        traineeService.delete(username);
    }

    // Trainer related methods
    public void createTrainer(TrainerDTO trainerDTO) {
        trainerService.create(trainerDTO);
    }

    public void updateTrainer(Integer userId, Trainer newTrainer) {
        trainerService.update(userId, newTrainer);
    }

    public Trainer getTrainer(String username) {
        return trainerService.select(username);
    }

    public void changePasswordTrainer(String username, String password) {
        trainerService.changePassword(username, password);
    }

    public void activeDeactivateTrainer(String username, Boolean isActive) {
        trainerService.activateDeactivate(username, isActive);
    }

    // Training related methods
    public void createTraining(Training training) {
        trainingService.create(training);
    }

    public Training getTraining(Integer id) {
        return trainingService.select(id);
    }

}
