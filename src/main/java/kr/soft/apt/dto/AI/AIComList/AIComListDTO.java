package kr.soft.apt.dto.AI.AIComList;

import lombok.Data;

@Data
public class AIComListDTO {
    private Long jobSeekerIdx;
    private String name;
    private String keySkill;
    private Integer matchRate;
    private String matchDate;
    private String comAiReason;

}