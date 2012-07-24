package messagepassing.pipeline;

import org.jetlang.channels.Channel;
import org.jetlang.channels.Subscribable;
import org.jetlang.core.Callback;
import org.jetlang.core.Disposable;
import org.jetlang.core.DisposingExecutor;

public class PredicatedChannel implements Channel<Message> {
    private Channel<Message> innerChannel;
    private MessagePredicate messagePredicate;

    public PredicatedChannel(Channel<Message> innerChannel, MessagePredicate messagePredicate) {
        this.innerChannel = innerChannel;
        this.messagePredicate = messagePredicate;
    }

    public void publish(Message s) {
        messagePredicate.process(s);
        if (messagePredicate.isTrue()) {
            innerChannel.publish(messagePredicate.getMessage());
        } else {
            messagePredicate.add(s);
        }
    }

    public Disposable subscribe(DisposingExecutor disposingExecutor, Callback<Message> messageCallback) {
        throw new UnsupportedOperationException();
    }

    public Disposable subscribe(Subscribable<Message> messageSubscribable) {
        throw new UnsupportedOperationException();
    }
}
