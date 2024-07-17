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
        // Mock behavior of storageTrainerMock to return a trainer when saveTrainer is called
        when(storageTrainerMock.saveTrainer(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(1); // Simulating storage assigning an ID
            return trainer;
        });

        // Create a trainer
        Trainer trainerToCreate = new Trainer("John", "Doe", "john.doe", true, "Physical balancing trainer", 1, TrainingType.BALANCE);
        trainerService.create(trainerToCreate);

        // Assert that the created trainer is not null and has an ID assigned
        assertNotNull(createdTrainer);
        assertEquals(1, createdTrainer.getId()); // Assuming storage assigns ID

        // Verify that saveTrainer method of storageTrainerMock was called exactly once with any Trainer object
        verify(storageTrainerMock, times(1)).saveTrainer(any(Trainer.class));
    }

}
