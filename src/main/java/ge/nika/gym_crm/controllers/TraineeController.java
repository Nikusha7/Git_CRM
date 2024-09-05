package ge.nika.gym_crm.controllers;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.services.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeRepository traineeRepository;

    public TraineeController(TraineeService traineeService, TraineeRepository traineeRepository) {
        this.traineeService = traineeService;
        this.traineeRepository = traineeRepository;
    }

    @PostMapping("/trainee/register")
    public ResponseEntity<Trainee> registerTrainee(@RequestBody TraineeDTO traineeDTO) {
        Trainee responseTrainee = traineeService.create(traineeDTO);
        return new ResponseEntity<>(responseTrainee, HttpStatus.CREATED);
    }

    @GetMapping("/trainee/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Trainee loginTrainee = traineeService.login(username, password);
        if (loginTrainee != null) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/trainee/change-password")
    public ResponseEntity<String> changeLogin(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            traineeService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password");
        }
    }


    @GetMapping("/trainee/get-all-profiles")
    public List<Trainee> getAllProfiles() {
        return traineeRepository.findAll();
    }

    @GetMapping("/trainee/get-profile")
    public ResponseEntity<?> getTraineeProfile(@RequestParam String username) {
        if (username == null || username.trim().isEmpty()) {
            // Return HTTP 400 Bad Request if the username is missing or empty
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }

        try {
            Trainee trainee = traineeService.select(username);
            return ResponseEntity.ok(trainee); // Return the trainee with HTTP 200 OK
        } catch (IllegalArgumentException e) {
            // Return HTTP 404 Not Found if the trainee is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/trainee/update")
    public ResponseEntity<?> updateTraineeProfile(@RequestParam String username,
                                                  @RequestParam String firstName,
                                                  @RequestParam String lastName,
                                                  @RequestParam(required = false) LocalDate dob, // optional
                                                  @RequestParam(required = false) String address, // optional
                                                  @RequestParam Boolean isActive) {
        // Create DTO
        TraineeDTO traineeDTO = new TraineeDTO(
                firstName,
                lastName,
                isActive,
                dob,
                address
        );

        try {
            Trainee updatedTrainee = traineeService.update(username, traineeDTO);
            return ResponseEntity.ok(updatedTrainee); // Return the updated trainee with HTTP 200 OK
        } catch (IllegalArgumentException e) {
            // Handle known validation issues or not found errors
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the trainee: " + e.getMessage());
        }

    }

    @DeleteMapping("/trainee/delete")
    public ResponseEntity<?> deleteTraineeProfile(@RequestParam String username) {
        try {
            traineeService.delete(username);
            return ResponseEntity.ok().build();  // Return 200 OK with no content
        } catch (IllegalArgumentException e) {
            // Handle the case where the trainee is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the trainee: " + e.getMessage());
        }
    }


    @PutMapping("/trainee/update-trainers")
    public ResponseEntity<?> updateTraineesTrainers(@RequestParam String username,
                                                    @RequestParam List<Trainer> trainers) {
        try {
            Trainee updatedTrainers = traineeService.updateTrainersList(username, trainers);
            return ResponseEntity.ok(updatedTrainers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating trainees trainers: " + e.getMessage());
        }

    }

    @GetMapping("/trainee/trainings")
    public ResponseEntity<?> getTraineeTrainings(@RequestParam String username,
                                                 @RequestParam(required = false) String periodFrom,
                                                 @RequestParam(required = false) String periodTo,
                                                 @RequestParam(required = false) String trainerName,
                                                 @RequestParam(required = false) TrainingTypeNames trainingTypeNames
    ) {
        try {
            List<Training> traineesTrainings = traineeService.getTraineesTrainings(username);
            return ResponseEntity.ok(traineesTrainings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/trainee/activate-deactivate")
    public ResponseEntity<?> activateDeactivateTrainee(@RequestParam String username,
                                                       @RequestParam Boolean isActive) {
        try {
            traineeService.activateDeactivate(username, isActive);
            return ResponseEntity.ok("Successfully set " + isActive + " status");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
