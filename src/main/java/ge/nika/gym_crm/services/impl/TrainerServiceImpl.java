package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DTO.TraineeDTO;
import ge.nika.gym_crm.DTO.TrainerDTO;
import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.User;
import ge.nika.gym_crm.repositories.TraineeRepository;
import ge.nika.gym_crm.repositories.TrainerRepository;
import ge.nika.gym_crm.repositories.UserRepository;
import ge.nika.gym_crm.services.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Trainer create(TrainerDTO trainerDTO) {
        log.info("Creating a new trainer with DTO: {}", trainerDTO);

        User user = new User(trainerDTO.getFirstName(), trainerDTO.getLastName(), trainerDTO.getIsActive());
        user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());

        Trainer trainer = new Trainer(trainerDTO.getSpecialization());

        User savedUser = userRepository.save(user);
        trainer.setUser(savedUser);

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
        }

        return trainer.orElse(null);
    }

    @Override
    @Transactional
    public Trainer update(Integer id, Trainer trainer) {
        log.info("Updating trainer with ID: {}", id);

        Optional<Trainer> existingTrainerOptional = trainerRepository.findById(id);
        if (existingTrainerOptional.isEmpty()) {
            log.error("Trainer with ID {} not found", id);
            throw new IllegalArgumentException("Trainer not found");
        }

        trainer.getUser().setPassword(generatePassword());
        trainer.getUser().setUserName(generateUniqueUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName()));

        Trainer existingTrainer = existingTrainerOptional.get();
        User existingUser = existingTrainer.getUser();
        User updatedUser = trainer.getUser();

        // Update user details
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setIsActive(updatedUser.getIsActive());

        // Save the updated user
        userRepository.save(existingUser);

        // Update trainer details
        existingTrainer.setSpecialization(trainer.getSpecialization());
        // Save the updated trainer
        Trainer updatedTrainer = trainerRepository.save(existingTrainer);

        log.info("Updated trainer with ID: {}", id);
        return updatedTrainer;
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

        Optional<Trainer> trainerOptional = trainerRepository.findByUser_UserName(username);
        if (trainerOptional.isEmpty()) {
            log.error("Trainer with username {} not found", username);
            throw new IllegalArgumentException("Trainer not found");
        }

        Trainer trainer =trainerOptional.get();
        User user = trainer.getUser();
        user.setPassword(password);

        userRepository.save(user);
        trainer.setUser(user);

        trainerRepository.save(trainer);

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

        Optional<Trainer> trainerOptional = trainerRepository.findByUser_UserName(username);
        if (trainerOptional.isEmpty()) {
            log.error("Trainer with username {} not found", username);
            throw new IllegalArgumentException("Trainer not found");
        }

        Trainer trainer = trainerOptional.get();
        User user = trainer.getUser();
        user.setIsActive(isActive);

        userRepository.save(user);
        trainer.setUser(user);

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
}
