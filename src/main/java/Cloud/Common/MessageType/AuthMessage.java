package Cloud.Common.MessageType;

import java.io.Serializable;

public class AuthMessage implements Serializable {
    /**
     * Класс AuthMessage отвечает за авторизацию клиента на севере
     */
    private String login;
    private String pass;

    public AuthMessage(String login, String pass){
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
