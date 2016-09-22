package service;

import dao.CommentDao;
import dao.MessageDao;
import model.Comment;
import model.Conversation;
import model.Message;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zya on 2016/9/9.
 */
@Service
public class ConversationService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private CommentDao commentDao;

    @Transactional
    public Map<String, Object> getConversations(Integer pageNo, Integer pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<Conversation> conversations = new ArrayList<Conversation>();

        List<Message> messages = messageDao.findMessage(pageNo, pageSize);
        for (Message message : messages) {
            List<Comment> comments = commentDao.findComment(message.getId());

            Conversation conversation = new Conversation();
            conversation.setMessage(message);
            conversation.setComments(comments);
            conversations.add(conversation);
        }
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "getMessageSuccess");
        returnMap.put("conversations", conversations);

        return returnMap;
    }

    public Map<String, Object> addMessage(HttpServletRequest request, String text, String imageUrl) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("loginUser");

        Message message = new Message();
        message.setCreator(user.getName());
        message.setCreatorIp(request.getRemoteAddr());
        message.setCreateTime(new Date());
        message.setText(text);
        message.setImageUrl(imageUrl);
        messageDao.addMessage(message);

        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "addMessageSuccess");

        return returnMap;
    }

    @Transactional
    public Map<String, Object> deleteMessage(Long messageId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        messageDao.deleteMessage(messageId);
        commentDao.deleteCommentByMessageId(messageId);
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "deleteMessageSuccess");

        return returnMap;
    }

    public Map<String, Object> addComment(HttpServletRequest request, String text, String replyTo, Long messageId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("loginUser");

        Comment comment = new Comment();
        comment.setCreator(user.getName());
        comment.setCreatorIp(request.getRemoteAddr());
        comment.setReplyTo(replyTo);
        comment.setCreateTime(new Date());
        comment.setText(text);
        comment.setMessageId(messageId);
        commentDao.addComment(comment);

        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "addCommentSuccess");

        return returnMap;
    }

    public Map<String, Object> deleteComment(Long commentId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        commentDao.deleteComment(commentId);
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "deleteCommentSuccess");

        return returnMap;
    }
}
