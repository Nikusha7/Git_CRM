package ge.nika.gym_crm.servicesTests;

import ge.nika.gym_crm.entities.Trainer;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.services.impl.TrainerServiceImpl;
import ge.nika.gym_crm.storages.StorageTrainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    private StorageTrainer storageTrainerMock;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    public void testCreateTrainer() {

    }

}
