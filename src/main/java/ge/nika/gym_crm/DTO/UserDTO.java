package ge.nika.gym_crm.DTO;

import ge.nika.gym_crm.entities.User;
import lombok.*;

@Setter
@Getter
public class UserDTO {
    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private Boolean isActive;

    public User getUser() {
        return new User(userName, firstName, lastName, password, isActive);
    }

    public UserDTO(String firstName, String lastName, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }


}
