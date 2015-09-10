package a.a.a.myagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;

public class ListEmail extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{

    private static final int KEY_FOR_UPDATE_LIST=1;

    ListView listView;
    private static MyListAdapter myListAdapter;
    ImageButton nextBtn;
    int k=1;
    WorkWithPost workWithPost;
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

        workWithPost = new WorkWithPost(dataDB.getLogin(), dataDB.getPass());
        myListAdapter = new MyListAdapter(this, initData(k));
        if(myListAdapter.getCount()!=0) {
            listView.setAdapter(myListAdapter);
        }else {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this,Start.class));
        }

        listView.setOnItemClickListener(this);
        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        dataDB.setTo(workWithPost.getToSendMail(k,position));

        json = gson.toJson(dataDB);
        int pos= position;
            startActivity(new Intent(ListEmail.this, ReadMessage.class)
                    .putExtra("position", pos)
                    .putExtra("json", json)
                    .putExtra("page", k));
    }

    public ArrayList<EmailData> initData(int page){
        final ArrayList<EmailData> arrSubject = new ArrayList<>();
        ArrayList<String> arrayList = workWithPost.getMessageTitle(page);
        for (int i = 0; i < arrayList.size(); i++) {
            arrSubject.add(new EmailData(arrayList.get(i)));
        }

        return arrSubject;
    }

    @Override
    public void onClick(View view) {
        myListAdapter.update(initData(++k));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,KEY_FOR_UPDATE_LIST,Menu.NONE,"Updata").setIcon(R.drawable.update);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case KEY_FOR_UPDATE_LIST :
                    myListAdapter.update(initData(k));
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
