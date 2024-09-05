package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, UserRepository userRepository,
                              TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Trainee create(TraineeDTO traineeDTO) {
        log.info("Creating a new trainee with DTO: {}", traineeDTO);

        // Create and configure Trainee's User
        User user = new User(traineeDTO.getFirstName(), traineeDTO.getLastName(), traineeDTO.getIsActive());
        user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());

        // Create Trainee entity
        Trainee trainee = new Trainee(traineeDTO.getDob(), traineeDTO.getAddress());


        if (traineeDTO.getTrainers() != null) {
            Set<Trainer> trainerSet = traineeDTO.getTrainers();

            // generate usernames and passwords for each Trainer's User
            for (Trainer trainer : trainerSet) {
                trainer.getUser().setPassword(generatePassword());
                trainer.getUser().setUserName(generateUniqueUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName()));
            }
            // Set Trainers and
            trainee.setTrainers(trainerSet);
        }

        // User to Trainee
        trainee.setUser(user);

        // Save the Trainee entity
        Trainee savedTrainee = traineeRepository.save(trainee);
        log.info("Created trainee with ID: {}", savedTrainee.getId());

        return savedTrainee;
    }

    @Override
    public Trainee select(String username) {
        log.info("Selecting trainee with username: {}", username);

        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee with username " + username + " not found ");
        } else {
            log.info("Trainee with username {} found", username);
        }

        return trainee.get();
    }

    @Override
    public List<Training> getTraineesTrainings(String username) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUser_UserName(username);

        if (traineeOptional.isEmpty()) {
            throw new IllegalArgumentException("Trainee not found with username: " + username);
        }

        Trainee trainee = traineeOptional.get();

        return trainingRepository.findByTrainee_Id(trainee.getId());
    }

    @Override
    public Trainee login(String username, String password) {
        if (authenticate(username, password)) {
            return select(username);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Trainee update(String username, TraineeDTO newTraineeDTO) {
        log.info("Attempting to update trainee with username: {}", username);

        // Validation: Ensure first name, last name, and isActive are provided
        if (newTraineeDTO.getFirstName() == null || newTraineeDTO.getFirstName().isEmpty()) {
            log.warn("First name is missing for the update.");
            throw new IllegalArgumentException("First name is required.");
        }

        if (newTraineeDTO.getLastName() == null || newTraineeDTO.getLastName().isEmpty()) {
            log.warn("Last name is missing for the update.");
            throw new IllegalArgumentException("Last name is required.");
        }

        if (newTraineeDTO.getIsActive() == null) {
            log.warn("IsActive status is missing for the update.");
            throw new IllegalArgumentException("IsActive status is required.");
        }

        // Fetch the existing trainee
        log.info("Fetching existing trainee with username: {}", username);
        Optional<Trainee> existingTraineeOptional = traineeRepository.findByUser_UserName(username);
        if (existingTraineeOptional.isEmpty()) {
            log.error("Trainee with username {} not found.", username);
            throw new IllegalArgumentException("Trainee not found.");
        }

        // Generate new username and password
        log.info("Generating a unique username and password for the trainee.");
        newTraineeDTO.setPassword(generatePassword());
        newTraineeDTO.setUserName(generateUniqueUsername(newTraineeDTO.getFirstName(), newTraineeDTO.getLastName()));

        Trainee existingTrainee = existingTraineeOptional.get();
        User existingUser = existingTrainee.getUser();

        // Update user details
        log.info("Updating user details.");
        existingUser.setFirstName(newTraineeDTO.getFirstName());
        existingUser.setLastName(newTraineeDTO.getLastName());
        existingUser.setUserName(newTraineeDTO.getUserName());
        existingUser.setPassword(newTraineeDTO.getPassword());
        existingUser.setIsActive(newTraineeDTO.getIsActive());


        // Update trainee details if they exist
        if (newTraineeDTO.getDob() != null) {
            log.info("Updating date of birth.");
            existingTrainee.setDob(newTraineeDTO.getDob());
        }

        if (newTraineeDTO.getAddress() != null) {
            log.info("Updating address.");
            existingTrainee.setAddress(newTraineeDTO.getAddress());
        }

        // Updating trainees trainer list
        if (newTraineeDTO.getTrainers() != null) {
            existingTrainee.setTrainers(newTraineeDTO.getTrainers());
        }

        // Save the updated trainee
        log.info("Saving the updated trainee.");
        existingTrainee.setUser(existingUser);
        Trainee savedTrainee = traineeRepository.save(existingTrainee);
        log.info("Successfully updated trainee with username: {}", username);

        return savedTrainee;
    }


    @Override
    public Trainee updateTrainersList(String username, List<Trainer> trainerList) {
        log.info("Attempting to update trainees trainer list with username: {}", username);

        // Fetch the existing trainee
        log.info("Fetching existing trainee with username: {}", username);
        Optional<Trainee> existingTraineeOptional = traineeRepository.findByUser_UserName(username);
        if (existingTraineeOptional.isEmpty()) {
            log.error("Trainee with username {} not found.", username);
            throw new IllegalArgumentException("Trainee not found.");
        }

        Trainee existingTrainee = existingTraineeOptional.get();

        Set<Trainer> trainerSet = new HashSet<>();
        for (Trainer trainer : trainerList) {
            trainer.getUser().setUserName(generateUniqueUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName()));
            trainer.getUser().setPassword(generatePassword());

            trainerSet.add(trainer);
        }

        existingTrainee.setTrainers(trainerSet);

        // Save the updated trainee
        log.info("Saving the updated trainees trainers.");
        Trainee savedTrainee = traineeRepository.save(existingTrainee);
        log.info("Successfully updated trainees trainers with username: {}", username);

        return savedTrainee;
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.info("Deleting trainee with username: {}", username);

        Optional<Trainee> existingTraineeOptional = traineeRepository.findByUser_UserName(username);
        if (existingTraineeOptional.isEmpty()) {
            log.error("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee not found");
        }

        try {
            // Delete the trainee; the associated User and join table entries will be deleted automatically
            traineeRepository.delete(existingTraineeOptional.get());
            log.info("Successfully deleted trainee with username: {}", username);
        } catch (Exception e) {
            log.error("Error occurred while deleting trainee with username {}: {}", username, e.getMessage());
            throw new RuntimeException("An error occurred while deleting the trainee", e);  // Re-throw or handle as needed
        }

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
        Optional<Trainee> traineeOptional = traineeRepository.findByUser_UserName(username);
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            User user = trainee.getUser();
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

        Optional<Trainee> traineeOptional = traineeRepository.findByUser_UserName(username);
        if (traineeOptional.isEmpty()) {
            log.error("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee not found");
        }

        Trainee trainee = traineeOptional.get();
        trainee.getUser().setIsActive(isActive);
        traineeRepository.save(trainee);

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