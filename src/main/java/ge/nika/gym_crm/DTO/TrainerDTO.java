package ge.nika.gym_crm.DTO;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class TrainerDTO extends UserDTO{
    private TrainingType specialization;
    private Set<Trainee> trainees;

    public TrainerDTO(String firstName, String lastName, Boolean isActive,
                      TrainingType specialization, Set<Trainee> trainees) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
        this.trainees = trainees;
    }

    public TrainerDTO(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
    }

}
