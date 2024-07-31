package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.services.TraineeService;
import ge.nika.gym_crm.services.TrainerService;
import ge.nika.gym_crm.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootTest
public class TrainingServiceTest {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @Test
    void testCrateTraining() {
        Trainer trainer = trainerService.select("Bob.Bor");
        Trainee trainee = traineeService.select("Nick.Bonny");

        Training training = new Training(trainer, trainee, "first training name", new TrainingType(1, TrainingTypeNames.CARDIO),
                Date.valueOf(LocalDate.now()), 55);
        trainingService.create(training);
    }

    @Test
    void testSelectTraining(){
        System.out.println(trainingService.select(1));
    }
}
