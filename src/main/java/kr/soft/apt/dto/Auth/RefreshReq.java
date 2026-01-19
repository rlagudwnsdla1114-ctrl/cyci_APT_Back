package kr.soft.apt.dto.Auth;

import lombok.Data;

@Data
public class RefreshReq {
    // 바디로 보낼 수도 있고(선택), 쿠키로만 받아도 됨
    private String refreshToken;
}
