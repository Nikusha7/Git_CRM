package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.services.impl.TraineeServiceImpl;
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
public class TraineeServiceTest {

    @Autowired
    private TraineeServiceImpl traineeService;

    @Test
    public void testSelectTrainee() {
        assertTraineeData(traineeService.select(1), 1, "Michael", "Brown", true, LocalDate.of(1990, 5, 14), "123 Elm St");
    }

    @Test
    public void testSelectNotExistingTrainee() {
        assertNull(traineeService.select(20));
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee("John", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi", 16);
        traineeService.create(trainee);
        assertTraineeData(traineeService.select(16), 16, "John", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi");
    }

    @Test
    public void testUpdateTrainee() {
        traineeService.update(16, new Trainee("John-updated!", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi", 16));
        assertTraineeData(traineeService.select(16), 16, "John-updated!", "Bonny", true, LocalDate.of(2000, 3, 21), "Georgia, Tbilisi");
    }

    @Test
    public void testUpdateNotExistingTrainee() {
        traineeService.update(25, new Trainee("Luke", "Williams", false, LocalDate.of(1985, 8, 23), "400 Oak St", 25));
        assertNull(traineeService.select(25));
    }

    @Test
    public void testDeleteTrainee(){
        assertNotNull(traineeService.select(16));
        traineeService.delete(16);
        assertNull(traineeService.select(16));
    }

    @Test
    public void testGeneratePassword() {
        String password = traineeService.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void generateUniqueUsername_noConflict() {
        String username = traineeService.generateUniqueUsername("Nick", "Doe");
        assertEquals("Nick.Doe", username);
    }

    @Test
    void generateUniqueUsername_withConflict() {
        String username = traineeService.generateUniqueUsername("Michael", "Brown");
        System.out.println("new username: " + username);
        assertEquals("Michael.Brown1", username);
    }

    private void assertTraineeData(Trainee trainee, int id, String firstName, String lastName, boolean isActive, LocalDate dob, String address) {
        assertNotNull(trainee);
        assertEquals(id, trainee.getUserId());
        assertEquals(firstName, trainee.getFirstName());
        assertEquals(lastName, trainee.getLastName());
        assertEquals(isActive, trainee.getActive());
        assertEquals(dob, trainee.getDob());
        assertEquals(address, trainee.getAddress());
    }

}
