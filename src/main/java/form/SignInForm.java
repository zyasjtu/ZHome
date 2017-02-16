package form;

import org.springframework.util.StringUtils;

/**
 * Created by Colin on 2017/2/14.
 */
public class SignInForm {
    private String signInEmail;
    private String signInPassword;

    public boolean validate() {
        if (StringUtils.isEmpty(signInEmail) || StringUtils.isEmpty(signInPassword))
            return false;
        else
            return true;
    }

    public String getSignInEmail() {
        return signInEmail;
    }

    public void setSignInEmail(String signInEmail) {
        this.signInEmail = signInEmail;
    }

    public String getSignInPassword() {
        return signInPassword;
    }

    public void setSignInPassword(String signInPassword) {
        this.signInPassword = signInPassword;
    }
}
