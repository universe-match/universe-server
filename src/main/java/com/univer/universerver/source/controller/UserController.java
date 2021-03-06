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
    @ApiOperation(value="??????????????????",notes="??????????????????")
    @GetMapping("/otherinfo/{id}")
    public ResponseEntity<?> myInfo(Principal principal,@PathVariable(name = "id") long id) {
        Optional<User> user = userService.findUserId(id);
        return ResponseEntity.ok(new UserResponse(user.get()));
    }
    @ApiOperation(value="???????????????",notes="???????????????")
    @GetMapping("/myinfo")
    public ResponseEntity<?> myInfo(Principal principal) {
        Optional<User> user = userService.findMyUserInfo(principal.getName());
        return ResponseEntity.ok(new UserResponse(user.get()));
    }
    @ApiOperation(value="???????????????",notes="???????????????")
    @PatchMapping("/myinfo/update")
    public ResponseEntity<?> myInfoUpdate(@RequestBody  UserRequest userRequest) {
//        Optional<User> user = userService.findMyUserInfo(principal.getName());
        userService.updateUser(userRequest);
        return ResponseEntity.ok("?????????????????????.");
    }

    @ApiOperation(value="????????????",notes="????????????")
    @DeleteMapping("/myinfo/delete")
    public ResponseEntity<?> myInfoDelete(Principal principal) {
        userService.myInfoDelete(principal.getName());
        return ResponseEntity.ok("???????????? ????????????????????? ????????? ??? ?????? ?????? ?????????.");
    }
    @ApiOperation(value="?????? ??????",notes="?????? ??????")
    @PatchMapping("/update/noti")
    public ResponseEntity<?> myInfoDelete(Principal principal,@RequestBody Map<String, Boolean> param) {
        boolean noti = param.get("noti");
        boolean rtnNoti = userService.myInfoNotiUpdate(principal.getName(),noti);
        return ResponseEntity.ok(rtnNoti);
    }
    @ApiOperation(value="????????????",notes="????????????")
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpForm signUpRequest) {

       userService.registerUser(signUpRequest);
            //????????? ??????
//            if(!user.getEmail().isEmpty()) {
//                verificationTokenService.sendAuthEmail(user.getId(), user.getEmail());
//            }
       return new ResponseEntity<>("??????????????? ?????????????????????.", HttpStatus.OK);
    }
    @ApiOperation(value="????????? ??????",notes="????????? ??????")
    @GetMapping("/nicknamecheck")
    public ResponseEntity<?> duplCheckNickname(@RequestParam(name = "nickName") String nickName){
        Optional<User> user = userService.selectNickname(nickName);
        if(user.isPresent()){
            return new ResponseEntity<>("?????? ???????????? ??????????????????.", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("??????????????? ??????????????????.", HttpStatus.OK);
        }
    }
    @ApiOperation(value="????????? ??????",notes="????????? ??????")
    @GetMapping("/idcheck")
    public ResponseEntity<?> duplCheckUserId(@RequestParam(name="userId")  String userId){
        Optional<User> user = userService.checkUserId(userId);
        if(user.isPresent()){
            return new ResponseEntity<>("?????? ???????????? ??????????????????.", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("??????????????? ??????????????????.", HttpStatus.OK);
        }
    }
    @ApiOperation(value="??????????????? ??????",notes="??????????????? ??????")
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
    @ApiOperation(value="?????? ?????????",notes="?????? ?????????")
    @PatchMapping("/valid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> validUser(Principal principal,@RequestBody AdminUserRequest adminUserRequest) {
        userService.validUser(adminUserRequest);
        return ResponseEntity.ok("????????? ???????????????.");
    }
    @ApiOperation(value="?????? ??????",notes="?????? ??????")
    @PatchMapping("/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectUser(Principal principal,@RequestBody AdminUserRequest adminUserRequest) {
        userService.rejectUser(adminUserRequest);
        return ResponseEntity.ok("?????? ???????????????.");
    }
    @ApiOperation(value="??????????????? ??????",notes="??????????????? ??????")
    @GetMapping("/university")
    public ResponseEntity<?> getUniversity(@RequestParam String name) {
        List<University> universities = userService.findUniversity(name);
        return ResponseEntity.ok(universities);
    }
    @ApiOperation(value="????????? ??????",notes="????????? ??????")
    @GetMapping("/interested")
    public ResponseEntity<?> getInterested() {
        List<Common> interested = userService.findInterested();
        return ResponseEntity.ok(interested);
    }
//    List<MatchRoomResponse> matchRoomRes = matchRoom
//            .stream()
//            .map(item->new MatchRoomResponse(item))
//            .collect(Collectors.toList());
//    @ApiOperation(value="???????????????",notes="???????????????")
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
//            return new ResponseEntity<>("?????? ??????..???????????? ??? ??????????????????.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
}
