package ge.nika.gym_crm.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id", nullable = false)
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL)  // Ensure User is saved when Trainer is saved
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false )
    private User user;

    @JsonBackReference // Prevents infinite recursion during serialization
    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Trainee> trainees = new HashSet<>();

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
        trainee.getTrainers().add(this); // Ensure bidirectional consistency
    }

    public void removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
        trainee.getTrainers().remove(this); // Ensure bidirectional consistency
    }

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    public Trainer(TrainingType specialization, User user, Set<Trainee> trainees) {
        this.specialization = specialization;
        this.user = user;
        this.trainees = trainees;
    }

    public Trainer(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", specialization=" + specialization +
                ", user=" + user +
                '}';
    }

}
