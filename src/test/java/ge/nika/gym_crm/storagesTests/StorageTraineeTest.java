package ge.nika.gym_crm.storagesTests;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.storages.StorageTrainee;
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
public class StorageTraineeTest {
    @Autowired
    private StorageTrainee storageTrainee;

    @Test
    public void testPostConstructInitialization() {
        Map<Integer, Trainee> traineeStorage = storageTrainee.getTraineeStorage();
        assertNotNull(traineeStorage);
        assertFalse(traineeStorage.isEmpty());
    }

    @Test
    public void testStorageSize() {
        Map<Integer, Trainee> traineeStorage = storageTrainee.getTraineeStorage();
        assertEquals(15, traineeStorage.size());
    }

    @Test
    public void testUniqueUsernames() {
        Map<Integer, Trainee> traineeStorage = storageTrainee.getTraineeStorage();
        traineeStorage.values().forEach(trainee -> {
            assertTrue(isUsernameUnique(trainee.getUserName(), traineeStorage));
        });
    }

    @Test
    public void testPasswordGeneration() {
        Map<Integer, Trainee> traineeStorage = storageTrainee.getTraineeStorage();

        traineeStorage.values().forEach(trainee -> {
            assertNotNull(trainee.getPassword());
            assertEquals(10, trainee.getPassword().length());
        });
    }

    @Test
    public void testCorrectDataReadFromCSV() {
        Map<Integer, Trainee> traineeStorage = storageTrainee.getTraineeStorage();

        // Assuming the expected data is known beforehand for comparison
        assertTraineeData(traineeStorage.get(1), 1, "Michael", "Brown", true, LocalDate.of(1990, 5, 14), "123 Elm St");
        assertTraineeData(traineeStorage.get(2), 2, "Sarah", "Williams", true, LocalDate.of(1985, 8, 23), "456 Oak St");
        assertTraineeData(traineeStorage.get(3), 3, "David", "Jones", false, LocalDate.of(1992, 11, 30), "789 Pine St");
        assertTraineeData(traineeStorage.get(4), 4, "Linda", "Miller", true, LocalDate.of(1988, 2, 19), "321 Maple St");
        assertTraineeData(traineeStorage.get(5), 5, "Robert", "Garcia", true, LocalDate.of(1991, 3, 22), "654 Birch St");
        assertTraineeData(traineeStorage.get(6), 6, "Jessica", "Martinez", false, LocalDate.of(1987, 7, 14), "987 Cedar St");
        assertTraineeData(traineeStorage.get(7), 7, "Thomas", "Anderson", true, LocalDate.of(1993, 12, 5), "741 Pine St");
        assertTraineeData(traineeStorage.get(8), 8, "Patricia", "Thomas", true, LocalDate.of(1986, 10, 10), "852 Oak St");
        assertTraineeData(traineeStorage.get(9), 9, "Christopher", "Moore", true, LocalDate.of(1990, 1, 1), "963 Maple St");
        assertTraineeData(traineeStorage.get(10), 10, "Barbara", "Taylor", false, LocalDate.of(1984, 6, 18), "147 Elm St");
        assertTraineeData(traineeStorage.get(11), 11, "Daniel", "Jackson", true, LocalDate.of(1989, 9, 23), "258 Birch St");
        assertTraineeData(traineeStorage.get(12), 12, "Amanda", "Thompson", true, LocalDate.of(1992, 4, 15), "369 Cedar St");
        assertTraineeData(traineeStorage.get(13), 13, "Matthew", "White", true, LocalDate.of(1991, 8, 30), "123 Oak St");
        assertTraineeData(traineeStorage.get(14), 14, "Elizabeth", "Lee", false, LocalDate.of(1985, 2, 20), "456 Pine St");
        assertTraineeData(traineeStorage.get(15), 15, "Joshua", "Harris", true, LocalDate.of(1990, 7, 9), "789 Maple St");

    }

    private void assertTraineeData(Trainee trainee, int id, String firstName, String lastName, boolean isActive, LocalDate dob, String address) {
        assertNotNull(trainee);
        assertEquals(id, trainee.getUserId());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals(isActive, trainee.getActive());
        assertEquals(dob, trainee.getDob());
        assertEquals(address, trainee.getAddress());  // Assuming 'address' is a field in Trainer class
    }

    private boolean isUsernameUnique(String username, Map<Integer, Trainee> traineeStorage) {
        long count = traineeStorage.values().stream()
                .filter(trainee -> trainee.getUserName().equals(username))
                .count();
        return count == 1;
    }

}
