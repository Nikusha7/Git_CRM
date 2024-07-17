package ge.nika.gym_crm.entities;

public class Trainer extends User {
    private String specialization;
    private Integer userId;
    private TrainingType trainingType;

    public Trainer(String firstName, String lastName, String userName, Boolean isActive,
                   String specialization, Integer userId, TrainingType trainingType) {
        super(firstName, lastName, userName, isActive);
        this.specialization = specialization;
        this.userId = userId;
        this.trainingType = trainingType;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    @Override
    public String toString() {
        return "Trainer{ userId " + userId + '\'' +
                ", " + super.toString() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", trainingType=" + trainingType +
                "} ";
    }
}