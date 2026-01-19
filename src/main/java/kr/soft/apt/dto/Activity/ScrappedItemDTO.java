package kr.soft.apt.dto.Activity;

import lombok.Data;

@Data
public class ScrappedItemDTO {
    private Long jobPostsIdx;
    private String companyName;
    private String title;
    private String scrappedAt;
    private Integer viewCount;
    private String postsCreatedAt;
}
