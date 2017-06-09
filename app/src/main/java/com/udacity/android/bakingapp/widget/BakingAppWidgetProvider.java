package com.udacity.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.android.bakingapp.MainActivity;
import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider_info);
            Intent titleIntent = new Intent(context, MainActivity.class);
            PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
            views.setOnClickPendingIntent(R.id.textViewTitle, titlePendingIntent);

            Intent intent = new Intent(context, WidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widgetListView, intent);

            Intent clickIntentTemplate = new Intent(context, StepsActivity.class);
            PendingIntent clickPendingIntentTemplate = PendingIntent.getActivity(context, 0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, BakingAppWidgetProvider.class);
            widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(componentName), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }
}

