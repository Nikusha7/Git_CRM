package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserRepository userRepository,
                              TrainingRepository trainingRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Trainer create(TrainerDTO trainerDTO) {
        log.info("Creating a new trainer with DTO: {}", trainerDTO);

        User user = new User(trainerDTO.getFirstName(), trainerDTO.getLastName(), trainerDTO.getIsActive());
        user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());

        Trainer trainer = new Trainer(trainerDTO.getSpecialization());
        trainer.setUser(user);

        if (trainerDTO.getTrainees() != null) {
            Set<Trainee> traineeSet = trainerDTO.getTrainees();
            // generate usernames and passwords for each Trainee's User
            for (Trainee trainee : traineeSet) {
                trainee.getUser().setPassword(generatePassword());
                trainee.getUser().setUserName(generateUniqueUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName()));
                trainer.addTrainee(trainee); // Ensure bidirectional consistency
            }
        }

        Trainer savedTrainer = trainerRepository.save(trainer);
        log.info("Created trainer with ID: {}", savedTrainer.getId());

        return savedTrainer;
    }

    @Override
    public Trainer select(String userName) {
        log.info("Selecting trainer with username: {}", userName);

        Optional<Trainer> trainer = trainerRepository.findByUser_UserName(userName);
        if (trainer.isEmpty()) {
            log.warn("Trainer with username {} not found", userName);
        } else {
            log.info("Trainer with username {} found", userName);
            log.info(trainer.toString());
        }

        return trainer.orElse(null);
    }

    @Override
    public List<Training> getTrainersTrainings(String username) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUser_UserName(username);

        if (trainerOptional.isEmpty()) {
            throw new IllegalArgumentException("Trainer not found with username: " + username);
        }

        Trainer trainer = trainerOptional.get();

        return trainingRepository.findByTrainer_Id(trainer.getId());
    }

    @Override
    public List<Trainer> getNotAssignedTrainers(String username) {
        log.info("Fetching trainers not assigned to trainee with username: {}", username);

        List<Trainer> trainers = trainerRepository.findActiveTrainersNotAssignedToTrainee(username);

        if (trainers.isEmpty()) {
            log.info("No trainers found that are not assigned to the trainee with username: {}", username);
        } else {
            log.info("{} trainers found that are not assigned to the trainee with username: {}", trainers.size(), username);
        }

        return trainers;
    }

    @Override
    public Trainer login(String username, String password) {
        if (authenticate(username, password)) {
            return select(username);
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public Trainer update(String username, TrainerDTO newTrainerDTO) {
        log.info("Attempting to update trainer with username: {}", username);

        // Validation: Ensure first name, last name, and isActive are provided
        if (newTrainerDTO.getFirstName() == null || newTrainerDTO.getFirstName().isEmpty()) {
            log.warn("First name is missing for the update.");
            throw new IllegalArgumentException("First name is required.");
        }

        if (newTrainerDTO.getLastName() == null || newTrainerDTO.getLastName().isEmpty()) {
            log.warn("Last name is missing for the update.");
            throw new IllegalArgumentException("Last name is required.");
        }

        if (newTrainerDTO.getIsActive() == null) {
            log.warn("IsActive status is missing for the update.");
            throw new IllegalArgumentException("IsActive status is required.");
        }

        // Fetch the existing trainer
        log.info("Fetching existing trainer with username: {}", username);
        Optional<Trainer> existingTrainerOptional = trainerRepository.findByUser_UserName(username);
        if (existingTrainerOptional.isEmpty()) {
            log.error("Trainer with username {} not found.", username);
            throw new IllegalArgumentException("Trainer not found.");
        }

        // Generate new username and password
        log.info("Generating a unique username and password for the trainer.");
        newTrainerDTO.setPassword(generatePassword());
        newTrainerDTO.setUserName(generateUniqueUsername(newTrainerDTO.getFirstName(), newTrainerDTO.getLastName()));

        Trainer existingTrainer = existingTrainerOptional.get();
        User existingUser = existingTrainer.getUser();

        // Update user details
        log.info("Updating user details.");
        existingUser.setFirstName(newTrainerDTO.getFirstName());
        existingUser.setLastName(newTrainerDTO.getLastName());
        existingUser.setUserName(newTrainerDTO.getUserName());
        existingUser.setPassword(newTrainerDTO.getPassword());
        existingUser.setIsActive(newTrainerDTO.getIsActive());

        // Save the updated trainer
        log.info("Saving the updated trainer.");
        existingTrainer.setUser(existingUser);
        Trainer savedTrainer = trainerRepository.save(existingTrainer);
        log.info("Successfully updated trainer with username: {}", username);

        return savedTrainer;
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for username: {}", username);

        // Validate password
        if (newPassword == null || newPassword.trim().isEmpty()) {
            log.error("Password cannot be null or empty");
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (newPassword.length() < 10) {
            log.error("Password must be at least 10 characters long");
            throw new IllegalArgumentException("Password must be at least 10 characters long");
        }

        // Authenticate user
        if (!authenticate(username, oldPassword)) {
            log.error("Username or old password is incorrect!");
            throw new IllegalArgumentException("Trainee not found/authenticated");
        }

        // Find user and update password
        Optional<Trainer> trainerOptional = trainerRepository.findByUser_UserName(username);
        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            User user = trainer.getUser();
            user.setPassword(newPassword);

            userRepository.save(user); // Save user with new password
            log.info("Password changed successfully for username: {}", username);
        } else {
            log.error("User not found for username: {}", username);
            throw new IllegalArgumentException("User not found");
        }

    }

    @Override
    @Transactional
    public void activateDeactivate(String username, Boolean isActive) {
        log.info("Changing active status for username: {} to {}", username, isActive);

        // Validate isActive
        if (isActive == null) {
            log.error("isActive cannot be null");
            throw new IllegalArgumentException("isActive cannot be null");
        }

        Optional<Trainer> trainerOptional = trainerRepository.findByUser_UserName(username);
        if (trainerOptional.isEmpty()) {
            log.error("Trainer with username {} not found", username);
            throw new IllegalArgumentException("Trainer not found");
        }

        Trainer trainer = trainerOptional.get();
        trainer.getUser().setIsActive(isActive);
        trainerRepository.save(trainer);

        log.info("Active status changed successfully for username: {} to {}", username, isActive);
    }


    public String generatePassword() {
        log.debug("Generating a new password");

        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(Character::isLetterOrDigit)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        log.debug("Generating unique username for firstName: {} and lastName: {}", firstName, lastName);
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (checkUsernameExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        log.debug("Generated unique username: {}", username);
        return username;
    }

    private boolean checkUsernameExists(String username) {
        boolean exists = userRepository.existsByUserName(username);
        log.debug("Checked if username {} exists: {}", username, exists);
        return exists;
    }

    public boolean authenticate(String username, String password) {
        // Retrieve User by username
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            return false; // User not found
        }
        // Check if the provided password matches the stored password
        return user.get().getPassword().equals(password);
    }

}
