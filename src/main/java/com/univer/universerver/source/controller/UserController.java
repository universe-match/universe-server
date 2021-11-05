package com.univer.universerver.source.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import com.univer.universerver.source.model.Common;
import com.univer.universerver.source.model.University;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.request.UserRequest;
import com.univer.universerver.source.model.request.admin.AdminUserRequest;
import com.univer.universerver.source.model.response.AdminUserResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.model.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.UserException;
import com.univer.universerver.source.model.dto.user.JwtResponse;
import com.univer.universerver.source.model.dto.user.LoginRequest;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.repository.RoleRepository;
import com.univer.universerver.source.security.jwt.JwtUtils;
import com.univer.universerver.source.security.service.UserPrinciple;
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
    JwtUtils jwtUtils;

    @Autowired
    RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUser() {
        return "";
    }
//    @ApiOperation(value="로그인",notes="로그인")
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest, HttpServletResponse response) {
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUserid(),
//                            loginRequest.getPassword()
//                    )
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String ROLE=auth.getAuthorities().toString();
//
//            UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
//
//            Optional<User> user = userService.findUserLoginId(loginRequest.getUserid());
//            userService.saveAccessTime(user.get());//마지막 접속시간 저장
//
//            AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(userPrincipal.getUsername());
//            String jwt = jwtProvider.generateJwtToken(authentication);
//            Map<String, String> map =new HashMap<String, String>();
//            map.put("ROLE", ROLE);
//            map.put("accessToken", authTokenDTO.getAccessToken());
//            map.put("refreshToken", authTokenDTO.getRefreshToken());
//            return ResponseEntity.ok(map);
//        }catch(Exception e) {
//            //e.printStackTrace();
//            return new ResponseEntity<>("아이디나 비밀번호를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword()));

		try {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			
			UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();		
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

			userService.createFcmToken(authentication.getName(),loginRequest.getFcmToken());

			return ResponseEntity.ok(new JwtResponse(jwt, 
													 userDetails.getId(), 
													 userDetails.getUsername(), 
													 userDetails.getEmail(), 
													 roles));	
		}catch(Exception e) {
			throw new UserException(ErrorCode.ERR_ID_PASSWORD); 
		}
		
	}
    @ApiOperation(value="타인정보조회",notes="타인정보조회")
    @GetMapping("/otherinfo/{id}")
    public ResponseEntity<?> myInfo(Principal principal,@PathVariable(name = "id") long id) {
        Optional<User> user = userService.findUserId(id);
        return ResponseEntity.ok(new UserResponse(user.get()));
    }
    @ApiOperation(value="내정보조회",notes="내정보조회")
    @GetMapping("/myinfo")
    public ResponseEntity<?> myInfo(Principal principal) {
        Optional<User> user = userService.findMyUserInfo(principal.getName());
        return ResponseEntity.ok(new UserResponse(user.get()));
    }
    @ApiOperation(value="내정보수정",notes="내정보수정")
    @PatchMapping("/myinfo/update")
    public ResponseEntity<?> myInfoUpdate(@RequestBody  UserRequest userRequest) {
//        Optional<User> user = userService.findMyUserInfo(principal.getName());
        userService.updateUser(userRequest);
        return ResponseEntity.ok("수정되었습니다.");
    }

    @ApiOperation(value="회원탈퇴",notes="회원탈퇴")
    @DeleteMapping("/myinfo/delete")
    public ResponseEntity<?> myInfoDelete(Principal principal) {
        userService.myInfoDelete(principal.getName());
        return ResponseEntity.ok("서비스를 이용할수없으며 일주일 뒤 완전 삭제 됩니다.");
    }
    @ApiOperation(value="알림여부체크",notes="알림여부체크")
    @PatchMapping("/myinfo/noti")
    public ResponseEntity<?> myInfoDelete(Principal principal,@RequestBody Map<String, String> param) {
        String noti = param.get("noti");
        long notiNumber = userService.myInfoNotiUpdate(principal.getName(),noti);
        return ResponseEntity.ok(notiNumber);
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
    @ApiOperation(value="닉네임 체크",notes="닉네임 체크")
    @GetMapping("/nicknamecheck")
    public ResponseEntity<?> duplCheckNickname(@RequestParam(name = "nickName") String nickName){
        Optional<User> user = userService.selectNickname(nickName);
        if(user.isPresent()){
            return new ResponseEntity<>("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("사용가능한 닉네임입니다.", HttpStatus.OK);
        }
    }
    @ApiOperation(value="아이디 체크",notes="아이디 체크")
    @GetMapping("/idcheck")
    public ResponseEntity<?> duplCheckUserId(@RequestParam(name="userId")  String userId){
        Optional<User> user = userService.checkUserId(userId);
        if(user.isPresent()){
            return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("사용가능한 아이디입니다.", HttpStatus.OK);
        }
    }
    @ApiOperation(value="유저리스트 조회",notes="유저리스트 조회")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserList(Principal principal) {
        List<User> users = userService.findAllUsr();

        logger.debug("users",users);

        List<AdminUserResponse> userRes = users
                                        .stream()
                                        .map(user->new AdminUserResponse(user))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(userRes);
    }
    @ApiOperation(value="유저 활성화",notes="유저 활성화")
    @PatchMapping("/valid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> validUser(Principal principal,@RequestBody AdminUserRequest adminUserRequest) {
        userService.validUser(adminUserRequest);
        return ResponseEntity.ok("활성화 되었습니다.");
    }
    @ApiOperation(value="대학리스트 조회",notes="대학리스트 조회")
    @GetMapping("/university")
    public ResponseEntity<?> getUniversity(@RequestParam String name) {
        List<University> universities = userService.findUniversity(name);
        return ResponseEntity.ok(universities);
    }
    @ApiOperation(value="관심사 조회",notes="관심사 조회")
    @GetMapping("/interested")
    public ResponseEntity<?> getInterested() {
        List<Common> interested = userService.findInterested();
        return ResponseEntity.ok(interested);
    }
//    List<MatchRoomResponse> matchRoomRes = matchRoom
//            .stream()
//            .map(item->new MatchRoomResponse(item))
//            .collect(Collectors.toList());
//    @ApiOperation(value="토큰재발행",notes="토큰재발행")
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refresh(@RequestParam(name = "refresh_token") String refreshToken) throws IOException {
//        try {
//            AuthTokenDTO authTokenDTO = authTokenService.refresh(refreshToken);
//            Map<String, String> map =new HashMap<String, String>();
//            map.put("accessToken", authTokenDTO.getAccessToken());
//            map.put("jwt", authTokenDTO.getAccessToken());
//            map.put("refreshToken", authTokenDTO.getRefreshToken());
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }catch(Exception e) {
//            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
}
