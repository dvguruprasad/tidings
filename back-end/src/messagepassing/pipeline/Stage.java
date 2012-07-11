package messagepassing.pipeline;


import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

public abstract class Stage implements Callback<Message> {
    private Channel<Message> inbox;
    private Channel<Message> outbox;
    private Fiber fiber;

    public Stage(Channel<Message> inbox, Channel<Message> outbox, Fiber threadFiber) {
        this.inbox = inbox;
        this.outbox = outbox;
        fiber = threadFiber;
    }

    public void start() {
        inbox.subscribe(fiber, this);
        fiber.start();
    }

    protected void publish(Message msg) {
        outbox.publish(msg);
    }
}
