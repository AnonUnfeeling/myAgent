package a.a.a.myagent;

import java.io.Serializable;

public class DataDB implements Serializable{

    private String login;
    private String pass;
    private String to;

    public DataDB() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
