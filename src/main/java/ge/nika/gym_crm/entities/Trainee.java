package ge.nika.gym_crm.entities;

import java.time.LocalDate;
import java.util.Date;

public class Trainee extends User {
    private LocalDate dob;
    private String address;
    private Integer userId;

    public Trainee(String firstName, String lastName, String userName, Boolean isActive, LocalDate dob, String address, Integer userId) {
        super(firstName, lastName, userName, isActive);
        this.dob = dob;
        this.address = address;
        this.userId = userId;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainee{ userId " + userId + '\'' +
                ", " + super.toString() + '\'' +
                "dob=" + dob +
                ", address='" + address + '\'' +
                "} ";
    }


}