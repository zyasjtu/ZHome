package form;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * Created by Colin on 2017/2/14.
 */
public class SignUpForm {
    public String signUpEmail;
    public String signUpPassword1;
    public String signUpPassword2;
    public String signUpVerifyCode;

    public boolean validate(HttpSession session) {
        if (StringUtils.isEmpty(signUpEmail) || StringUtils.isEmpty(signUpPassword1) || StringUtils.isEmpty(signUpPassword2)
                || StringUtils.isEmpty(signUpVerifyCode) || !signUpVerifyCode.equals(session.getAttribute("verifyCode")))
            return false;
        else
            return true;
    }

    public String getSignUpEmail() {
        return signUpEmail;
    }

    public void setSignUpEmail(String signUpEmail) {
        this.signUpEmail = signUpEmail;
    }

    public String getSignUpPassword1() {
        return signUpPassword1;
    }

    public void setSignUpPassword1(String signUpPassword1) {
        this.signUpPassword1 = signUpPassword1;
    }

    public String getSignUpPassword2() {
        return signUpPassword2;
    }

    public void setSignUpPassword2(String signUpPassword2) {
        this.signUpPassword2 = signUpPassword2;
    }

    public String getSignUpVerifyCode() {
        return signUpVerifyCode;
    }

    public void setSignUpVerifyCode(String signUpVerifyCode) {
        this.signUpVerifyCode = signUpVerifyCode;
    }
}
