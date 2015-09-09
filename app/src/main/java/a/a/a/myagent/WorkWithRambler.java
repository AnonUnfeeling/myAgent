package a.a.a.myagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class WorkWithRambler extends AsyncTask<Void, Void, Boolean> {

    protected static String hostSMTP;
    protected static String hostPOP;
    protected  String login,password,to,title,text;
    Message[] messages;
    ProgressDialog progressDialog;
    protected Context context;

    public WorkWithRambler(Context context,String login, String password, String to, String title, String text) {
        this.login = login;
        this.password = password;
        this.to = to;
        this.title = title;
        this.text = text;
        this.context=context;
    }

    public WorkWithRambler(String login, String password) {
        this.login = login;
        this.password = password;
        hostPOP="pop."+testService(login).replace("@","");
        hostSMTP="smtp."+testService(login).replace("@","");
    }

    public String testService(String str){
        char[] s = str.toCharArray();
        String result="";
        int j=0;
        for (int i = 0; i <s.length; i++) {
            if(s[i]=='@'){
                j=i;
            }
        }
        for (int i = j; i < s.length; i++) {
            result+=s[i];
        }
        return result;
    }

    protected Properties setProperties(String host,int port){
        Properties props = new Properties();

        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        return props;
    }

    public String getBodyMessage(int page, int i) {
       String body ="";
        GetBodyMessage getBodyMessage = new GetBodyMessage();
        getBodyMessage.execute(page, i);
        try {
            body = getBodyMessage.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return body;
    }

    public ArrayList<String> getMessageTitle(int page) {
        ArrayList<String> list = new ArrayList<>();
        GetMessTitle getMessTitle = new GetMessTitle();
        getMessTitle.execute(page);
        try {
            list = getMessTitle.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Message[] getMessage() {
        try {
            Session session = Session.getInstance(setProperties(hostPOP, 995));
            Store store = session.getStore("pop3");
            store.connect(hostPOP,login,password);

            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            messages = folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public String getToSendMail(int page, int position){
        String address = null;
        messages = getMessage();
        if (page == 1) {
            try {
                Address[] addresses = messages[messages.length - 1 -position].getFrom();
                address=addresses == null ? null : ((InternetAddress) addresses[0]).getAddress();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Address[] addresses = messages[messages.length - ((page-1)*10)-position].getFrom();
                address=addresses == null ? null : ((InternetAddress) addresses[0]).getAddress();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return address;
    }

    protected Session setSession(final String login, final String password, String host){

        Session session = Session.getInstance(setProperties(host,465), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login,password);
            }
        });

        return session;
    }

    public void sendMessage(){
        progressDialog = ProgressDialog.show(context,"","Sending message...",true);
        execute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            MimeMessage message = new MimeMessage(setSession(login,password,hostSMTP));
            message.setFrom(new InternetAddress(login));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(title);
            message.setText(text);
            Transport.send(message);
        } catch (AddressException e) {
            return false;
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    class GetMessTitle extends AsyncTask<Integer,Void,ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            ArrayList<String> list = new ArrayList<>();
            messages = getMessage();
            if (integers[0] == 1) {
                for (int i = messages.length - 1; i > messages.length - integers[0] * 10; i--) {
                    try {
                        list.add(messages[i].getSubject());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = messages.length - (integers[0] - 1) * 10; i > messages.length - integers[0] * 10; i--) {
                    try {
                        list.add(messages[i].getSubject());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return list;
        }
    }

    class GetBodyMessage extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            messages = getMessage();
            String body = null;
            if(integers[0]==1) {
                try {
                    Multipart multipart = (Multipart) messages[messages.length - 1 - integers[1]].getContent();

                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart bodyPart = multipart.getBodyPart(j);
                        body = bodyPart.getContent().toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (ClassCastException ex) {
                    try {
                        body = messages[messages.length - 1 - integers[1]].getContent().toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    Multipart multipart = (Multipart) messages[messages.length - 1 - integers[1]-((integers[0]-1)*10)+1].getContent();

                    for (int j = 0; j < multipart.getCount(); j++) {
                        BodyPart bodyPart = multipart.getBodyPart(j);

                        body = bodyPart.getContent().toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (ClassCastException ex) {
                    try {
                        body = messages[messages.length - 1 - integers[1]-((integers[0]-1)*10)+1].getContent().toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

            return body;
        }
    }
}
