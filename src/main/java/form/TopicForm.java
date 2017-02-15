package form;

import org.springframework.util.StringUtils;

/**
 * Created by z673413 on 2017/2/15.
 */
public class TopicForm {
    private String subject;
    private String detail;
    private String authorWecha;
    private String authorQQ;
    private String authorEmail;
    private String authorMobile;
    private Integer authorType;

    public boolean validate() {
        if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(detail) || StringUtils.isEmpty(authorType))
            return false;
        if (StringUtils.isEmpty(authorWecha) && StringUtils.isEmpty(authorQQ) &&
                StringUtils.isEmpty(authorEmail) && StringUtils.isEmpty(authorMobile))
            return false;
        if ((authorType != 1 && authorType != 2) || !authorEmail.contains("@"))
            return false;

        return true;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAuthorWecha() {
        return authorWecha;
    }

    public void setAuthorWecha(String authorWecha) {
        this.authorWecha = authorWecha;
    }

    public String getAuthorQQ() {
        return authorQQ;
    }

    public void setAuthorQQ(String authorQQ) {
        this.authorQQ = authorQQ;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorMobile() {
        return authorMobile;
    }

    public void setAuthorMobile(String authorMobile) {
        this.authorMobile = authorMobile;
    }

    public Integer getAuthorType() {
        return authorType;
    }

    public void setAuthorType(Integer authorType) {
        this.authorType = authorType;
    }
}
