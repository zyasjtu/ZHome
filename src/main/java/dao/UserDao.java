package dao;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by zya on 2016/9/8.
 */
@Repository
public class UserDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addUser(User user) {
        hibernateTemplate.save(user);
    }

    public void addUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        hibernateTemplate.save(user);
    }

    public void deleteUser(User user) {
        hibernateTemplate.delete(user);
    }

    public void updateUser(User user) {
        hibernateTemplate.update(user);
    }

    public void updateUser(String name, String password) {
        final String hql = "UPDATE User set password = ? where name = ?";
        hibernateTemplate.bulkUpdate(hql, new Object[]{password, name});
    }

    public List<User> findUser(final String email, final String password) {
        final String hql = "FROM User WHERE email = ? AND password = ? ";
        List<User> users = (List<User>) hibernateTemplate.find(hql, new Object[]{email, password});
        return users;
    }
}
