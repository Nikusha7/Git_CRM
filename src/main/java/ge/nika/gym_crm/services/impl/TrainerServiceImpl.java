package ge.nika.gym_crm.services.impl;

import ge.nika.gym_crm.DAO.Impl.TrainerDaoImpl;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.services.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerDaoImpl trainerDao;

    public TrainerServiceImpl(TrainerDaoImpl trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Override
    public void create(Trainer trainer) {
        trainerDao.create(trainer);

        log.info("Trainer: " + trainer + ", has been created");
    }

    @Override
    public void update(Integer userId, Trainer newTrainer) {
        if (trainerDao.select(userId) != null) {
            trainerDao.update(userId, newTrainer);
            log.info("Trainer with id: " + userId + ", has been updated");
        } else {
            log.warn("Trainer with id: " + userId + " not found for update");
        }
    }

    @Override
    public Trainer select(Integer userId) {
        Trainer trainer = trainerDao.select(userId);
        if (trainer != null) {
            log.info("Trainer with id: " + userId + ", has been selected/retrieved");
            return trainer;
        } else {
            log.warn("Trainer with id: " + userId + " not found");
        }
        return null;
    }

}