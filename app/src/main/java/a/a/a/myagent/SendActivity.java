package a.a.a.myagent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.gson.Gson;

public class SendActivity extends Activity implements View.OnClickListener{

    EditText text,title;
    ImageButton sendBtn;
    DataDB dataDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);

        Gson gson = new Gson();
        dataDB = gson.fromJson(getIntent().getStringExtra("json"),DataDB.class);

        text = (EditText) findViewById(R.id.text);
        title= (EditText) findViewById(R.id.title);

        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
                new WorkWithPost(this,dataDB.getLogin(),dataDB.getPass()
        ,dataDB.getTo(),title.getText().toString(),text.getText().toString()).sendMessage();
    }
}
