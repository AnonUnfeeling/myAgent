package a.a.a.myagent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class MyListAdapter extends ArrayAdapter<String>{

    private final Set<DB> list;
    private final Context contex;

    public MyListAdapter(Context context, LinkedHashSet<DB> emailDatas) {
        super(context,R.layout.style_list_view);
        this.contex=context;
        this.list=emailDatas;
    }

    static class ViewHolder {
        TextView textView;
    }

    public void update(LinkedHashSet<DB> emailDatas){
        list.addAll(emailDatas);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        List<DB> list1= new ArrayList<>(list);
        return list1.get(position).getTitle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null) {
            LayoutInflater infater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infater.inflate(R.layout.style_list_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }else {
           viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
