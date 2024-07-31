package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Trainee create(TraineeDTO traineeDTO) {
        log.info("Creating a new trainee with DTO: {}", traineeDTO);

        User user = new User(traineeDTO.getFirstName(), traineeDTO.getLastName(), traineeDTO.getIsActive());
        user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());

        Trainee trainee = new Trainee(Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");

        User savedUser = userRepository.save(user);
        trainee.setUser(savedUser);

        Trainee savedTrainee = traineeRepository.save(trainee);
        log.info("Created trainee with ID: {}", savedTrainee.getId());

        return savedTrainee;
    }

    @Override
    public Trainee select(String userName) {
        log.info("Selecting trainee with username: {}", userName);

        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(userName);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username {} not found", userName);
        } else {
            log.info("Trainee with username {} found", userName);
        }

        return trainee.orElse(null);
    }

    @Override
    @Transactional
    public Trainee update(Integer id, Trainee trainee) {
        log.info("Updating trainee with ID: {}", id);

        Optional<Trainee> existingTraineeOptional = traineeRepository.findById(id);
        if (existingTraineeOptional.isEmpty()) {
            log.error("Trainee with ID {} not found", id);
            throw new IllegalArgumentException("Trainee not found");
        }

        trainee.getUser().setPassword(generatePassword());
        trainee.getUser().setUserName(generateUniqueUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName()));

        Trainee existingTrainee = existingTraineeOptional.get();
        User existingUser = existingTrainee.getUser();
        User updatedUser = trainee.getUser();

        // Update user details
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setIsActive(updatedUser.getIsActive());

        // Save the updated user
        userRepository.save(existingUser);

        // Update trainee details
        existingTrainee.setDob(trainee.getDob());
        existingTrainee.setAddress(trainee.getAddress());

        // Save the updated trainee
        Trainee savedTrainee = traineeRepository.save(existingTrainee);
        log.info("Updated trainee with ID: {}", id);

        return savedTrainee;
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.info("Deleting trainee with username: {}", username);

        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isEmpty()) {
            log.error("User with username: {} not found", username);
            throw new IllegalArgumentException("User with username: " + username + " not found");
        }

        User user = userOptional.get();

        // Find the Trainee by the user's ID
        Optional<Trainee> traineeOptional = traineeRepository.findByUserId(user.getId());
        if (traineeOptional.isEmpty()) {
            log.error("Trainee associated with the user not found");
            throw new IllegalArgumentException("Trainee associated with the user not found");
        }

        Trainee trainee = traineeOptional.get();

        // Delete the trainee
        traineeRepository.delete(trainee);

        // Delete the associated user
        userRepository.delete(user);

        log.info("Deleted trainee and associated user with username: {}", username);
    }

    @Override
    @Transactional
    public void changePassword(String username, String password) {
        log.info("Changing password for username: {}", username);

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            log.error("Password cannot be null or empty");
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 10) {
            log.error("Password must be at least 10 characters long");
            throw new IllegalArgumentException("Password must be at least 10 characters long");
        }

        Optional<Trainee> traineeOptional = traineeRepository.findByUser_UserName(username);
        if (traineeOptional.isEmpty()) {
            log.error("Trainee with username {} not found", username);
            throw new IllegalArgumentException("Trainee not found");
        }

        Trainee trainee = traineeOptional.get();
        User user = trainee.getUser();
        user.setPassword(password);

        userRepository.save(user);
        trainee.setUser(user);

        traineeRepository.save(trainee);
        log.info("Password changed successfully for username: {}", username);
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
        User user = trainee.getUser();
        user.setIsActive(isActive);

        userRepository.save(user);
        trainee.setUser(user);

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

}