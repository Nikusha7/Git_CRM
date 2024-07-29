package ge.nika.gym_crm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @Column(nullable = false)
    private Integer trainerId;
    @Column(nullable = false)
    private Integer traineeId;
    @Column(nullable = false)
    private String trainingName;
    @Column(nullable = false)
    private Integer trainingTypeId;
    @Column(nullable = false)
    private LocalDate trainingDate;
    @Column(nullable = false)
    private Integer trainingDuration;

}
