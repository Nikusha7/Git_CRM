package ge.nika.gym_crm.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dob;
    private String address;
    //    @Column(nullable = false)
//    private Integer userId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    public Trainee(Integer id, User user) {
        this.id = id;
        this.user = user;
    }

    public Trainee(Date dob, String address, User user) {
        this.dob = dob;
        this.address = address;
        this.user = user;
    }

    public Trainee(Date dob, String address) {
        this.dob = dob;
        this.address = address;
    }

    public Trainee(User user) {
        this.user = user;
    }

}
