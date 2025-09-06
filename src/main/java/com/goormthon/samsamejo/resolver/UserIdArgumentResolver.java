package com.goormthon.samsamejo.resolver;

import com.goormthon.samsamejo.annotation.UserId;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object userIdObject = webRequest.getAttribute("USER_ID", WebRequest.SCOPE_REQUEST);

        if (parameter.getParameterAnnotation(UserId.class).required() && userIdObject == null) {
            throw new RestException(ErrorCode.ACCESS_DENIED);
        }
        return Optional.ofNullable(userIdObject).map(userIdObj -> Long.valueOf(userIdObj.toString())).orElseGet(null);
    }
}
