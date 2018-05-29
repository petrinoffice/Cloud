package Cloud.Common.MessageType;

import java.io.Serializable;

public class AuthMessage implements Serializable {
    private static final long serialVersionUID = 5193392663743561681L;

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
