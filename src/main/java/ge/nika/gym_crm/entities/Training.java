package ge.nika.gym_crm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id", nullable = false)
    private Trainee trainee;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
    private TrainingType trainingType;

    @Column(nullable = false)
    private Date trainingDate;
    @Column(nullable = false)
    private Integer trainingDuration;

    public Training(Trainer trainer, Trainee trainee, String trainingName, TrainingType trainingType,
                    Date trainingDate, Integer trainingDuration) {
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }
}
