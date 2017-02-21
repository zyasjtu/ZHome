package dao;

import model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by z673413 on 2017/2/15.
 */
@Repository
public class TopicDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addTopic(Topic topic) {
        hibernateTemplate.save(topic);
    }

    public List<Topic> findAllTopic() {
        final String hql = "FROM Topic WHERE 1=1 ORDER BY updateTime DESC";
        List<Topic> topics = (List<Topic>) hibernateTemplate.find(hql);
        return topics;
    }

    public List<Topic> findTopic(Integer authorType) {
        final String hql = "FROM Topic WHERE authorType = ? ORDER BY updateTime DESC";
        List<Topic> topics = (List<Topic>) hibernateTemplate.find(hql, new Object[]{authorType});
        return topics;
    }
}
