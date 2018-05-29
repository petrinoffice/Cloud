package Cloud.Common.MessageType;


import java.io.Serializable;

public class CommonMessage implements Serializable {
    private int cmd;
    private Object[] attachment;

    public static final int AUTH_OK = 1;
    public static final int DEAUTH = 2;
    public static final int CANNOT_AUHT = 3;




    public int getCmd() {
        return cmd;
    }

    public Object[] getAttachment() {
        return attachment;
    }

    public CommonMessage(int cmd, Object... attachment){
        this.cmd = cmd;
        this.attachment = attachment;
    }
    public CommonMessage(int cmd){
        this.cmd = cmd;
    }
}

