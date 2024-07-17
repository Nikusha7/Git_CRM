package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Override
    public void create() {
        logger.info("Training has been created!");
    }

    @Override
    public Training select() {
        System.out.println("Training has been selected!");
        return null;
    }
}
