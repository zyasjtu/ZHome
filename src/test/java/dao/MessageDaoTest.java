package dao;

import model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zya on 2016/9/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional(transactionManager = "transactionManager")
public class MessageDaoTest {
    @Autowired
    private MessageDao messageDao;

    @Test
    public void addMessage() throws Exception {
        Message message = new Message();
        message.setCreator("Colin");
        message.setCreatorIp("127.0.0.1");
        message.setCreateTime(new Date());
        message.setText("This is a message");
        message.setImageUrl("image.jpg");
        messageDao.addMessage(message);
    }

    @Test
    public void deleteMessage() throws Exception {

    }

    @Test
    public void updateMessage() throws Exception {

    }

    @Test
    public void findMessage() throws Exception {

    }

}