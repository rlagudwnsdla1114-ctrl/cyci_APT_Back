package kr.soft.apt.service.SignUp;
import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.SignUp.CompanyLoginDTO;
import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import kr.soft.apt.dto.SignUp.JobseekerLoginDTO;
import kr.soft.apt.mapper.SignUp.CompanyUserMapper;
import kr.soft.apt.service.RedisTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void signup(CompanyUserDTO dto){

        // 1) 회사 규모 변환을 먼저
        dto.setCompanySize(dto.getCompanySize());

        // 2) 비밀번호 해시
        String hashedPw = encoder.encode(dto.getCompanyPassword());
        dto.setCompanyPassword(hashedPw);

        // 3) DB 저장은 딱 1번만
        companyUserMapper.signup(dto);
    }

    public String login(CompanyLoginDTO dto){
        CompanyLoginDTO resultDTO = companyUserMapper.login(dto.getCompanyEmail());
        if (resultDTO == null) return null;

        // ✅ 암호 검증 (raw vs hashed)
        if (!encoder.matches(dto.getCompanyPassword(), resultDTO.getCompanyPassword())) {
            return null;
        }

        String id = resultDTO.getCompanyEmail();
        long idx = resultDTO.getCompanyIdx();
        String redisKey = "company:" + idx;

        // JWT 생성
        String accessToken = jwtTokenProvider.createAccessToken(idx, id);

        // Redis 등록
        redisTokenService.saveAccessToken(redisKey, accessToken);

        return accessToken;
    }
}
