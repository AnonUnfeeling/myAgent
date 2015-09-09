package a.a.a.myagent;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class ReadMessage extends Activity implements View.OnClickListener{

    ImageButton reply;
    WebView webView;
    Gson gson = new Gson();
    DataDB dataDB;
    WorkWithRambler workWithRambler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_message);

        dataDB = gson.fromJson(getIntent().getStringExtra("json"),DataDB.class);
        workWithRambler = new WorkWithRambler(dataDB.getLogin(),dataDB.getPass());

        int postition=getIntent().getIntExtra("position",0);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        GetBodyMess getBodyMess = new GetBodyMess();
        getBodyMess.execute(getIntent().getIntExtra("page",0),postition);

        try {
            webView.loadDataWithBaseURL(null, getBodyMess.get(), "text/html","utf-8",null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        reply = (ImageButton) findViewById(R.id.replyBtn);
        reply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, SendActivity.class).putExtra("json",getIntent().getStringExtra("json")));
    }

    class GetBodyMess extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... integers) {
           String mess= workWithRambler.getBogyMessage(integers[0],integers[1]);
            return mess;
        }
    }
}
