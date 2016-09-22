package model;

import java.util.List;

/**
 * Created by zya on 2016/9/22.
 */
public class Conversation {
    private Message message;
    private List<Comment> comments;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
