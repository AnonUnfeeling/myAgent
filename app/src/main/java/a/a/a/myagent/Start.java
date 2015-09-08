package a.a.a.myagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

public class Start extends Activity {

    ImageButton enterBtn;
    EditText login,pass;
    ProgressDialog progressDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = (EditText) findViewById(R.id.loginEdit);
        pass = (EditText) findViewById(R.id.passEdit);

        enterBtn = (ImageButton) findViewById(R.id.enterBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
                progressDialog = ProgressDialog.show(Start.this, "", "Loading...", true);

                DataDB dataDB = new DataDB();
                dataDB.setLogin(login.getText().toString());
                dataDB.setPass(pass.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(dataDB);
                startActivity(new Intent(getApplicationContext(), ListEmail.class).putExtra("json", json));
            }
        });
    }

    @Override
    protected void onStart() {
        loadSettings();
        super.onStart();
    }

    public void loadSettings() {
        login.setText(getSharedPreferences("setting",MODE_PRIVATE).getString("login", ""));
        pass.setText(getSharedPreferences("setting", MODE_PRIVATE).getString("pass", ""));
    }

    public void saveSettings() {
        SharedPreferences.Editor ed = getSharedPreferences("setting",MODE_PRIVATE).edit();
        ed.putString("login", login.getText().toString()!=null ? login.getText().toString() : "");
        ed.putString("pass", pass.getText().toString() != null ? pass.getText().toString() : "");
        ed.commit();
    }

    @Override
    protected void onStop() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        super.onStop();
    }
}
