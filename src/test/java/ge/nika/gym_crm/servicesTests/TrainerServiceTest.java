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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class TrainerServiceTest {

    @Autowired
    private TrainerService trainerService;

    @Test
    void testCreateTrainer() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        Trainer result = trainerService.create(trainerDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Luke.Bor", result.getUser().getUserName());

        TrainerDTO trainerDTO1 = new TrainerDTO("Bob", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        result = trainerService.create(trainerDTO1);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Bob.Bor", result.getUser().getUserName());
    }

    @Test
    void testSelectTrainee() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);

        Trainer result = trainerService.select("john.doe");

        assertNotNull(result);
        assertEquals("Luke.Bor", result.getUser().getUserName());

    }

    @Test
    void select_ShouldReturnNullWhenTrainerNotFound() {
        Trainer result = trainerService.select("Unknown.Unknown");
        assertNull(result);
    }

    @Test
    void testUpdateTrainee() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        Trainer existingTrainer = trainerService.create(trainerDTO);

        User user = new User("Luke-Updated", "Bor", true);
        Trainer updatedTrainer = new Trainer(new TrainingType(2, TrainingTypeNames.STRENGTH), user);


        Trainer result = trainerService.update(existingTrainer.getId(), updatedTrainer);

        assertNotNull(result);
        assertEquals("Luke-Updated.Bor", result.getUser().getUserName());
        assertEquals(TrainingTypeNames.STRENGTH, result.getSpecialization().getTrainingType());
    }

    @Test
    void update_ShouldThrowExceptionWhenTrainerNotFound() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);


        User user = new User("Luke-Updated", "Bor", true);
        Trainer updatedTrainer = new Trainer(new TrainingType(2, TrainingTypeNames.STRENGTH), user);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.update(11111, updatedTrainer);
        });

        assertEquals("Trainer not found", thrown.getMessage());
    }

    @Test
    void testChangePassword() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);

        trainerService.changePassword("Luke.Bor", "newpasswor");

        assertEquals("newpasswor", trainerService.select("Luke.Bor").getUser().getPassword());

    }

    @Test
    void changePassword_ShouldThrowExceptionWhenPasswordInvalid() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.changePassword("Luke.Bor", "short");
        });

        assertEquals("Password must be at least 10 characters long", thrown.getMessage());
    }


    @Test
    void activateDeactivate_ShouldUpdateActiveStatus() {
        TrainerDTO trainerDTO = new TrainerDTO("Luke", "Bor", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        trainerService.create(trainerDTO);

        trainerService.activateDeactivate("Luke.Bor", false);

        assertFalse(trainerService.select("Luke.Bor").getUser().getIsActive());
    }

    @Test
    void activateDeactivate_ShouldThrowExceptionWhenTrainerNotFound() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.activateDeactivate("unknown.user", true);
        });

        assertEquals("Trainer not found", thrown.getMessage());
    }


}
