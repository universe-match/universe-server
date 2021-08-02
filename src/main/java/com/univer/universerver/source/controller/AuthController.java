package com.univer.universerver.source.controller;

import com.univer.universerver.source.model.Role;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.dto.AuthTokenDTO;
import com.univer.universerver.source.model.request.LoginForm;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.repository.RoleRepository;
import com.univer.universerver.source.repository.UserRepository;
import com.univer.universerver.source.security.jwt.JwtProvider;
import com.univer.universerver.source.security.service.UserPrinciple;
import com.univer.universerver.source.service.AuthTokenService;
import com.univer.universerver.source.service.UserService;
import com.univer.universerver.source.utils.RoleName;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends HttpServlet {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthTokenService authTokenService;


    @ApiOperation(value="로그인",notes="로그인")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserid(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String ROLE=auth.getAuthorities().toString();

            UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

            Optional<User> user = userService.findUserLoginId(loginRequest.getUserid());
            userService.saveAccessTime(user.get());//마지막 접속시간 저장

            AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(userPrincipal.getUsername());
            String jwt = jwtProvider.generateJwtToken(authentication);
            Map<String, String> map =new HashMap<String, String>();
            map.put("ROLE", ROLE);
            map.put("accessToken", authTokenDTO.getAccessToken());
            map.put("jwt", authTokenDTO.getAccessToken());
            map.put("refreshToken", authTokenDTO.getRefreshToken());
            return ResponseEntity.ok(map);
        }catch(Exception e) {
            //e.printStackTrace();
            return new ResponseEntity<>("아이디나 비밀번호를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value="회원가입",notes="회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpForm signUpRequest) {

        if(userRepository.existsByNickname(signUpRequest.getNickname())) {
            return new ResponseEntity<String>("닉네임이 이미 존재합니다!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUserid(signUpRequest.getUserid())) {
            return new ResponseEntity<String>("아이디가 이미 존재합니다!",
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // Creating user's account
            userService.registerUser(signUpRequest);
            //이메일 인증
//            if(!user.getEmail().isEmpty()) {
//                verificationTokenService.sendAuthEmail(user.getId(), user.getEmail());
//            }
            return new ResponseEntity<>("성공적으로 가입되었습니다.", HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @ApiOperation(value="토큰재발행",notes="토큰재발행")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam(name = "refresh_token") String refreshToken) throws IOException {
        try {
            AuthTokenDTO authTokenDTO = authTokenService.refresh(refreshToken);
            Map<String, String> map =new HashMap<String, String>();
            map.put("accessToken", authTokenDTO.getAccessToken());
            map.put("jwt", authTokenDTO.getAccessToken());
            map.put("refreshToken", authTokenDTO.getRefreshToken());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}