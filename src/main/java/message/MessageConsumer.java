package message;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by Colin on 2017/6/21.
 */
public class MessageConsumer implements MessageListener {
    Logger logger = Logger.getLogger(MessageConsumer.class);

    public void onMessage(Message message) {
        logger.info("message body:\n" + message.toString());
        logger.info("message body:\n" + message.getMessageProperties().toString());
    }
}
