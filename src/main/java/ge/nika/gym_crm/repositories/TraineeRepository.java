package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.Trainee;
import ge.nika.gym_crm.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository
        extends JpaRepository<Trainee, Integer> {
    @Query("SELECT t FROM Trainee t " +
            "JOIN FETCH t.user u " +
            "LEFT JOIN FETCH t.trainers tr " +
            "WHERE u.userName = :username")
    Optional<Trainee> findByUser_UserName(@Param("username") String username);
//    List<Trainer> (@Param("username") String username)

}
