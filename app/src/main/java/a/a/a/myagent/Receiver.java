package a.a.a.myagent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.List;

public class Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        List<DataDB> list = new DataBase(context).getAllUsers();
        WorkWithPost workWithPost = new WorkWithPost(list.get(0).getLogin(),list.get(0).getPass());
        int k = workWithPost.getSizeMessage();
        if(k<2000000000){
            //Toast.makeText(context,"New Message",Toast.LENGTH_LONG).show();
            showNotification(context);
        }
    }

    public void showNotification(Context context) {

        Notification n = new Notification(R.drawable.icon, "You are be new message",
                System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);

        n.setLatestEventInfo(context, "Remind Me", "You are be new nessage", pi);

        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, n);


    }

}
