package a.a.a.myagent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.List;

public class Receiver extends BroadcastReceiver{

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

       List<DB> list = new DataBase(context).getAllUsers();
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSize() < new WorkWithPost(list.get(i).getLogin(), list.get(i).getPass()).getSizeMessage()) {
                    showNotification(context);
                }
            }
        }
    }

    public void showNotification(Context context) {

        Notification n = new Notification(R.drawable.icon, "You receive a new message",
                System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,Start.class), 0);

        n.setLatestEventInfo(context, new DB().getLogin(), "You receive a new message", pi);

        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, n);


    }
}
