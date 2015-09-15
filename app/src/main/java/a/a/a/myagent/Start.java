package a.a.a.myagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.gson.Gson;

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
                if(login.getText().length()>1&&pass.getText().length()>1) {
                    DataDB dataDB = new DataDB(Start.this,login.getText().toString(),pass.getText().toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(dataDB);
                    startActivity(new Intent(getApplicationContext(), ListEmail.class).putExtra("json", json));
                }else {
                    Toast.makeText(getApplicationContext(),"Enter your login and password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
