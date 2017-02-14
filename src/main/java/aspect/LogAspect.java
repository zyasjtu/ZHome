package aspect;

import annotation.LogFunction;
import model.User;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zya on 2016/9/8.
 */
@Aspect
public class LogAspect {
    @Pointcut("execution(* controller.UserController.*(..)) && @annotation(lf)")
    public void functionPointcut(LogFunction lf) {
    }

    @Pointcut("execution(* controller.UserController.signOut(..))")
    public void logoutPointcut() {
    }

    @Before("functionPointcut(lf)")
    public void recordFunction(JoinPoint joinPoint, LogFunction lf) {
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass());
        logger.info("function " + joinPoint.getSignature().getName() + " is called!");
    }

    @Before("logoutPointcut()")
    public void recordLogout(JoinPoint joinPoint) {
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass());
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        User user = (User) request.getSession().getAttribute("loginUser");
        logger.info(user.getEmail() + " logout!");
    }
}
