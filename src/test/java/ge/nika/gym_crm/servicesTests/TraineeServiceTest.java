package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.UserDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TraineeServiceTest {

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeService traineeService;


    @Test
    void testSaveTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("John", "Bonny", true,
                Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");
        traineeService.create(traineeDTO);
    }

    @Test
    void testSelectTrainee() {
        System.out.println(traineeService.select("John.Bonny"));
    }


    @Test
    void testUpdateTrainee(){
        User user = new User("John-UPDATED!", "Bonny", true);
        Trainee trainee = new Trainee(Date.valueOf(LocalDate.now()), "Georgia, Tbilisi", user);

        traineeService.update(1, trainee);
    }

    @Test
    void testDeleteTrainee(){
        traineeService.delete("John-UPDATED!.Bonny");

    }

}
