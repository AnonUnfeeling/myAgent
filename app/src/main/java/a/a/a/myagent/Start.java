package a.a.a.myagent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.List;

public class Start extends Activity {

    private EditText login;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setContentView(R.layout.activity_start);

        login = (EditText) findViewById(R.id.loginEdit);
        pass = (EditText) findViewById(R.id.passEdit);

        ImageButton enterBtn = (ImageButton) findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login.getText().length() > 1 && pass.getText().length() > 1) {
                    DB db = new DB(login.getText().toString(), pass.getText().toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(db);
                    startActivity(new Intent(getApplicationContext(), ListEmail.class).putExtra("json", json));
                } else {
                    Toast.makeText(getApplicationContext(), "Enter your login and password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getApplication().registerReceiver(new Receiver(), new IntentFilter("android.intent.action.TIME_TICK"));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Start.this.sendBroadcast(new Intent(Start.this, Receiver.class));
//            }
//        }).start();

        List<DB> db = new DataBase(Start.this).getAllUsers();
        if(!db.isEmpty()) {
            for (int i = 0; i < db.size(); i++) {
                login.setText(db.get(0).getLogin());
                pass.setText(db.get(0).getPass());
                System.out.println(db.get(i).getSize());
            }
        }
    }
}
