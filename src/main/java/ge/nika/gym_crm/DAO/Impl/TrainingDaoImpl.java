package ge.nika.gym_crm.DAO.Impl;

import ge.nika.gym_crm.DAO.TrainingDao;
import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.Training;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    @Override
    public void create(Training training) {

    }

    @Override
    public Training select(Integer integer) {
        return null;
    }

    @Override
    public Training select(Integer trainerId, Integer traineeId) {
        return null;
    }
}
