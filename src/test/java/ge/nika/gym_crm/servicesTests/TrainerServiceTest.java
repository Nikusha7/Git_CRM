package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootTest
public class TrainerServiceTest {

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainerService trainerService;

    @Test
    void testCreateTrainer() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);
    }

    @Test
    void testSelectTrainee() {
        System.out.println(trainerService.select("Luke.Bor"));
    }


    @Test
    void testUpdateTrainee(){
        User user = new User("Luke-UPDATED", "Bor", true);
        Trainer trainer = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), user);

        trainerService.update(1, trainer);
    }

}
