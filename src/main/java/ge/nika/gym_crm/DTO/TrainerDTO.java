package ge.nika.gym_crm.DTO;

import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
public class TrainerDTO extends UserDTO{
    private TrainingType specialization;

    public TrainerDTO(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
    }

}
