package ge.nika.gym_crm.repositories;

import ge.nika.gym_crm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
}
