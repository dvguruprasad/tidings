package messagepassing.pipeline;

public class Message {
    private Object payload;

    public Message(Object payload) {
        this.payload = payload;
    }

    public Object payload() {
        return payload;
    }
}
