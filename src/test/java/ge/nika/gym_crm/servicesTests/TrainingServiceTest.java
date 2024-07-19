package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DAO.Impl.TrainingDaoImpl;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.services.impl.TrainingServiceImpl;
import ge.nika.gym_crm.storages.StorageTraining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainingServiceTest {

    @Autowired
    private TrainingServiceImpl trainingService;

    @Autowired
    private StorageTraining storageTraining;

    @Test
    public void testSelectTraining() {
        assertTrainingData(trainingService.select(1, 1), 1, 1, "Morning Cardio", TrainingType.CARDIO, LocalDate.of(2024, 7, 1), "60");
    }

    @Test
    public void testCreateTraining() {
        trainingService.create(new Training(5, 1, "New Training-created from test", TrainingType.CARDIO,
                LocalDate.now(), "120"));
        assertTrainingData(trainingService.select(5, 1), 5, 1, "New Training-created from test", TrainingType.CARDIO,
                LocalDate.now(), "120");
        storageTraining.getTrainingStorage().remove("51"); //removing at the end only to ensure other tests work on initial dataset
    }


    @Test
    public void testSelectNonExistingTraining() {
        assertNull(trainingService.select(24, 25));
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
