package dao;

import model.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by z673413 on 2017/2/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional(transactionManager = "transactionManager")
public class TopicDaoTest {
    @Autowired
    private TopicDao topicDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addTopic() throws Exception {
        Topic topic = new Topic();
        topic.setSubject("subject");
        topic.setDetail("detail");
        topic.setAuthor("author");
        topic.setAuthorWechat("authorWechat");
        topic.setAuthorQQ("authorQQ");
        topic.setAuthorEmail("authorEmail");
        topic.setAuthorMobile("authorMobile");
        topic.setAuthorIp("authorIp");
        topic.setAuthorType(0);
        topic.setCreateTime(new Date());
        topic.setUpdateTime(new Date());

        topicDao.addTopic(topic);
    }

    @Test
    public void findAllTopic() throws Exception {
        addTopic();
        List<Topic> topics = topicDao.findAllTopic();
    }

}