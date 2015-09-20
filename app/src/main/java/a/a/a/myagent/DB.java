package a.a.a.myagent;

import android.content.Context;
import java.io.Serializable;

class DB implements Serializable{

    private String login;
    private String pass;
    private String to;
    private String title;
    private int size;

    public DB() {
    }

    public DB(String title) {
        this.title = title;
    }

    public DB(String login, String pass) {

        this.login = login;
        this.pass = pass;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {

        return size;
    }

    public String getTitle() {
        return title;
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
