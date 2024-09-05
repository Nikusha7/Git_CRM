package ge.nika.gym_crm.DTO;

import ge.nika.gym_crm.entities.Trainer;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Setter
@Getter
public class TraineeDTO extends UserDTO{
    private LocalDate dob;
    private String address;
    private Set<Trainer> trainers;

    public TraineeDTO(String firstName, String lastName, Boolean isActive,
                      LocalDate dob, String address, Set<Trainer> trainers) {
        super(firstName, lastName, isActive);
        this.dob = dob;
        this.address = address;
        this.trainers = trainers;
    }

    public TraineeDTO(String firstName, String lastName, Boolean isActive, LocalDate dob, String address) {
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
