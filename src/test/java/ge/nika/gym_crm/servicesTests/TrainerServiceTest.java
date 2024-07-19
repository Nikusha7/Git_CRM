package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.services.impl.TrainerServiceImpl;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainerServiceTest {

    @Autowired
    private TrainerServiceImpl trainerService;
    @Autowired
    private StorageTrainer storageTrainer;

    @Test
    public void testSelectTrainer() {
        assertTrainerData(trainerService.select(1), 1, "John", "Doe", true, "Strength", TrainingType.CARDIO);
    }

    @Test
    public void testSelectNotExistingTrainer() {
        assertNull(trainerService.select(20));
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer("Nick", "Brown", true, "Strength", 6, TrainingType.STRENGTH);
        trainerService.create(trainer);
        assertTrainerData(trainerService.select(6), 6, "Nick", "Brown", true, "Strength", TrainingType.STRENGTH);
    }

    @Test
    public void testUpdateTrainer() {
        trainerService.update(6, new Trainer("Nick-Updated", "Brown", true, "Strength", 6, TrainingType.STRENGTH));
        assertTrainerData(trainerService.select(6), 6, "Nick-Updated", "Brown", true, "Strength", TrainingType.STRENGTH);
        storageTrainer.getTrainerStorage().remove(6);
    }

    @Test
    public void testUpdateNotExistingTrainer() {
        trainerService.update(25, new Trainer("Jane", "Smith", true, "Endurance", 2, TrainingType.STRENGTH));
        assertNull(trainerService.select(25));
    }

    @Test
    public void testGeneratePassword() {
        String password = trainerService.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generateUniqueUsername_noConflict() {
        String username = trainerService.generateUniqueUsername("Nick", "Doe");
        assertEquals("Nick.Doe", username);
    }

    @Test
    void generateUniqueUsername_withConflict() {
        String username = trainerService.generateUniqueUsername("Bob", "Johnson");
        System.out.println("new username: " + username);
        assertEquals("Bob.Johnson1", username);
    }

    private void assertTrainerData(Trainer trainer, int id, String firstName, String lastName, boolean isActive, String specialization, TrainingType trainingType) {
        assertNotNull(trainer);
        assertEquals(id, trainer.getUserId());
        assertEquals(firstName, trainer.getFirstName());
        assertEquals(lastName, trainer.getLastName());
        assertEquals(isActive, trainer.getActive());
        assertEquals(specialization, trainer.getSpecialization());
        assertEquals(trainingType, trainer.getTrainingType());
    }

}
