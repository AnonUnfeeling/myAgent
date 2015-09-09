package a.a.a.myagent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String>{

    private ArrayList<EmailData> list;
    private Context contex;

    public MyListAdapter(Context context, ArrayList<EmailData> emailDatas) {
        super(context,R.layout.style_list_view);
        this.contex=context;
        this.list=emailDatas;
    }

    public void update(ArrayList<EmailData> emailDatas){
        list.clear();
        list.addAll(emailDatas);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position).getTitle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater infater =(LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =infater.inflate(R.layout.style_list_view,parent,false);
        TextView title = (TextView) view.findViewById(R.id.title);
        EmailData emailData = list.get(position);
        title.setText(emailData.getTitle());
        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
