package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DTO.TrainingDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Training create(String traineeUsername, String trainerUsername, String trainingName,
                           Date trainingDate, Integer trainingDuration) {
        log.info("Attempting to create training with traineeUsername: {} and trainerUsername: {}", traineeUsername, trainerUsername);

        // Retrieve the Trainee based on the username and throw an exception if not found
        Trainee trainee = traineeRepository.findByUser_UserName(traineeUsername)
                .orElseThrow(() -> {
                    log.error("No Trainee found with username: {}", traineeUsername);
                    return new IllegalArgumentException("Trainee with username " + traineeUsername + " not found.");
                });
        log.info("Successfully retrieved Trainee with username: {}", traineeUsername);

        // Retrieve the Trainer based on the username and throw an exception if not found
        Trainer trainer = trainerRepository.findByUser_UserName(trainerUsername)
                .orElseThrow(() -> {
                    log.error("No Trainer found with username: {}", trainerUsername);
                    return new IllegalArgumentException("Trainer with username " + trainerUsername + " not found.");
                });
        log.info("Successfully retrieved Trainer with username: {}", trainerUsername);

        // Create a new Training object
        log.info("Creating new Training for Trainee: {} and Trainer: {}", trainee.getUser().getUserName(), trainer.getUser().getUserName());
        Training newTraining = new Training(trainee, trainer, trainingName, trainer.getSpecialization(), trainingDate, trainingDuration);

        // Save the Training object to the repository
        Training createdTraining = trainingRepository.save(newTraining);
        log.info("Successfully created Training with ID: {} for Trainee: {} and Trainer: {}",
                createdTraining.getId(), trainee.getUser().getUserName(), trainer.getUser().getUserName());

        return createdTraining;
    }

    @Override
    public Training select(Integer id) {
        log.info("Selecting training session with ID: {}", id);

        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty()) {
            log.warn("Training session with ID {} not found", id);
        } else {
            log.info("Training session with ID {} found", id);
        }

        return training.orElse(null);
    }

}
