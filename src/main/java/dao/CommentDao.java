package dao;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zya on 2016/9/22.
 */
@Repository
public class CommentDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addComment(Comment comment) {
        hibernateTemplate.save(comment);
    }

    public void deleteComment(Comment comment) {
        hibernateTemplate.delete(comment);
    }

    public void deleteCommentByMessageId(Long messageId) {
        final String hql = "DELETE Comment WHERE messageId = ?";
        hibernateTemplate.bulkUpdate(hql, new Object[]{messageId});
    }

    public void deleteComment(Long commentId) {
        final String hql = "DELETE Comment WHERE id = ?";
        hibernateTemplate.bulkUpdate(hql, new Object[]{commentId});
    }

    public void updateComment(Comment comment) {
        hibernateTemplate.update(comment);
    }

    public List<Comment> findComment(Long messageId) {
        final String hql = "FROM Comment WHERE messageId = ?";
        List<Comment> comments = (List<Comment>) hibernateTemplate.find(hql, new Object[]{messageId});
        return comments;
    }
}
