package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Use in-memory database
@Transactional // Rollback transactions after each test
class TraineeServiceTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee result = traineeService.create(traineeDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("John.Bonny", result.getUser().getUserName());
    }

    @Test
    void testSelectTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        traineeService.create(traineeDTO);

        Trainee result = traineeService.select("John.Bonny");

        assertNotNull(result);
        assertEquals("Georgia, Tbilisi", result.getAddress());
    }

    @Test
    void select_ShouldReturnNullWhenTraineeNotFound() {
        Trainee result = traineeService.select("unknown.user");
        assertNull(result);
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee existingTrainee = traineeService.create(traineeDTO);

        User updatedUser = new User("John", "Doe-Updated", true);
        Trainee updatedTrainee = new Trainee(Date.valueOf(LocalDate.now()), "Georgia, Tbilisi Updated", updatedUser);

        Trainee result = traineeService.update(existingTrainee.getId(), updatedTrainee);

        assertNotNull(result);
        assertEquals("John.Doe-Updated", result.getUser().getUserName());
        assertEquals("Georgia, Tbilisi Updated", result.getAddress());
    }

    @Test
    void update_ShouldThrowExceptionWhenTraineeNotFound() {
        User updatedUser = new User("John", "Doe Updated", true);
        Trainee updatedTrainee = new Trainee(Date.valueOf(LocalDate.now()), "Georgia, Tbilisi Updated", updatedUser);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(111111, updatedTrainee);
        });

        assertEquals("Trainee not found", thrown.getMessage());
    }

    @Test
    void delete_ShouldDeleteTraineeAndUser() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee existingTrainee = traineeService.create(traineeDTO);

        traineeService.delete("John.Bonny");

        assertFalse(traineeRepository.existsById(existingTrainee.getId()));
        assertFalse(userRepository.existsById(existingTrainee.getUser().getId()));
    }

    @Test
    void delete_ShouldThrowExceptionWhenUserNotFound() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.delete("unknown.user");
        });

        assertEquals("User with username: unknown.user not found", thrown.getMessage());
    }

    @Test
    void changePassword_ShouldChangePassword() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee existingTrainee = traineeService.create(traineeDTO);

        traineeService.changePassword("John.Bonny", "newPassword123");

        assertEquals("newPassword123", traineeService.select("John.Bonny").getUser().getPassword());
    }

    @Test
    void changePassword_ShouldThrowExceptionWhenPasswordInvalid() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.changePassword("john.doe", "short");
        });

        assertEquals("Password must be at least 10 characters long", thrown.getMessage());
    }

    @Test
    void activateDeactivate_ShouldUpdateActiveStatus() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        Trainee existingTrainee = traineeService.create(traineeDTO);

        traineeService.activateDeactivate("John.Bonny", false);

        assertFalse(traineeService.select("John.Bonny").getUser().getIsActive());
    }

    @Test
    void activateDeactivate_ShouldThrowExceptionWhenTraineeNotFound() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.activateDeactivate("unknown.user", true);
        });

        assertEquals("Trainee with username unknown.user not found", thrown.getMessage());
    }
}
