package ge.nika.gym_crm.configuration;

import ge.nika.gym_crm.entities.*;
import ge.nika.gym_crm.repositories.TrainingTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class TrainingTypeConfig {

    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @PostConstruct
    public void initializeTrainingTypes() {
        /*
        Idempotency: The check if (trainingTypeRepository.count() == 0)
        ensures that the initialization code runs only if there are no existing records,
        preventing duplicate entries.
        */

        if (trainingTypeRepository.count() == 0) {
            List<TrainingType> trainingTypes = Arrays.asList(
                    new TrainingType(1, TrainingTypeNames.CARDIO),
                    new TrainingType(2, TrainingTypeNames.STRENGTH),
                    new TrainingType(3, TrainingTypeNames.FLEXIBILITY),
                    new TrainingType(4, TrainingTypeNames.BALANCE)
            );
            trainingTypeRepository.saveAll(trainingTypes);
        }

    }
}
