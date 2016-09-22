package dao;

import model.Message;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
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

    public void deleteMessage(Long messageId) {
        final String hql = "DELETE Message WHERE id = ?";
        hibernateTemplate.bulkUpdate(hql, new Object[]{messageId});
    }

    public void updateMessage(Message message) {
        hibernateTemplate.update(message);
    }

    public List<Message> findMessage(String creator) {
        final String hql = "FROM Message WHERE creator = ?";
        List<Message> messages = (List<Message>) hibernateTemplate.find(hql, new Object[]{creator});
        return messages;
    }

    public List<Message> findMessage(Integer pageNo, Integer pageSize) {
        final String hql = "FROM Message";
        List<Message> messages = findByPage(hql, null, pageNo, pageSize);
        return messages;
    }

    private <T> List<T> findByPage(final String queryString, final Object[] values, final int pageNo, final int pageSize) {
        return (List) hibernateTemplate.execute(new HibernateCallback() {
            public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryString);
                if (values != null) {
                    for (int list = 0; list < values.length; ++list) {
                        query.setParameter(list, values[list]);
                    }
                }

                query.setFirstResult((pageNo - 1) * pageSize);
                query.setMaxResults(pageSize);
                List var4 = query.list();
                return var4;
            }
        });
    }
}
