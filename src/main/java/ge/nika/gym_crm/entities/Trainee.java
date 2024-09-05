package ge.nika.gym_crm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dob;
    private String address;

    @OneToOne(cascade = CascadeType.ALL)  // Ensure User is saved when Trainee is saved
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    @JsonManagedReference // Prevents infinite recursion during serialization
    private Set<Trainer> trainers = new HashSet<>();

    public Trainee(LocalDate dob, String address, User user, Set<Trainer> trainers) {
        this.dob = dob;
        this.address = address;
        this.user = user;
        this.trainers = trainers;
    }

    public Trainee(LocalDate dob, String address, User user) {
        this.dob = dob;
        this.address = address;
        this.user = user;
    }

    public Trainee(LocalDate dob, String address) {
        this.dob = dob;
        this.address = address;
    }

    public Trainee(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(id, trainee.id) && Objects.equals(dob, trainee.dob) && Objects.equals(address, trainee.address) && Objects.equals(user, trainee.user) && Objects.equals(trainers, trainee.trainers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dob, address, user, trainers);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
