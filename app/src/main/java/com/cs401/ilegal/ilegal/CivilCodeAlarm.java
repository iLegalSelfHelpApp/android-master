package com.cs401.ilegal.ilegal;

/**
 * Created by seanyuan on 9/28/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CivilCodeAlarm extends BroadcastReceiver {
    static String scheduledtitle = "Civil Code of the Month";
    static String scheduledbody;
    List<String> x = new ArrayList<>(Arrays.asList(
            "§23103 (a)  A person who drives a vehicle in willful or wanton disregard is guilty of reckless driving.",
            "§23140 (a) It is unlawful for a person under the age of 21 years who has 0.05 percent or more, by weight, of alcohol in his or her blood to drive a vehicle.",
            "§23249.50 (1) Driving under the influence of alcoholic or drug is a serious problem, constituting the largest group of misdemeanor violations in many counties.",
            "§23249.50 (2) Studies of first offenders found that more than half of first offenders are alcoholics or problem drinkers.",
            "§21200 (a) A person riding a bicycle or pedicab has all the rights and is subject to all the provisions applicable to the driver of a vehicle.",
            "§21205 No person operating a bicycle shall carry any package, bundle or article which prevents the operator from keeping at least one hand upon the handlebars.",
            "§21213 (a) A person under 16 years of age shall not operate a class 3 electric bicycle.",
            "§21221 Every person operating a motorized scooter upon a highway has all the rights and is subject to all the provisions applicable to the driver of a vehicle",
            "§21800 (a) The driver of a vehicle approaching an intersection shall yield the right-of-way to any vehicle which has entered the intersection from a different highway.",
            "§21952 The driver of any motor vehicle, prior to driving over or upon any sidewalk, shall yield the right-of-way to any pedestrian approaching thereon.",
            "§21957 No person shall stand in a roadway for the purpose of soliciting a ride from the driver of any vehicle.",
            "§21966 No pedestrian shall proceed along a bicycle path or lane where there is an adjacent adequate pedestrian facility.",
            "§21970 (a) No person may stop a vehicle unnecessarily in a manner that causes the vehicle to block a marked or unmarked crosswalk or sidewalk.",
            "§298.5 (a) Two persons desiring to become domestic partners may complete and file a Declaration of Domestic Partnership with the Secretary of State.",
            "§30 The parties to crimes are classified as Principals and Accessories.",
            "§208 (a) Kidnapping is punishable by imprisonment in the state prison for three, five, or eight years.",
            "§215 (b) Carjacking is punishable by imprisonment in the state prison for a term of three, five, or nine years.",
            "§236 False imprisonment is the unlawful violation of the personal liberty of another",
            "§236.1 (g) (6) “Great bodily injury” means a significant or substantial physical injury.",
            "§236.1 (g) (7) “Minor” means a person less than 18 years of age.",
            "§240 An assault is an unlawful attempt, coupled with a present ability, to commit a violent injury on the person of another.",
            "§242 A battery is any willful and unlawful use of force or violence upon the person of another."
    ));
    @Override
    public void onReceive(Context context, Intent intent) {
            System.out.println("Civil code running");
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(x.size());
            scheduledbody = x.get(randomInt);
            MainActivity.civiltext = x.get(randomInt);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.civil)
                            .setContentTitle(scheduledtitle)
                            .setContentText(scheduledbody);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("code", scheduledbody);
            notificationIntent.putExtras(mBundle);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, mBuilder.build());
    }
}