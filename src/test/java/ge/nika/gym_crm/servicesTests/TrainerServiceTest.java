package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        TrainerDTO trainerDTO1 = new TrainerDTO("Bob", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO1);
    }

    @Test
    void testSelectTrainee() {
        System.out.println(trainerService.select("Luke.Bor"));
    }


    @Test
    void testUpdateTrainee() {
        User user = new User("Luke-UPDATED", "Bor", true);
        Trainer trainer = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), user);

        trainerService.update(1, trainer);
    }

    @Test
    void testChangePassword() {
        trainerService.changePassword("Luke-UPDATED.Bor", "newpasswor");
    }

    @Test
    void testActivateDeactivate() {
        trainerService.activateDeactivate("Luke-UPDATED.Bor", false);
    }

}
