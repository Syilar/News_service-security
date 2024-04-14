package com.example.news.aop;

import com.example.news.exception.WrongUserException;
import com.example.news.model.RoleType;
import com.example.news.model.User;
import com.example.news.service.CommentNewsService;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import com.example.news.web.controller.v2.NewsControllerV2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckingAspect {

    private final NewsService databaseNewsService;

    private final UserService userService;

    private final CommentNewsService databaseCommentNewsService;

    @Before("@annotation(CheckOwner)")
    public void checkBeforeOwner(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        if (joinPoint.getSignature().getName().equals("deleteById") &&
                (request.isUserInRole(RoleType.ROLE_ADMIN.name()) || request.isUserInRole(RoleType.ROLE_MODERATOR.name()))) {
            return;
        }

        var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long idParam = Long.valueOf(pathVariables.get("id"));

        Long userIdParam;
        if (joinPoint.getSignature().getDeclaringType().equals(NewsControllerV2.class)) {
            userIdParam = databaseNewsService.findById(idParam).getUser().getId();
        }
        else {
            userIdParam = databaseCommentNewsService.findById(idParam).getUser().getId();
        }

        String userName = request.getUserPrincipal().getName();
        User user = userService.findByName(userName);
        Long userId = user.getId();

        if (userId != userIdParam) {
            throw new WrongUserException("У вас нет прав для редактирования данных этого пользователя!");
        }

        log.info("{} is done.", joinPoint.getSignature().getName());
    }

    @Before("@annotation(CheckUser)")
    public void checkBeforeUser(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        if (request.isUserInRole(RoleType.ROLE_ADMIN.name()) || request.isUserInRole(RoleType.ROLE_MODERATOR.name())) {
            return;
        }

        String userName = request.getUserPrincipal().getName();
        User user = userService.findByName(userName);
        Long userId = user.getId();

        var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long userIdParam = Long.valueOf(pathVariables.get("id"));

        if (userId != userIdParam) {
            throw new WrongUserException("Вы не можете просматривать, изменять, удалять информацию о другом пользователе!");
        }

        log.info("{} is done.", joinPoint.getSignature().getName());
    }
}
