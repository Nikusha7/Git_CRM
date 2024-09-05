package ge.nika.gym_crm.controllersTests;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.controllers.TraineeController;
import ge.nika.gym_crm.controllers.TrainerController;
import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.services.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TrainerControllerTest {

    @Autowired
    TrainerController trainerController;

    @Autowired
    TrainerRepository trainerRepository;

    @Autowired
    TraineeController traineeController;

    @Autowired
    TrainerService trainerService;

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    TraineeRepository traineeRepository;

    @Test
    void testRegisterTrainer() {
        User traineeUser1 = new User("Bob", "Johnson", true);
        Trainee trainee1 = new Trainee(LocalDate.of(1998, 2, 15), "London", traineeUser1);

        User traineeUser2 = new User("Duke", "Borrow", true);
        Trainee trainee2 = new Trainee(LocalDate.of(1999, 1, 18), "Oslo", traineeUser2);

        Set<Trainee> traineeSet = new HashSet<>();
        traineeSet.add(trainee1);
        traineeSet.add(trainee2);

        TrainerDTO trainerDTO = new TrainerDTO("Wilson", "Baker", true,
                new TrainingType(3, TrainingTypeNames.FLEXIBILITY), traineeSet);

        Trainer savedTrainer = trainerController.registerTrainer(trainerDTO).getBody();
        System.out.println("Trainer saved successfully::" + savedTrainer);
        System.out.println("Associated trainees: " + savedTrainer.getTrainees());
    }

    @Test
    void testGetTrainerProfile() {
        Trainer savedTrainer = registerTrainerWithTrainees();

        System.out.println("trainer that needs to be selected:: " + savedTrainer);

        ResponseEntity<?> trainerResponseEntity = trainerController.getTrainerProfile(savedTrainer.getUser().getUserName());

        Trainer retrievedTrainerProfile = (Trainer) trainerResponseEntity.getBody();
        System.out.println(retrievedTrainerProfile);
        Set<Trainee> traineeSet = retrievedTrainerProfile.getTrainees();

        for (Trainee trainee : traineeSet) {
            System.out.println(trainee.toString());
        }
    }

    @Test
    void testUpdateTrainerProfile() {
        Trainer trainer = registerTrainerWithTrainees();
        System.out.println("trainer that needs to be updated:: " + trainer);

        ResponseEntity<?> trainerResponseEntity = trainerController.updateTrainerProfile("Wilson.Baker", "Wilson", "Baker-UPDATED",
                new TrainingType(2, TrainingTypeNames.STRENGTH), true);

        Trainer trainerProfile = (Trainer) trainerResponseEntity.getBody();
        System.out.println("trainer profile: " + trainerProfile.getId());
        System.out.println(trainerProfile.getUser());
        System.out.println(trainerProfile.getSpecialization());

        Set<Trainee> traineeSet = trainerProfile.getTrainees();

        for (Trainee trainee : traineeSet) {
            System.out.println(trainee.toString());
        }

        assertEquals(2, traineeSet.size());
    }

    @Test
    void testGetNotAssignedTrainers() {
//        register Trainee
        TraineeDTO traineeDTO = new TraineeDTO("Nika", "Williams", true,
                LocalDate.of(2000, 3, 29), "Georgia, Tbilisi");

        ResponseEntity<Trainee> traineeResponseEntity = traineeController.registerTrainee(traineeDTO);
        Trainee savedTrainee = traineeResponseEntity.getBody();

//        register Trainers
        TrainerDTO trainerDTO1 = new TrainerDTO("Luke", "Bore", true,
                new TrainingType(1, TrainingTypeNames.CARDIO));
        TrainerDTO trainerDTO2 = new TrainerDTO("Tom", "Holland", true,
                new TrainingType(3, TrainingTypeNames.FLEXIBILITY));

        trainerService.create(trainerDTO1);
        trainerService.create(trainerDTO2);

        ResponseEntity<?> selectedTraineeEntity = traineeController.getTraineeProfile(savedTrainee.getUser().getUserName());
        System.out.println("Saved Trainee: " + selectedTraineeEntity.getBody());

        List<Trainer> trainerList = trainerRepository.findAll();

        trainerList.forEach(System.out::println);


        ResponseEntity<?> notAssignedTrainers = trainerController.getNotAssignedTrainers(savedTrainee.getUser().getUserName());
        List<Trainer> notAssignedTrainerList = (List<Trainer>) notAssignedTrainers.getBody();

        notAssignedTrainerList.forEach(System.out::println);
    }

    @Test
    void testGetTraineesTrainings() {
        Trainer trainer = registerTrainerWithTraineeAndTraining();

        ResponseEntity<?> responseEntity = trainerController.getTrainerTrainings(trainer.getUser().getUserName(), null, null,null);
        List<Training> trainings = (List<Training>) responseEntity.getBody();

        System.out.println(trainings);
    }

    @Test
    void testActivateDeActivateTrainerProfile(){
        Trainer trainer = registerTrainerWithTrainees();
        Boolean deActivate = false;

        trainerController.activateDeactivateTrainer(trainer.getUser().getUserName(), deActivate);

        Optional<Trainer> traineeOptional = trainerRepository.findByUser_UserName(trainer.getUser().getUserName());
        Trainer trainerDeActivated = traineeOptional.get();
        System.out.println(trainerDeActivated);
    }

    private Trainer registerTrainerWithTrainees() {
        User traineeUser1 = new User("Bob", "Johnson", true);
        Trainee trainee1 = new Trainee(LocalDate.of(1998, 2, 15), "London", traineeUser1);

        User traineeUser2 = new User("Duke", "Borrow", true);
        Trainee trainee2 = new Trainee(LocalDate.of(1999, 1, 18), "Oslo", traineeUser2);

        Set<Trainee> traineeSet = new HashSet<>();
        traineeSet.add(trainee1);
        traineeSet.add(trainee2);

        TrainerDTO trainerDTO = new TrainerDTO("Wilson", "Baker", true,
                new TrainingType(3, TrainingTypeNames.FLEXIBILITY), traineeSet);

        return trainerController.registerTrainer(trainerDTO).getBody();
    }

    private Trainer registerTrainerWithTraineeAndTraining() {
        User traineeUser1 = new User("Bob", "Johnson", true);
        Trainee trainee1 = new Trainee(LocalDate.of(1998, 2, 15), "London", traineeUser1);

        User traineeUser2 = new User("Duke", "Borrow", true);
        Trainee trainee2 = new Trainee(LocalDate.of(1999, 1, 18), "Oslo", traineeUser2);

        Set<Trainee> traineeSet = new HashSet<>();
        traineeSet.add(trainee1);
        traineeSet.add(trainee2);

        TrainerDTO trainerDTO = new TrainerDTO("Wilson", "Baker", true,
                new TrainingType(3, TrainingTypeNames.FLEXIBILITY), traineeSet);

        Trainer trainer = trainerController.registerTrainer(trainerDTO).getBody();

        Optional<Trainee> selectedTrainee1 = traineeRepository.findByUser_UserName("Bob.Johnson");
        Optional<Trainee> selectedTrainee2 = traineeRepository.findByUser_UserName("Duke.Borrow");

        Training training1 = new Training(selectedTrainee1.get(), trainer, "NEW TRAINING-1", trainer.getSpecialization(), new Date(), 3);
        Training training2 = new Training(selectedTrainee2.get(), trainer, "NEW TRAINING-2", trainer.getSpecialization(), new Date(), 2);

        trainingRepository.save(training1);
        trainingRepository.save(training2);


        return trainer;
    }
}
