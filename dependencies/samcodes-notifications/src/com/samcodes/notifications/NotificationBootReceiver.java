package com.samcodes.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.Window;
import com.samcodes.notifications.Common;
import org.haxe.extension.Extension;

public class NotificationBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(context == null || intent == null) {
			return;
		}
		String action = intent.getAction();
		if(action == null) {
			return;
		}
		
		registerAlarmsPostBoot(context);
		
		// Set the last known badge number on the app icon at boot, in case it was forgotten between reboots
		Common.setApplicationIconBadgeNumber(context, Common.getApplicationIconBadgeNumber(context));
	}
	
	private static void registerAlarmsPostBoot(Context context) {
		Common.pendingIntents.clear(); // Clear out the pending intents, precaution in case spurious/multiple post-boot events are received somehow
		
		Long currentTime = System.currentTimeMillis();
		
		// Alarm re-registration. This is required because alarms are cleared when the device is turned off or rebooted
		for(int slot = 0; slot < Common.MAX_NOTIFICATION_SLOTS; slot++) {
			SharedPreferences prefs = Common.getNotificationSettings(context, slot);
			if(prefs == null) {
				continue;
			}
			Long alertTime = prefs.getLong(Common.UTC_SCHEDULED_TIME, -1);
			if(alertTime == -1) {
				continue; // Skip unreadable/not-set notification data
			}
			if(alertTime - currentTime < 0) {
				Common.erasePreference(context, slot); // Skip and erase notifications whose time passed while the phone was powered off
				continue;
			}
			
			String titleText = prefs.getString(Common.TITLE_TEXT_TAG, "");
			String subtitleText = prefs.getString(Common.SUBTITLE_TEXT_TAG, "");
			String messageBodyText = prefs.getString(Common.MESSAGE_BODY_TEXT_TAG, "");
			String tickerText = prefs.getString(Common.TICKER_TEXT_TAG, "");
			
			PendingIntent intent = Common.scheduleLocalNotification(context, slot, alertTime, titleText, subtitleText, messageBodyText, tickerText);
			Common.pendingIntents.put(slot, intent);
		}
	}
}