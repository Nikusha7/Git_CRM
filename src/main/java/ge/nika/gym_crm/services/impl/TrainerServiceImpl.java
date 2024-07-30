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
        User user = new User(trainerDTO.getFirstName(), trainerDTO.getLastName(), trainerDTO.getIsActive());
        user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());

        Trainer trainer = new Trainer(trainerDTO.getSpecialization());

        User savedUser = userRepository.save(user);
        trainer.setUser(savedUser);

        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer select(String userName) {
        Optional<Trainer> trainer = trainerRepository.findByUser_UserName(userName);
        return trainer.orElse(null);
    }

    @Override
    @Transactional
    public Trainer update(Integer id, Trainer trainer) {
        Optional<Trainer> existingTrainerOptional = trainerRepository.findById(id);
        if (existingTrainerOptional.isEmpty()) {
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
        return trainerRepository.save(existingTrainer);
    }


    public String generatePassword() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(Character::isLetterOrDigit)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;

        while (checkUsernameExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }

    private boolean checkUsernameExists(String username) {
        return userRepository.existsByUserName(username);
    }
}
