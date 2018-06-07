package Cloud.Common.MessageType;


import java.io.Serializable;

public class CommonMessage implements Serializable {
    /**
     * Класс CommonMessage отвечает за взаимодейсвие клиента и севера
     * В классе предусмотрено 2 конструктора позволяющих передавать обьекты или только необходимый код
     */
    private int cmd;
    private Object[] attachment;

    public static final int AUTH_OK = 1;
    public static final int DEAUTH = 2;
    public static final int CANNOT_AUHT = 3;
    public static final int SERVER_FILE_REFRESH = 4;
    public static final int SERVER_DELETE_FILE = 5;
    public static final int SERVER_GET_FILE = 6;

    public CommonMessage(int cmd){
        this.cmd = cmd;
    }

    public CommonMessage(int cmd, Object... attachment){
        this.cmd = cmd;
        this.attachment = attachment;
    }

    public int getCmd() {
        return cmd;
    }

    public Object[] getAttachment() {
        return attachment;
    }
}

