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
    User user = new User(traineeDTO.getFirstName(), traineeDTO.getLastName(), traineeDTO.getIsActive());
    user.setUserName(generateUniqueUsername(user.getFirstName(), user.getLastName()));
    user.setPassword(generatePassword());

    Trainee trainee = new Trainee(Date.valueOf(LocalDate.now()), "Georgia, Tbilisi");

    User savedUser = userRepository.save(user);
    trainee.setUser(savedUser);

        return traineeRepository.save(trainee);
    }

    @Override
    public Trainee select(String userName) {
        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(userName);
        return trainee.orElse(null);
    }

    @Override
    @Transactional
    public Trainee update(Integer id, Trainee trainee) {
        Optional<Trainee> existingTraineeOptional = traineeRepository.findById(id);
        if (existingTraineeOptional.isEmpty()) {
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
        return traineeRepository.save(existingTrainee);
    }

    @Override
    @Transactional
    public void delete(String username) {
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with username: " + username + " not found");
        }

        User user = userOptional.get();

        // Find the Trainee by the user's ID
        Optional<Trainee> traineeOptional = traineeRepository.findByUserId(user.getId());
        if (traineeOptional.isEmpty()) {
            throw new IllegalArgumentException("Trainee associated with the user not found");
        }

        Trainee trainee = traineeOptional.get();

        // Delete the trainee
        traineeRepository.delete(trainee);

        // Delete the associated user
        userRepository.delete(user);
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