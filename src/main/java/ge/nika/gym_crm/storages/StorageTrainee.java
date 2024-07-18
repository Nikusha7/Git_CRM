package ge.nika.gym_crm.storages;

import ge.nika.gym_crm.entities.Trainee;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class StorageTrainee {
    private static final Logger log = LoggerFactory.getLogger(StorageTrainee.class);
    private final Map<Integer, Trainee> traineeStorage = new HashMap<>();

    @Value("${trainee.data.file.path}")
    private String dataFilePath;

    @PostConstruct
    public void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip header line
                }

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                boolean isActive = Boolean.parseBoolean(parts[3]);
                LocalDate dob = LocalDate.parse(parts[4]);
                String address = parts[5];
                Trainee trainee = new Trainee(firstName, lastName, isActive, dob, address, id);

                trainee.setUserName(generateUniqueUsername(firstName, lastName));
                trainee.setPassword(generatePassword());

                traineeStorage.put(id, trainee);
                log.info("Trainee: "+trainee);
            }
        } catch (IOException e) {
            log.error("Error occurred while reading " + dataFilePath + ": \n" + e.getMessage());
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
        return traineeStorage.values().stream()
                .anyMatch(trainee -> trainee.getUserName().equals(username));
    }

    private String generatePassword() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> Character.isLetterOrDigit(i))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Map<Integer, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

}
