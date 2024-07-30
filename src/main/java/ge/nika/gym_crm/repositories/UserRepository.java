package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

//    checking if username exists in user table, usable for both trainee and trainer to generate unique
    boolean existsByUserName(String username);

}
