package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by z673413 on 2017/2/15.
 */
@Entity
@Table(name = "ZHome_Topic")
public class Topic {
    private Long id;
    private String subject;
    private String detail;
    private String author;
    private String authorWechat;
    private String authorQQ;
    private String authorEmail;
    private String authorMobile;
    private String authorIp;
    private Integer authorType;
    private Date createTime;
    private Date updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "authorWechat")
    public String getAuthorWechat() {
        return authorWechat;
    }

    public void setAuthorWechat(String authorWechat) {
        this.authorWechat = authorWechat;
    }

    @Column(name = "authorQQ")
    public String getAuthorQQ() {
        return authorQQ;
    }

    public void setAuthorQQ(String authorQQ) {
        this.authorQQ = authorQQ;
    }

    @Column(name = "authorEmail")
    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    @Column(name = "authorMobile")
    public String getAuthorMobile() {
        return authorMobile;
    }

    public void setAuthorMobile(String authorMobile) {
        this.authorMobile = authorMobile;
    }

    @Column(name = "authorIp")
    public String getAuthorIp() {
        return authorIp;
    }

    public void setAuthorIp(String authorIp) {
        this.authorIp = authorIp;
    }

    @Column(name = "authorType")
    public Integer getAuthorType() {
        return authorType;
    }

    public void setAuthorType(Integer authorType) {
        this.authorType = authorType;
    }

    @Column(name = "createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "updateTime")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
