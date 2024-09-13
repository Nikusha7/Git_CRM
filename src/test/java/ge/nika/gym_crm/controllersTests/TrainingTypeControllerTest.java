package ge.nika.gym_crm.controllersTests;

import ge.nika.gym_crm.controllers.TrainingTypesController;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.repositories.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class TrainingTypeControllerTest {
    @Autowired
    TrainingTypesController trainingTypeController;

    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @Test
    void testGetTrainingTypes() {
        // Arrange: Create the expected list of training types
        List<TrainingType> expectedTrainingTypes = List.of(
                new TrainingType(1, TrainingTypeNames.CARDIO),
                new TrainingType(2, TrainingTypeNames.STRENGTH),
                new TrainingType(3, TrainingTypeNames.FLEXIBILITY),
                new TrainingType(4, TrainingTypeNames.BALANCE)
        );

        // Act: Get the actual list from the controller
        List<TrainingType> actualTrainingTypes = trainingTypeController.getTrainingTypes();

        // Assert: Check that the actual list matches the expected list
        assertThat(actualTrainingTypes)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedTrainingTypes);
    }
}
