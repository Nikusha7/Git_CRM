package ge.nika.gym_crm.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Setter
@Getter
public class TraineeDTO extends UserDTO{
    private Date dob;
    private String address;

    public TraineeDTO(String firstName, String lastName, Boolean isActive,Date dob, String address) {
        super(firstName, lastName, isActive);
        this.dob = dob;
        this.address = address;
    }

    public TraineeDTO(String firstName, String lastName, Boolean isActive) {
        super(firstName, lastName, isActive);
    }

//
//    public TraineeDTO(Date dob, String address) {
//        super(String firstName, String lastName, Boolean isActive);
//        this.dob = dob;
//        this.address = address;
//
//    }
//
//    public TraineeDTO(UserDTO userDTO) {
//        super();
//        this.userDTO = userDTO;
//    }

}
