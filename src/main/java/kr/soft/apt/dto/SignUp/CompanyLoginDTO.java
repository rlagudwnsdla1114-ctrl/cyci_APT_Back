package kr.soft.apt.dto.SignUp;

import lombok.Data;

@Data
public class CompanyLoginDTO {
    private long companyIdx;
    private String companyEmail;
    private String companyPassword;
}
