package com.univer.universerver.source.config;

import com.google.common.base.Joiner;
import com.univer.universerver.source.model.User;
import com.univer.universerver.source.model.UserHistory;
import com.univer.universerver.source.repository.mongo.UserHistoryRepository;
import com.univer.universerver.source.repository.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component // 1
@Aspect // 2
public class RequestLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private UserRepository userRepository;


    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)", entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    @Pointcut("within(com.univer.universerver.source.controller..*)") // 3
    public void onRequest() {
    }

    @Around("com.univer.universerver.source.config.RequestLoggingAspect.onRequest()") // 4
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (paramMap.isEmpty() == false) {
            params = " [" + paramMapToString(paramMap) + "]";
        }


        long start = System.currentTimeMillis();
        try {
            Authentication auth= SecurityContextHolder.getContext().getAuthentication();
            String username=auth.getName();

            SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
            String lastTime = formatter.format ( request.getSession().getLastAccessedTime() );//세션마지막요청시간
            String initTime = formatter.format ( request.getSession().getCreationTime());//세션초기시간

            UrlPathHelper urlPathHelper = new UrlPathHelper();
            String accessPath = urlPathHelper.getOriginatingRequestUri(request);
            System.out.println("originalURL-->" + accessPath);

            UserHistory userhistory = new UserHistory();

            userhistory.setAccesspath(accessPath);			//로그인시
            userhistory.setIpaddress(request.getRemoteAddr());//아이피주소

            userhistory.setLogindate(initTime);			//초기접속시간
            userhistory.setSessionlastaccess(lastTime);	//마지막접속시간
            userhistory.setOstype(request.getHeader("User-Agent"));
//
            if(!(username.equals("anonymousUser"))) {//사용자의 정보를 가져올 수 있는 경우
                System.out.println("username===="+username);

                Optional<User> findUser = userRepository.findByUserid(username);
                User users=new User();
                long userid = findUser.get().getId();
                users.setId(userid);
                //userhistory.setUser(users);
                userhistory.setUserNumber(findUser.get().getId());
                userhistory.setAccessname(findUser.get().getNickname());
            }
//            //없는 경우는 accessname, userid없이 저장
//
            userHistoryRepository.save(userhistory);
            return pjp.proceed(pjp.getArgs()); // 6
        } finally {
            long end = System.currentTimeMillis();
            logger.debug("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(), params,
                    request.getRemoteHost(), end - start);
        }
    }
}