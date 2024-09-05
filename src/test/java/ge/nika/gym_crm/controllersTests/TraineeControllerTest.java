package ge.nika.gym_crm.controllersTests;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.controllers.TraineeController;
import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.services.TraineeService;
import ge.nika.gym_crm.services.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class TraineeControllerTest {
    @Autowired
    TraineeController traineeController;
    @Mock
    private TraineeService traineeService;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TrainerService trainerService;
    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    TrainerRepository trainerRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO("Nika", "Williams", true,
                LocalDate.of(2000, 3, 29), "Georgia, Tbilisi");

        ResponseEntity<Trainee> traineeResponseEntity = traineeController.registerTrainee(traineeDTO);
        Trainee responseTrainee = traineeResponseEntity.getBody();

        Trainee trainee = traineeRepository.findByUser_UserName("Nika.Williams").get();

        assertNotNull(trainee);
        assertEquals("Nika.Williams", trainee.getUser().getUserName());
        assertEquals(10, trainee.getUser().getPassword().length());
    }

    @Test
    void testRegisterTraineeWithTrainers() {
        User trainerUser1 = new User("Luke", "Bore", true);
        Trainer trainer1 = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), trainerUser1);

        User trainerUser2 = new User("Tom", "Holland", true);
        Trainer trainer2 = new Trainer(new TrainingType(2, TrainingTypeNames.STRENGTH), trainerUser2);

        User trainerUser3 = new User("Antony", "Joshua", true);
        Trainer trainer3 = new Trainer(new TrainingType(4, TrainingTypeNames.BALANCE), trainerUser3);

        Set<Trainer> trainers = new HashSet<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        trainers.add(trainer3);

        TraineeDTO traineeDTO = new TraineeDTO("Nika", "Williams", true,
                LocalDate.of(2000, 3, 29), "Georgia, Tbilisi", trainers);

        ResponseEntity<Trainee> traineeResponseEntity = traineeController.registerTrainee(traineeDTO);
        Trainee responseTrainee = traineeResponseEntity.getBody();

        // Assertions
        assertNotNull(responseTrainee);
        assertNotNull(responseTrainee.getUser());
        assertEquals("Nika.Williams", responseTrainee.getUser().getUserName());
        assertNotNull(responseTrainee.getUser().getPassword());
        assertNotNull(responseTrainee.getTrainers());
        assertEquals(3, responseTrainee.getTrainers().size());


        List<String> usernames = responseTrainee.getTrainers().stream()
                .map(trainer -> trainer.getUser().getUserName())
                .toList();

        // Assert that the list contains the expected usernames
        assertTrue(usernames.contains("Luke.Bore"), "Trainers usernames should contain 'Luke.Bore'");
        assertTrue(usernames.contains("Tom.Holland"), "Trainers usernames should contain 'Tom.Holland'");
        assertTrue(usernames.contains("Antony.Joshua"), "Trainers usernames should contain 'Antony.Joshua'");
    }

    @Test
    void testAllProfile() {
        Trainee trainee = registerTraineeWithTrainers();

        List<Trainee> trainees = traineeController.getAllProfiles();
        Trainee trainee1 = trainees.getFirst();
        System.out.println(trainee1.getUser());
//        System.out.println(trainee1.getTrainers());
    }

    @Test
    void testGetTraineeProfile() {
        Trainee trainee = registerTraineeWithTrainers();
        System.out.println("trainee that needs to be selected:: " + trainee);

        ResponseEntity<?> traineeResponseEntity = traineeController.getTraineeProfile(trainee.getUser().getUserName());

        Trainee traineeProfile = (Trainee) traineeResponseEntity.getBody();
        System.out.println("trainee profile: " + traineeProfile.getId());
        System.out.println(traineeProfile.getDob());
        System.out.println(traineeProfile.getAddress());
        System.out.println(traineeProfile.getUser());

        Set<Trainer> trainerSet = traineeProfile.getTrainers();

        for (Trainer trainer : trainerSet) {
            System.out.println(trainer.toString());
        }

        assertEquals(2, trainerSet.size());

    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = registerTraineeWithTrainers();
        System.out.println("trainee that needs to be updated:: " + trainee);

        ResponseEntity<?> traineeResponseEntity = traineeController.updateTraineeProfile("Nika.Williams", "Luke", "Johnson",
                LocalDate.of(2002, 6, 19), "London", true);

        Trainee traineeProfile = (Trainee) traineeResponseEntity.getBody();
        System.out.println("trainee profile: " + traineeProfile.getId());
        System.out.println(traineeProfile.getDob());
        System.out.println(traineeProfile.getAddress());
        System.out.println(traineeProfile.getUser());

        Set<Trainer> trainerSet = traineeProfile.getTrainers();

        for (Trainer trainer : trainerSet) {
            System.out.println(trainer.toString());
        }

        assertEquals(2, trainerSet.size());

    }

    @Test
    void testUpdateTraineesTrainers() {
        Trainee trainee = registerTraineeWithTrainers();

        List<Trainer> updatedTrainers = new ArrayList<>();

        User trainerUser1 = new User("Luke", "Bore-UPDATED", true);
        Trainer trainer1 = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), trainerUser1);

        updatedTrainers.add(trainer1);

        ResponseEntity<?> traineeResponseEntity = traineeController.updateTraineesTrainers(trainee.getUser().getUserName(), updatedTrainers);
        Trainee traineeProfile = (Trainee) traineeResponseEntity.getBody();

        Set<Trainer> trainerSet = traineeProfile.getTrainers();
        trainerSet.forEach(System.out::println);

    }

    @Test
    void testDeleteTrainee() {
        Trainee trainee = registerTraineeWithTrainers();
        ResponseEntity<?> response = traineeController.deleteTraineeProfile(trainee.getUser().getUserName());

        // Verify the response status
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody()); // Response body should be null

        Optional<Trainee> deletedTrainee = traineeRepository.findByUser_UserName(trainee.getUser().getUserName());
        assertTrue(deletedTrainee.isEmpty());
    }

    @Test
    void testDeleteTrainee_NotFound() {
        String username = "testUsername";

        // Mock the delete method to throw an IllegalArgumentException (trainee not found)
        doThrow(new IllegalArgumentException("Trainee not found")).when(traineeService).delete(username);

        ResponseEntity<?> response = traineeController.deleteTraineeProfile(username);

        // Verify the response status and body
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trainee not found", response.getBody());
    }

    @Test
    void testGetTraineesTrainings() {
        Trainee trainee = registerTraineeWithTrainerAndTraining();

        ResponseEntity<?> responseEntity = traineeController.getTraineeTrainings(trainee.getUser().getUserName(), null, null, null, null);
        List<Training> trainings = (List<Training>) responseEntity.getBody();

        System.out.println(trainings);
    }

    @Test
    void testActivateDeActivateTraineeProfile() {
        Trainee trainee = registerTraineeWithTrainers();
        Boolean deActivate = false;

        traineeController.activateDeactivateTrainee(trainee.getUser().getUserName(), deActivate);

        Optional<Trainee> traineeOptional = traineeRepository.findByUser_UserName(trainee.getUser().getUserName());
        Trainee traineeDeActivated = traineeOptional.get();
        System.out.println(traineeDeActivated);
    }

    private Trainee registerTraineeWithTrainers() {
        User trainerUser1 = new User("Luke", "Bore", true);
        Trainer trainer1 = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), trainerUser1);

        User trainerUser2 = new User("Tom", "Holland", true);
        Trainer trainer2 = new Trainer(new TrainingType(2, TrainingTypeNames.STRENGTH), trainerUser2);

        Set<Trainer> trainers = new HashSet<>();
        trainers.add(trainer1);
        trainers.add(trainer2);

        TraineeDTO traineeDTO = new TraineeDTO("Nika", "Williams", true,
                LocalDate.of(2000, 3, 29), "Georgia, Tbilisi", trainers);
        return traineeController.registerTrainee(traineeDTO).getBody();
    }

    private Trainee registerTraineeWithTrainerAndTraining() {
        User trainerUser1 = new User("Luke", "Bore", true);
        Trainer trainer1 = new Trainer(new TrainingType(1, TrainingTypeNames.CARDIO), trainerUser1);

        User trainerUser2 = new User("Tom", "Holland", true);
        Trainer trainer2 = new Trainer(new TrainingType(2, TrainingTypeNames.STRENGTH), trainerUser2);

        Set<Trainer> trainers = new HashSet<>();
        trainers.add(trainer1);
        trainers.add(trainer2);

        TraineeDTO traineeDTO = new TraineeDTO("Nika", "Williams", true,
                LocalDate.of(2000, 3, 29), "Georgia, Tbilisi", trainers);

        Trainee trainee = traineeController.registerTrainee(traineeDTO).getBody();
        Optional<Trainer> selectedTrainer1 = trainerRepository.findByUser_UserName("Tom.Holland");
        Optional<Trainer> selectedTrainer2 = trainerRepository.findByUser_UserName("Luke.Bore");

        Training training1 = new Training(trainee, trainer1, "NEW TRAINING-1", selectedTrainer1.get().getSpecialization(), new Date(), 3);
        Training training2 = new Training(trainee, trainer2, "NEW TRAINING-2", selectedTrainer2.get().getSpecialization(), new Date(), 2);

        trainingRepository.save(training1);
        trainingRepository.save(training2);


        return trainee;
    }
}




