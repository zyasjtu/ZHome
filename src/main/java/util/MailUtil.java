package util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by Colin on 2017/2/10.
 */

public class MailUtil {
    public static void sendMail(String mailAccount, String mailSubject, String verifyCode) {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server
        senderImpl.setHost("smtp.163.com");

        String mailText = "【机器之芯】 尊敬的" + mailAccount.substring(0, mailAccount.indexOf("@")) + "，" +
                "欢迎注册机器之芯账号，您的验证码是： " + verifyCode + "，若非本人操作请忽略该邮件。";

        // 建立邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[] {"sun111@163.com","sun222@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo(mailAccount);
        mailMessage.setFrom("username@163.com");
        mailMessage.setSubject(mailSubject);
        mailMessage.setText(mailText);

        senderImpl.setUsername("username"); // 根据自己的情况,设置username
        senderImpl.setPassword("password"); // 根据自己的情况, 设置password

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);
    }
}