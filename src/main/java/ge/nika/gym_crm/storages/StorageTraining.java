package ge.nika.gym_crm.storages;

import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class StorageTraining {
    private static final Logger log = LoggerFactory.getLogger(StorageTraining.class);
    private final Map<String, Training> trainingStorage = new HashMap<>();

    @Value("${training.data.file.path}")
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
                Integer trainerId = Integer.parseInt(parts[0]);
                Integer traineeId = Integer.parseInt(parts[1]);
                String trainingName = parts[2];
                TrainingType trainingType = TrainingType.valueOf(parts[3]);
                Date trainingDate = parseDate(parts[4]);
                String trainingDuration = parts[5];

                Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
                trainingStorage.put(trainerId+""+traineeId, training);  // Use hashCode as the key

                log.info("Loaded training: " + training);
            }
        } catch (IOException | ParseException e) {
            log.error("Error occurred while reading " + dataFilePath + ": \n" + e.getMessage());
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateStr);
    }

    public Map<String, Training> getTrainingStorage() {
        return trainingStorage;
    }
}
