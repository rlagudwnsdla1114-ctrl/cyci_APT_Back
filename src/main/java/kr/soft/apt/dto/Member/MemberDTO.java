package kr.soft.apt.dto.Member;

import lombok.Data;

@Data
public class MemberDTO {
    private long cIdx;
    private String cEmail;
    private String cName;
    private String cRegistration;
    private String cPhone;
    private String cSize;
    private String cRegion;

    private long jIdx;
    private String jEmail;
    private String jName;
    private String jBirth;
    private String jPhone;
}