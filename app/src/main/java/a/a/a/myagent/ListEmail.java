package a.a.a.myagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.gson.Gson;
import java.util.ArrayList;

public class ListEmail extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

    ListView listView;
    private static MyListAdapter myListAdapter;
    ImageButton nextBtn;
    int k=1;
    WorkWithRambler workWithRambler;
    ProgressDialog progressDialog=null;
    DataDB dataDB;
    String json;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_email);

        listView = (ListView) findViewById(R.id.listEmail);

        json=getIntent().getStringExtra("json");
        dataDB = gson.fromJson(json, DataDB.class);

        workWithRambler = new WorkWithRambler(dataDB.getLogin(), dataDB.getPass());



        myListAdapter = new MyListAdapter(this, initData(k));
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(this);
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        progressDialog =ProgressDialog.show(this,"","Loading message...",true);
        dataDB.setTo(workWithRambler.getToSendMail(k, position));
        json = gson.toJson(dataDB);
        startActivity(new Intent(ListEmail.this, ReadMessage.class)
                .putExtra("bodyMess", workWithRambler.getBogyMessage(k, position))
                .putExtra("json", json));
    }

    @Override
    public void onClick(View view) {
            myListAdapter.updata(initData(++k));
    }

    private ArrayList<EmailData> initData(final int page) {
        final ArrayList<EmailData> arrSubject = new ArrayList<>();

        ArrayList<String> arrayList = workWithRambler.getMessageTitle(page);
        for (int i = 0; i < arrayList.size(); i++) {
            arrSubject.add(new EmailData(arrayList.get(i)));
        }

        return arrSubject;
    }

    @Override
    protected void onStop() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
        super.onStop();
    }
}
