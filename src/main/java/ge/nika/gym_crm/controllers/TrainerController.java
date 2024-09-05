package ge.nika.gym_crm.controllers;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.services.TrainerService;
import ge.nika.gym_crm.services.impl.TraineeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrainerController {
    private static final Logger log = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerService trainerService;
    private final TrainerRepository trainerRepository;

    public TrainerController(TrainerService trainerService, TrainerRepository trainerRepository) {
        this.trainerService = trainerService;
        this.trainerRepository = trainerRepository;
    }

    @PostMapping("/trainer/register")
    public ResponseEntity<Trainer> registerTrainer(@RequestBody TrainerDTO trainerDTO) {
        Trainer responseTrainer = trainerService.create(trainerDTO);
        return new ResponseEntity<>(responseTrainer, HttpStatus.CREATED);
    }

    @GetMapping("/trainer/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Trainer loginTrainer = trainerService.login(username, password);
        if (loginTrainer != null) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/trainer/change-password")
    public ResponseEntity<String> changeLogin(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            trainerService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password");
        }
    }

    @GetMapping("/trainer/get-profile")
    public ResponseEntity<?> getTrainerProfile(@RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            // Return HTTP 400 Bad Request if the username is missing or empty
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }

        try {
            Trainer trainer = trainerService.select(username);
            return ResponseEntity.ok(trainer); // Return the trainer with HTTP 200 OK
        } catch (IllegalArgumentException e) {
            // Return HTTP 404 Not Found if the trainer is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/trainee/not-assigned-trainers")
    public ResponseEntity<?> getNotAssignedTrainers(@RequestParam String username) {
        log.info("Received request to fetch trainers not assigned to trainee with username: {}", username);

        try {
            List<Trainer> trainers = trainerService.getNotAssignedTrainers(username);

            if (trainers.isEmpty()) {
                log.info("No trainers found that are not assigned to trainee with username: {}", username);
                return ResponseEntity.ok("No trainers available for this trainee.");
            } else {
                log.info("Returning {} trainers not assigned to trainee with username: {}", trainers.size(), username);
                return ResponseEntity.ok(trainers);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching trainers for trainee with username: {}", username, e);
            return ResponseEntity.status(500).body("An error occurred while fetching trainers. Please try again later.");
        }

    }


    @PostMapping("/trainer/update")
    public ResponseEntity<?> updateTrainerProfile(@RequestParam String username,
                                                  @RequestParam String firstName,
                                                  @RequestParam String lastName,
                                                  @RequestParam TrainingType trainingType, //read-only
                                                  @RequestParam Boolean isActive) {
        TrainerDTO trainerDTO = new TrainerDTO(
                firstName,
                lastName,
                isActive,
                trainingType
        );

        try {
            Trainer updatedTrainer = trainerService.update(username, trainerDTO);
            return ResponseEntity.ok(updatedTrainer); // Return the updated trainer with HTTP 200 OK
        } catch (IllegalArgumentException e) {
            // Handle known validation issues or not found errors
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the trainee: " + e.getMessage());
        }
    }


    @GetMapping("/trainer/trainings")
    public ResponseEntity<?> getTrainerTrainings(@RequestParam String username,
                                                        @RequestParam(required = false) String periodFrom,
                                                        @RequestParam(required = false) String periodTo,
                                                        @RequestParam(required = false) String trainerName) {
        try {
            List<Training> trainersTrainings = trainerService.getTrainersTrainings(username);
            return ResponseEntity.ok(trainersTrainings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/trainer/activate-deactivate")
    public ResponseEntity<?> activateDeactivateTrainer(@RequestParam String username,
                                                             @RequestParam Boolean isActive) {
        try {
            trainerService.activateDeactivate(username, isActive);
            return ResponseEntity.ok("Successfully set " + isActive + " status");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}