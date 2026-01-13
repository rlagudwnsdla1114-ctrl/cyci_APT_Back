package kr.soft.apt.dto.SignUp;


import lombok.Data;

@Data
public class CompanyUserDTO {



    private String email;
    private String password;
    private String region;


    private String companyName;
    private String bizNumber;
    private String bizPhone;
    private String companySize;

}
