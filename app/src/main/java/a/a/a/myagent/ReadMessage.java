package a.a.a.myagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import com.google.gson.Gson;

public class ReadMessage extends Activity implements View.OnClickListener{

    ImageButton reply;
    WebView webView;
    Gson gson = new Gson();
    DataDB dataDB;
    WorkWithPost workWithPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_message);

        dataDB = gson.fromJson(getIntent().getStringExtra("json"),DataDB.class);
        workWithPost = new WorkWithPost(dataDB.getLogin(),dataDB.getPass());

        int postition=getIntent().getIntExtra("position",0);
        int page = getIntent().getIntExtra("page",0);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL(null, workWithPost.getBodyMessage(page, postition), "text/html","utf-8",null);

        reply = (ImageButton) findViewById(R.id.replyBtn);
        reply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, SendActivity.class).putExtra("json",getIntent().getStringExtra("json")));
    }
}
