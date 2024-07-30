package ge.nika.gym_crm.DTO;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
public class TrainingDTO {
    private Integer id;
    private Integer trainerId;
    private Integer traineeId;
    private String trainingName;
    private Integer trainingTypeId;
    private LocalDate trainingDate;
    private Integer trainingDuration;


}
