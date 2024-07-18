package ge.nika.gym_crm.storagesTests;

import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StorageTrainerTest {

    @Autowired
    private StorageTrainer storageTrainer;

    @Test
    public void testPostConstructInitialization() {
        Map<Integer, Trainer> trainerStorage = storageTrainer.getTrainerStorage();
        assertNotNull(trainerStorage);
        assertFalse(trainerStorage.isEmpty());
    }
    @Test
    public void testStorageSize() {
        Map<Integer, Trainer> trainerStorage = storageTrainer.getTrainerStorage();
        assertEquals(5, trainerStorage.size());
    }

    @Test
    public void testUniqueUsernames() {
        Map<Integer, Trainer> trainerStorage = storageTrainer.getTrainerStorage();
        trainerStorage.values().forEach(trainer -> {
            assertTrue(isUsernameUnique(trainer.getUserName(), trainerStorage));
        });
    }

    @Test
    public void testPasswordGeneration() {
        Map<Integer, Trainer> trainerStorage = storageTrainer.getTrainerStorage();
        trainerStorage.values().forEach(trainer -> {
            assertNotNull(trainer.getPassword());
            assertEquals(10, trainer.getPassword().length());
        });
    }

    @Test
    public void testCorrectDataReadFromCSV() {
        Map<Integer, Trainer> trainerStorage = storageTrainer.getTrainerStorage();

        // Assuming the expected data is known beforehand for comparison
        assertTrainerData(trainerStorage.get(1), 1, "John", "Doe", true, "Strength", TrainingType.CARDIO);
        assertTrainerData(trainerStorage.get(2), 2, "Jane", "Smith", true, "Endurance", TrainingType.STRENGTH);
        assertTrainerData(trainerStorage.get(3), 3, "Bob", "Johnson", false, "Fitness", TrainingType.FLEXIBILITY);
        assertTrainerData(trainerStorage.get(4), 4, "Emily", "Davis", true, "Yoga", TrainingType.BALANCE);
        assertTrainerData(trainerStorage.get(5), 5, "John", "Doe", true, "Cardio", TrainingType.CARDIO);
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

    private boolean isUsernameUnique(String username, Map<Integer, Trainer> trainerStorage) {
        long count = trainerStorage.values().stream()
                .filter(trainer -> trainer.getUserName().equals(username))
                .count();

        return count == 1;
    }

}
