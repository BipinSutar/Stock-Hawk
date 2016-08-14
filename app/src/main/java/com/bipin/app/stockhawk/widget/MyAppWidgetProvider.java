package com.bipin.app.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.bipin.app.stockhawk.R;
import com.bipin.app.stockhawk.ui.LineGraph;
import com.bipin.app.stockhawk.ui.MyStocksActivity;

public class MyAppWidgetProvider extends AppWidgetProvider
{
    //@TargetApi(Build.VERSION_CODES.KITKAT)
   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        for(int i=0;i<appWidgetIds.length;i++)
        {
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_detail);

            //To Launch MainStocksActivity
            Intent intent=new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
            remoteViews.setOnClickPendingIntent(R.id.widget,pendingIntent);

            //Setting up data.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            {
                setRemoteAdapter(context,remoteViews);
            }
            else
            {
                setRemoteAdapterr(context,remoteViews);
            }
            boolean detailActivity=context.getResources().getBoolean(R.bool.detail_Activity);
            Intent clickIntent=detailActivity ? new Intent(context,LineGraph.class) : new Intent(context,MyStocksActivity.class);
            PendingIntent clickPendingIntent= TaskStackBuilder.create(context).addNextIntentWithParentStack(clickIntent)
                                              .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list,clickPendingIntent);
            remoteViews.setEmptyView(R.id.widget_list,R.id.widget_empty);

            appWidgetManager.updateAppWidget(appWidgetIds[i],remoteViews);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent)
    {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
        int[] appWidgetIds=appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews remoteViews)
    {
        remoteViews.setRemoteAdapter(R.id.widget_list, new Intent(context, DetailWidgetRemoteViewService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterr(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0,R.id.widget_list,
                new Intent(context,DetailWidgetRemoteViewService.class));
    }
    /*
    @Override
    public void onEnabled(Context context) {
        //Log.v("toggle_widget","Enabled is being called");

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        //retrieve a ref to the manager so we can pass a view update

        Intent i = new Intent();
        i.setClassName("com.bipin.app.stockhawk.widget", "com.bipin.app.stockhawk.widget.MyAppWidgetProvider");
        PendingIntent myPI = PendingIntent.getService(context, 0, i, 0);
        //intent to start service

        // Get the layout for the App Widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_my_stocks);

        //attach the click listener for the service start command intent
        views.setOnClickPendingIntent(R.id.widget, myPI);

        //define the componenet for self
        ComponentName comp = new ComponentName(context.getPackageName(), DetailWidgetRemoteViewService.class.getName());

        //tell the manager to update all instances of the toggle widget with the click listener
        mgr.updateAppWidget(comp, views);
    }*/
}
