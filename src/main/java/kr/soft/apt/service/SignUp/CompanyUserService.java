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

        companyUserMapper.signup(dto);


        String sizeStr = dto.getCompanySize();
        String convertedSize = "0";

        if (sizeStr != null) {
            switch (sizeStr) {
                case "1~10명":      convertedSize = "1"; break;
                case "11~50명":     convertedSize = "2"; break;
                case "51~200명":    convertedSize = "3"; break;
                case "201~1000명":  convertedSize = "4"; break;
                case "1000명 이상": convertedSize = "5"; break;
            }
        }

        dto.setCompanySize(convertedSize);
        companyUserMapper.signup(dto);



    }


    public String login(CompanyLoginDTO dto){
        CompanyLoginDTO resultDTO=companyUserMapper.login(dto.getCompanyEmail());
        if (resultDTO == null) return null;

        if (!encoder.matches(dto.getCompanyPassword(), resultDTO.getCompanyPassword())) {
            return null;
        }
        String id = resultDTO.getCompanyEmail();
        long idx = resultDTO.getCompanyIdx();
        String key = "company:" + idx;


        //2. JWT 토큰 만들기
        String accessToken = jwtTokenProvider.createAccessToken(idx, id);

        //3. Redis 등록 (access:userId 형태)
        redisTokenService.saveAccessToken(key, accessToken);

        return accessToken;
    }


}