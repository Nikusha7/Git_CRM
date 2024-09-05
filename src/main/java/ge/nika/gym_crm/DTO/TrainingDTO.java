package ge.nika.gym_crm.DTO;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
public class TrainingDTO {

    private Integer trainerId;
    private Integer traineeId;
    private String trainingName;
    private Integer trainingTypeId;
    private LocalDate trainingDate;
    private Integer trainingDuration;

    public TrainingDTO(Integer trainerId, Integer traineeId, String trainingName,
                       Integer trainingTypeId, LocalDate trainingDate, Integer trainingDuration) {
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.trainingName = trainingName;
        this.trainingTypeId = trainingTypeId;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

}
