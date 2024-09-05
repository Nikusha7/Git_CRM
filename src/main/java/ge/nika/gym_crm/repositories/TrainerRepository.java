package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository
        extends JpaRepository<Trainer, Integer> {
    @Query("SELECT t FROM Trainer t " +
            "JOIN FETCH t.user u " +
            "LEFT JOIN FETCH t.trainees tr " +
            "WHERE u.userName = :username")
    Optional<Trainer> findByUser_UserName(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.isActive = true AND t.id NOT IN " +
            "(SELECT tr.id FROM Trainer tr JOIN tr.trainees tt WHERE tt.user.userName = :username)")
    List<Trainer> findActiveTrainersNotAssignedToTrainee(@Param("username") String username);

}
