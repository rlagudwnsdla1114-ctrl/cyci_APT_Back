package kr.soft.apt.dto.AI.JobMatchSelect;

import lombok.Data;

@Data
public class SelectJobMatchDTO {
    private long mIdx;
    private String cName;
    private String jobPos;
    private int mRate;
    private String mDate;
    private String mStatus;
}
