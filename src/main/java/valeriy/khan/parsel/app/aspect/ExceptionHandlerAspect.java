package valeriy.khan.parsel.app.aspect;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static feign.FeignException.*;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionHandlerAspect {
//    @Pointcut("execution(* valeriy.khan.parsel.app.auth.AuthService.registerUser(..))")
//    public void handleOpenFeignException() {
//    }
//    @AfterThrowing(value = "handleOpenFeignException()", throwing = "ex")
//    public ResponseEntity<?> afterCallMethod(FeignClientException ex) {
//
//    }

}
