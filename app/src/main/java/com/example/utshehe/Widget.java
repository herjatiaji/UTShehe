package com.example.utshehe;

import static android.content.Context.MODE_PRIVATE;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import io.paperdb.Paper;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {
    final String API_ID = "104ca9ffdd53f5ad89d60f8118ac6c0e";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        String temper = pref.getString("temp", null);
        String city = pref.getString("cityyy",null);
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.appwidget_text, temper);
        views.setTextViewText(R.id.city_widget,city);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

}