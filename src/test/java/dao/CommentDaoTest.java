package dao;

import model.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by z673413 on 2016/9/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class CommentDaoTest {
    @Autowired
    private CommentDao commentDao;

    @Test
    public void addComment() throws Exception {
        Comment comment = new Comment();
        comment.setCreator("Colin");
        comment.setCreatorIp("127.0.0.1");
        comment.setReplyTo("Sharon");
        comment.setCreateTime(new Date());
        comment.setText("This is a comment");
        comment.setMessageId(1L);
        commentDao.addComment(comment);
    }

    @Test
    public void deleteComment() throws Exception {

    }

    @Test
    public void updateComment() throws Exception {

    }

    @Test
    public void findComment() throws Exception {

    }

}