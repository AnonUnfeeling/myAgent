package a.a.a.myagent;

import android.content.Context;

import java.io.Serializable;

class DataDB implements Serializable{

    private String login;
    private String pass;
    private String to;

    public DataDB() {
    }

    public DataDB(Context context, String login, String pass) {
        this.login=login;
        this.pass=pass;

        DataBase dataBase = new DataBase(context);
        dataBase.addLoginAndPassword(this);
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
