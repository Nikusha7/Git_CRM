package ge.nika.gym_crm.daoTests;

import ge.nika.gym_crm.DAO.Impl.TraineeDaoImpl;
import ge.nika.gym_crm.DAO.Impl.TrainingDaoImpl;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.storages.StorageTrainee;
import ge.nika.gym_crm.storages.StorageTraining;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainingDaoTest {
    @Autowired
    private TrainingDaoImpl trainingDao;

    @Autowired
    private StorageTraining storageTraining;

    @Test
    void testCreate() {
        Training training = new Training(5, 1, "New Training-created from test", TrainingType.CARDIO,
                LocalDate.now(), "120");
        trainingDao.create(training);
        assertEquals(training, storageTraining.getTrainingStorage().get("51"));
        storageTraining.getTrainingStorage().remove("51"); //removing just to ensure safety for other tests
    }

    @Test
    void testSelect_existing() {
        assertNotNull(trainingDao.select("11"));
    }

    @Test
    void testSelect_nonExisting() {
        assertNull(trainingDao.select("251"));
    }


}
