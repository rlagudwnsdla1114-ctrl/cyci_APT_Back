package kr.soft.apt.dto.UserInfo;

import lombok.Data;

@Data
public class JobseekerInfoDTO {
    private String jobseekerName;
    private String jobseekerBirth; // DB가 varchar(45)라 String
}