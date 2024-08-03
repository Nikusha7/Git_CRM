package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.services.TraineeService;
import ge.nika.gym_crm.services.TrainerService;
import ge.nika.gym_crm.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class TrainingServiceTest {

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TraineeService traineeService;

    @Test
    void testCreateTraining() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee trainee = traineeService.create(traineeDTO);

        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        Trainer trainer = trainerService.create(trainerDTO);

        Training training = new Training(trainer, trainee, "first training name", new TrainingType(1, TrainingTypeNames.CARDIO),
                Date.valueOf(LocalDate.now()), 55);
        Training savedTraining = trainingService.create(training);

        assertNotNull(savedTraining);
    }

    @Test
    void testSelectTraining() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee trainee = traineeService.create(traineeDTO);

        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        Trainer trainer = trainerService.create(trainerDTO);

        Training training = new Training(trainer, trainee, "first training name", new TrainingType(1, TrainingTypeNames.CARDIO),
                Date.valueOf(LocalDate.now()), 55);
        Training savedTraining = trainingService.create(training);

        Training result = trainingService.select(savedTraining.getId());

        assertNotNull(result);
        assertEquals("Compare selected training: ", savedTraining.getId(), result.getId());
    }

    @Test
    void testSelectNullTraining() {
        Training result = trainingService.select(111111);
        assertNull(result);
    }
}


