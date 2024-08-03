package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.repositories.TrainingRepository;
import ge.nika.gym_crm.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training create(Training training) {
        log.info("Creating a new training session: {}", training);

        Training savedTraining = trainingRepository.save(training);

        log.info("Training session created with ID: {}", savedTraining.getId());
        return savedTraining;
    }

    @Override
    public Training select(Integer id) {
        log.info("Selecting training session with ID: {}", id);

        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty()) {
            log.warn("Training session with ID {} not found", id);
        } else {
            log.info("Training session with ID {} found", id);
        }

        return training.orElse(null);
    }

}
