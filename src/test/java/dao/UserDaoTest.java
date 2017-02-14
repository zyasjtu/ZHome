package dao;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by zya on 2016/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setEmail("Colin");
        user.setPassword("a");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDao.addUser(user);
    }

    @Test
    public void deleteUser() throws Exception {

    }

    @Test
    public void updateUser() throws Exception {
        userDao.updateUser("Colin", "a");
    }

    @Test
    public void findUser() throws Exception {

    }

}