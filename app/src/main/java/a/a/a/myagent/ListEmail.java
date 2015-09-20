package a.a.a.myagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.mail.Message;
import javax.mail.MessagingException;

public class ListEmail extends Activity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    private static final int KEY_FOR_UPDATE_LIST=1;

    private static MyListAdapter myListAdapter;
    private int k=1;
    private WorkWithPost workWithPost;
    private DB DB;
    private String json;
    private final Gson gson = new Gson();
    private ListView listView;
    private GetMessTitle getMessTitle = new GetMessTitle();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_email);

        listView = (ListView) findViewById(R.id.listEmail);

        json=getIntent().getStringExtra("json");
        DB = gson.fromJson(json, DB.class);

        DataBase dataBase = new DataBase(ListEmail.this);

        workWithPost = new WorkWithPost(DB.getLogin(),DB.getPass());

        myListAdapter = new MyListAdapter(this, initData(k));

        if(myListAdapter.getCount()!=0) {
            DB.setSize(workWithPost.getSizeMessage());
            dataBase.addLoginAndPassword(DB);
            listView.setAdapter(myListAdapter);
        }else {
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this,Start.class));
        }

        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    private LinkedHashSet <DB> initData(int page){
        final LinkedHashSet<DB> arrSubject = new LinkedHashSet <>();
        getMessTitle.execute(page);
        ArrayList<String> arrayList = null;
        try {
            arrayList = getMessTitle.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < (arrayList != null ? arrayList.size() : 0); i++) {
            arrSubject.add(new DB(arrayList.get(i)));
        }

        return arrSubject;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        DB.setTo(workWithPost.getToSendMail(k, position));

        json = gson.toJson(DB);
        startActivity(new Intent(ListEmail.this, ReadMessage.class)
                .putExtra("position", position)
                .putExtra("json", json)
                .putExtra("page", k));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,KEY_FOR_UPDATE_LIST,Menu.NONE,"Update").setIcon(R.drawable.update);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = visibleItemCount + firstVisibleItem > totalItemCount-1;

        if(loadMore&&view==listView){
            getMessTitle = new GetMessTitle();
            progressDialog = ProgressDialog.show(this,"","Loading messages...");
            getMessTitle.execute(++k);
            try {
                LinkedHashSet <DB> arrSubject = new LinkedHashSet <>();
                ArrayList<String> arrayList = getMessTitle.get();
                for (int i = 0; i < arrayList.size(); i++) {
                    arrSubject.add(new DB(arrayList.get(i)));
                }
                myListAdapter.update(arrSubject);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetMessTitle extends AsyncTask<Integer,Void,ArrayList<String>>{
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if(progressDialog==null)return;
            progressDialog.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
           ArrayList<String> list = new ArrayList<>();
           Message[] messages = workWithPost.getMessage();

            if (integers[0] == 1) {
                try {
                    for (int i = messages.length - 1; i > messages.length - integers[0] * 10; i--) {
                        list.add(messages[i].getSubject());
                    }
                }catch (NullPointerException | MessagingException ex){
                    ex.printStackTrace();
                }
            } else {
                try {
                    for (int i = messages.length- (integers[0] - 1) * 10; i > messages.length - integers[0] * 10; i--) {
                        list.add(messages[i].getSubject());
                    }
                    } catch (NullPointerException | MessagingException ex){
                        ex.printStackTrace();
                    }
                }
            return list;
        }
    }
}
