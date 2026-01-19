package kr.soft.apt.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRes {
    private String accessToken;
    private Long accessExpiresInMs;

    // refresh rotate(재발급)까지 할거면 내려주고, 쿠키로만 쓰면 null로 둬도 됨
    private String refreshToken;
    private Long refreshExpiresInMs;
}
