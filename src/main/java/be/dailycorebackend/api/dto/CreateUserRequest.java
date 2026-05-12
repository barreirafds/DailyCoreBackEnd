package be.dailycorebackend.api.dto;

public class CreateUserRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}