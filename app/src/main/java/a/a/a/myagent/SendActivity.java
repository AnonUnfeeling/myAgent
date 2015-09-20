package a.a.a.myagent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.gson.Gson;

public class SendActivity extends Activity implements View.OnClickListener{

    private EditText text;
    private EditText title;
    private DB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);

        Gson gson = new Gson();
        DB = gson.fromJson(getIntent().getStringExtra("json"),DB.class);

        text = (EditText) findViewById(R.id.text);
        title= (EditText) findViewById(R.id.title);

        ImageButton sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
                new WorkWithPost(this, DB.getLogin(), DB.getPass()
        , DB.getTo(),title.getText().toString(),text.getText().toString()).sendMessage();
    }
}
