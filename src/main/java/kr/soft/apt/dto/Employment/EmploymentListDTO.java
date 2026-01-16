package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentListDTO {
    private Long jobPostsIdx;
    private String title;
    private String postsCreatedAt;
    private Integer viewCount;
    private String companyName;
    private Long companyIdx;
}
