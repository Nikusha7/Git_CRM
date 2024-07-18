package ge.nika.gym_crm.entities;

import java.time.LocalDate;

public class Training {
    private Integer trainerId;
    private Integer traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private String trainingDuration;

    public Training(Integer trainerId, Integer traineeId, String trainingName, TrainingType trainingType,
                    LocalDate trainingDate, String trainingDuration) {
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(String trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainerId=" + trainerId +
                ", traineeId=" + traineeId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }
}