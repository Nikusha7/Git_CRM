package ge.nika.gym_crm.storagesTests;

import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.storages.StorageTraining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StorageTrainingTest {

    @Autowired
    private StorageTraining storageTraining;

    @Test
    public void testPostConstructInitialization() {
        Map<String, Training> trainingStorage = storageTraining.getTrainingStorage();
        assertNotNull(trainingStorage);
        assertFalse(trainingStorage.isEmpty()); // Ensure storage is initialized and not empty
    }

    @Test
    public void testStorageSize() {
        Map<String, Training> trainingStorage = storageTraining.getTrainingStorage();
        assertEquals(15, trainingStorage.size());
    }

    @Test
    public void testCorrectDataReadFromCSV() {
        Map<String, Training> trainingStorage = storageTraining.getTrainingStorage();

        assertTrainingData(trainingStorage.get("11"), 1, 1, "Morning Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 1), "60");
        assertTrainingData(trainingStorage.get("22"), 2, 2, "Strength Training", TrainingType.STRENGTH, LocalDate.of(2024, 7, 2), "45");
        assertTrainingData(trainingStorage.get("33"), 3, 3, "Fitness Flexibility", TrainingType.FLEXIBILITY, LocalDate.of(2024, 7, 3), "30");
        assertTrainingData(trainingStorage.get("44"), 4, 4, "Yoga Balance", TrainingType.BALANCE, LocalDate.of(2024, 7, 4), "90");
        assertTrainingData(trainingStorage.get("55"), 5, 5, "Evening Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 5), "60");
        assertTrainingData(trainingStorage.get("61"), 6, 1, "Morning Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 6), "60");
        assertTrainingData(trainingStorage.get("72"), 7, 2, "Strength Training", TrainingType.STRENGTH, LocalDate.of(2024, 7, 7), "45");
        assertTrainingData(trainingStorage.get("84"), 8, 4, "Yoga Balance", TrainingType.BALANCE, LocalDate.of(2024, 7, 8), "90");
        assertTrainingData(trainingStorage.get("95"), 9, 5, "Evening Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 9), "60");
        assertTrainingData(trainingStorage.get("103"), 10, 3, "Fitness Flexibility", TrainingType.FLEXIBILITY, LocalDate.of(2024, 7, 10), "30");
        assertTrainingData(trainingStorage.get("111"), 11, 1, "Morning Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 11), "60");
        assertTrainingData(trainingStorage.get("122"), 12, 2, "Strength Training", TrainingType.STRENGTH, LocalDate.of(2024, 7, 12), "45");
        assertTrainingData(trainingStorage.get("134"), 13, 4, "Yoga Balance", TrainingType.BALANCE, LocalDate.of(2024, 7, 13), "90");
        assertTrainingData(trainingStorage.get("145"), 14, 5, "Evening Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 14), "60");
        assertTrainingData(trainingStorage.get("153"), 15, 3, "Fitness Flexibility", TrainingType.FLEXIBILITY, LocalDate.of(2024, 7, 15), "30");
    }

    private void assertTrainingData(Training training, Integer trainerId, Integer traineeId, String trainingName, TrainingType trainingType, LocalDate trainingDate, String trainingDuration) {
        assertNotNull(training);
        assertEquals(trainerId, training.getTrainerId());
        assertEquals(traineeId, training.getTraineeId());
        assertEquals(trainingName, training.getTrainingName());
        assertEquals(trainingType, training.getTrainingType());
        assertEquals(trainingDate, training.getTrainingDate());
        assertEquals(trainingDuration, training.getTrainingDuration());
    }

}
