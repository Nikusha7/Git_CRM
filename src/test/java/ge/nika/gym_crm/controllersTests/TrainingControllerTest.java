package ge.nika.gym_crm.controllersTests;

import ge.nika.gym_crm.controllers.TrainingController;
import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
public class TrainingControllerTest {
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TrainingController trainingController;
    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    TrainerRepository trainerRepository;

    @Test
    void testCreatingTraining() {
        Trainee trainee = registerTrainee();
        Trainer trainer = registerTrainer();

        ResponseEntity<?> trainingResponseEntity = trainingController.registerTraining(trainee.getUser().getUserName(), trainer.getUser().getUserName(),
                "NEW TRAINING", new Date(), 2);

        Training training = (Training) trainingResponseEntity.getBody();
        System.out.println(training);
    }


    private Trainee registerTrainee() {
        User traineeUser = new User("Tom", "Aspinal", true);
        traineeUser.setUserName("Tom.Aspinal");
        traineeUser.setPassword("1234567890");
        Trainee trainee = new Trainee(LocalDate.of(1995, 5, 15), "Ireland");
        trainee.setUser(traineeUser);

        return traineeRepository.save(trainee);
    }


    private Trainer registerTrainer() {
        User trainerUser = new User("Lucas", "Holmes", true);
        trainerUser.setUserName("Lucas.Holmes");
        trainerUser.setPassword("1234567890");
        Trainer trainer = new Trainer(new TrainingType(3, TrainingTypeNames.FLEXIBILITY));
        trainer.setUser(trainerUser);

        return trainerRepository.save(trainer);
    }
}
