package kr.soft.apt.service.SignUp;


import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.JobseekerLoginDTO;
import kr.soft.apt.mapper.SignUp.JobSeekerUserMapper;
import kr.soft.apt.service.RedisTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobSeekerUserService {

    @Autowired
    private JobSeekerUserMapper jobSeekerUserMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RedisTokenService redisTokenService;
    @Autowired
    private BCryptPasswordEncoder encoder;


    public void jbSignup(JobSeekerUserDTO dto){

        String hashedPw = encoder.encode(dto.getJobSeekerPassword());
        dto.setJobSeekerPassword(hashedPw);

        jobSeekerUserMapper.signup(dto);

    }

    public String jbLogin(JobseekerLoginDTO dto){
        JobseekerLoginDTO resultDTO = jobSeekerUserMapper.login(dto.getJobseekerEmail());

        if (resultDTO == null) return null;

        if (!encoder.matches(dto.getJobseekerPassword(), resultDTO.getJobseekerPassword())) {
            return null;
        }
        String id = resultDTO.getJobseekerEmail();
        long idx = resultDTO.getJobseekerIdx();
        String key = "jobseeker:" + idx;


        //2. JWT 토큰 만들기
        String accessToken = jwtTokenProvider.createAccessToken(idx, id);

        //3. Redis 등록 (access:userId 형태)
        redisTokenService.saveAccessToken(key, accessToken);

        return accessToken;
    }


}
