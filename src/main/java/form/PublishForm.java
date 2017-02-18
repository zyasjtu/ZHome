package form;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by z673413 on 2017/2/15.
 */
public class PublishForm {
    private String publishSubject;
    private String publishDetail;
    private String publishWechat;
    private String publishQQ;
    private String publishEmail;
    private String publishMobile;
    private Integer publishType;
    private String publishVerifyCode;

    public boolean validate(HttpServletRequest request) {
        if (StringUtils.isEmpty(publishSubject) || StringUtils.isEmpty(publishDetail) ||
                StringUtils.isEmpty(publishType) || StringUtils.isEmpty(publishVerifyCode))
            return false;
        if (StringUtils.isEmpty(publishWechat) && StringUtils.isEmpty(publishQQ) &&
                StringUtils.isEmpty(publishEmail) && StringUtils.isEmpty(publishMobile))
            return false;
        if ((publishType != 1 && publishType != 2) || (!StringUtils.isEmpty(publishEmail) && !publishEmail.contains("@")))
            return false;
        if (!request.getSession().getAttribute("verifyCode").equals(publishVerifyCode))
            return false;

        return true;
    }

    public String getPublishSubject() {
        return publishSubject;
    }

    public void setPublishSubject(String publishSubject) {
        this.publishSubject = publishSubject;
    }

    public String getPublishDetail() {
        return publishDetail;
    }

    public void setPublishDetail(String publishDetail) {
        this.publishDetail = publishDetail;
    }

    public String getPublishWechat() {
        return publishWechat;
    }

    public void setPublishWechat(String publishWechat) {
        this.publishWechat = publishWechat;
    }

    public String getPublishQQ() {
        return publishQQ;
    }

    public void setPublishQQ(String publishQQ) {
        this.publishQQ = publishQQ;
    }

    public String getPublishEmail() {
        return publishEmail;
    }

    public void setPublishEmail(String publishEmail) {
        this.publishEmail = publishEmail;
    }

    public String getPublishMobile() {
        return publishMobile;
    }

    public void setPublishMobile(String publishMobile) {
        this.publishMobile = publishMobile;
    }

    public Integer getPublishType() {
        return publishType;
    }

    public void setPublishType(Integer publishType) {
        this.publishType = publishType;
    }

    public String getPublishVerifyCode() {
        return publishVerifyCode;
    }

    public void setPublishVerifyCode(String publishVerifyCode) {
        this.publishVerifyCode = publishVerifyCode;
    }
}
