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
//   TODO: use trainee service methods

    // Trainer related methods
//   TODO: use trainer service methods
    // Training related methods
//   TODO: use training service methods


}
