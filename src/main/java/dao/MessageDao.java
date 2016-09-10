package dao;

import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zya on 2016/9/9.
 */
@Repository
public class MessageDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addMessage(Message message) {
        hibernateTemplate.save(message);
    }

    public void deleteMessage(Message message) {
        hibernateTemplate.delete(message);
    }

    public void updateMessage(Message message) {
        hibernateTemplate.update(message);
    }

    public List<Message> findMessage(String creator) {
        String hql = "FROM Message WHERE creator = ?";
        List<Message> messages = (List<Message>) hibernateTemplate.find(hql, new Object[]{creator});
        return messages;
    }

    public List<Message> findMessage() {
        String hql = "FROM Message";
        List<Message> messages = (List<Message>) hibernateTemplate.find(hql);
        return messages;
    }
}
