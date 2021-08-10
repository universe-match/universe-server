package com.univer.universerver.source.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.UserDuplicateException;
import com.univer.universerver.source.model.Role;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.request.SignUpForm;
import com.univer.universerver.source.repository.RoleRepository;
import com.univer.universerver.source.repository.UserRepository;
import com.univer.universerver.source.utils.RoleName;

@Service

public class UserService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    public void registerUser (SignUpForm signUpForm)
    {
    	
		if(userRepository.existsByUserid(signUpForm.getUserid())) {
			throw new UserDuplicateException(ErrorCode.USERID_DUPLICATION);
	    }
        if(userRepository.existsByNickname(signUpForm.getNickname())) {
        	throw new UserDuplicateException(ErrorCode.NICKNAME_DUPLICATION);
        }
        User user = new User(signUpForm, encoder.encode(signUpForm.getPassword()));

        Set<String> strRoles = signUpForm.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);
    }

    public Optional<User> findUserId(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserLoginId(String userid) {
        return userRepository.findByUserid(userid);
    }

    //마지막 로그인 시간 저장
    public void saveAccessTime(User user) {
        LocalDateTime dateTime= LocalDateTime.now();
        user.setLastaccesstime(dateTime);
        userRepository.save(user);
    }
}
