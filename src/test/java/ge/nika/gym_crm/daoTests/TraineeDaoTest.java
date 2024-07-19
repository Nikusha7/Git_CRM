package ge.nika.gym_crm.daoTests;

import ge.nika.gym_crm.DAO.Impl.TraineeDaoImpl;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.storages.StorageTrainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TraineeDaoTest {

    @Autowired
    private TraineeDaoImpl traineeDao;

    @Autowired
    private StorageTrainee storageTrainee;

    @Test
    void testCreate() {
        Trainee trainee = new Trainee("John", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi", 16);
        traineeDao.create(trainee);
        assertTraineeData(trainee, storageTrainee.getTraineeStorage().get(16));
    }

    @Test
    void testSelect_existing() {
        Trainee trainee = new Trainee("Michael", "Brown", true, LocalDate.of(1990, 5, 14), "123 Elm St",1);
        Trainee result = traineeDao.select(1);
        assertTraineeData(trainee, result);
    }

    @Test
    void testSelect_nonExisting() {
        Trainee result = traineeDao.select(999);
        assertNull(result);
    }

    @Test
    void testUpdate() {
        Trainee trainee = new Trainee("John-UPDATED", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi", 16);
        traineeDao.update(16, trainee);
        assertEquals(trainee, storageTrainee.getTraineeStorage().get(16));
    }

    @Test
    void testDelete() {
        traineeDao.delete(16);
        assertNull(storageTrainee.getTraineeStorage().get(16));
    }

    private void assertTraineeData(Trainee trainee, Trainee actualTrainee) {
        assertNotNull(trainee);
        assertEquals(actualTrainee.getUserId(), trainee.getUserId());
        assertEquals(actualTrainee.getFirstName(), trainee.getFirstName());
        assertEquals(actualTrainee.getLastName(), trainee.getLastName());
        assertEquals(actualTrainee.getActive(), trainee.getActive());
        assertEquals(actualTrainee.getDob(), trainee.getDob());
        assertEquals(actualTrainee.getAddress(), trainee.getAddress());
    }
}
