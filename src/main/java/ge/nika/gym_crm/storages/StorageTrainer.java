package ge.nika.gym_crm.storages;

import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class StorageTrainer {
    private static final Logger log = LoggerFactory.getLogger(StorageTrainer.class);
    private final Map<Integer, Trainer> trainerStorage = new HashMap<>();

    @Value("${trainer.data.file.path}")
    private String dataFilePath;

    @PostConstruct
    public void init() {

        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
//                skips the header
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip header line
                }

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                boolean isActive = Boolean.parseBoolean(parts[3]);
                String specialization = parts[4];
                TrainingType trainingType = TrainingType.valueOf(parts[5]);
                Trainer trainer = new Trainer(firstName, lastName, isActive, specialization, id, trainingType);

                trainer.setUserName(generateUniqueUsername(firstName, lastName));
                trainer.setPassword(generatePassword());

                trainerStorage.put(id, trainer);
                log.info("Trainer initialized: " + trainer);
            }
        } catch (IOException e) {
            log.error("Error occurred while reading Trainer data from file: {}", dataFilePath, e);
        }

    }

    private String generateUniqueUsername(String firstName, String lastName) {
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
        return trainerStorage.values().stream()
                .anyMatch(trainer -> trainer.getUserName().equals(username));
    }

    private String generatePassword() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> Character.isLetterOrDigit(i))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Map<Integer, Trainer> getTrainerStorage() {
        return trainerStorage;
    }


}