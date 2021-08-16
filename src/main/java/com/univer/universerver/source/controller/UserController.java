package com.univer.universerver.source.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.univer.universerver.source.config.S3Uploader;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.dto.AuthTokenDTO;
import com.univer.universerver.source.model.dto.MatchingDTO;
import com.univer.universerver.source.model.request.LoginForm;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.repository.RoleRepository;
import com.univer.universerver.source.security.jwt.JwtProvider;
import com.univer.universerver.source.security.service.UserPrinciple;
import com.univer.universerver.source.service.AuthTokenService;
import com.univer.universerver.source.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/user")
public class UserController extends HttpServlet {

    @Autowired
    AuthenticationManager authenticationManager;

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


       userService.registerUser(signUpRequest);
            //이메일 인증
//            if(!user.getEmail().isEmpty()) {
//                verificationTokenService.sendAuthEmail(user.getId(), user.getEmail());
//            }
       return new ResponseEntity<>("성공적으로 가입되었습니다.", HttpStatus.OK);
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
