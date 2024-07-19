package ge.nika.gym_crm.daoTests;

import ge.nika.gym_crm.DAO.Impl.TraineeDaoImpl;
import ge.nika.gym_crm.DAO.Impl.TrainerDaoImpl;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.storages.StorageTrainee;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainerDaoTest {
    @Autowired
    private TrainerDaoImpl trainerDao;

    @Autowired
    private StorageTrainer storageTrainer;

    @Test
    void testCreate() {
        Trainer trainer = new Trainer("Nick", "Brown", true, "Strength", 6, TrainingType.STRENGTH);
        trainerDao.create(trainer);
        assertTrainerData(trainerDao.select(6), 6, "Nick", "Brown", true, "Strength", TrainingType.STRENGTH);
    }

    @Test
    void testSelect_existing() {
        assertTrainerData(trainerDao.select(1), 1, "John", "Doe", true, "Strength", TrainingType.CARDIO);
    }

    @Test
    void testSelect_nonExisting() {
        assertNull(trainerDao.select(10));
    }

    @Test
    void testUpdate() {
        trainerDao.update(6, new Trainer("Jane-UPDATED", "Smith", true, "Endurance", 6, TrainingType.STRENGTH));
        assertTrainerData(trainerDao.select(6), 6, "Jane-UPDATED", "Smith", true, "Endurance", TrainingType.STRENGTH);
        storageTrainer.getTrainerStorage().remove(6);
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
