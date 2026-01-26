package kr.soft.apt.dto.AI;

import lombok.Data;

@Data
public class AIListTopDTO {
    private long mIdx;
    private String cName;
    private String jobPos;
    private int mRate;
    private String mDate;
    private String mStatus;
    private String aiReason;
}
