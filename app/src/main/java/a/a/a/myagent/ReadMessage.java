package a.a.a.myagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

public class ReadMessage extends Activity implements View.OnClickListener{

    private final Gson gson = new Gson();
    private WorkWithPost workWithPost;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_message);

        DataDB dataDB = gson.fromJson(getIntent().getStringExtra("json"), DataDB.class);
        workWithPost = new WorkWithPost(dataDB.getLogin(), dataDB.getPass());

        int postition=getIntent().getIntExtra("position",0);
        int page = getIntent().getIntExtra("page",0);

        WebView webView = (WebView) findViewById(R.id.webView);

        GetBodyMessage getBodyMessage = new GetBodyMessage();
        progressDialog = ProgressDialog.show(this,"","Loading message...");
        getBodyMessage.execute(page,postition);

        try {
            webView.loadDataWithBaseURL(null, getBodyMessage.get(), "text/html", "utf-8", null);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ImageButton reply = (ImageButton) findViewById(R.id.replyBtn);
        reply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, SendActivity.class).putExtra("json",getIntent().getStringExtra("json")));
    }

    private class GetBodyMessage extends AsyncTask<Integer,Void,String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog==null)return;
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Message[] messages = workWithPost.getMessage();
            String body = null;
            try {
                Multipart multipart = (Multipart) messages[messages.length -1- integers[1]].getContent();

                for (int j = 0; j < multipart.getCount(); j++) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    body = bodyPart.getContent().toString();
                }
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            } catch (ClassCastException ex) {
                try {
                    body = messages[messages.length - 1 - integers[1]].getContent().toString();
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                }
            }

            return body;
        }
    }
}
