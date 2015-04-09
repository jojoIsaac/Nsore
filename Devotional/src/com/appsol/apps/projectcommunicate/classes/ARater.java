/*

Copyright (c) 2012, Carl Rice
All rights reserved.

New BSD License
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.appsol.apps.projectcommunicate.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * ARater (Android Rater, App Rater) is used to remind users to rate your Android app
 * after enough usage. Inspired by https://github.com/arashpayan/appirater and others.
 * <br/><br/>
 * Usage:<br/>
 *      1. Include in your project<br/>
 *      2. Edit properties under "Configure these"<br/>
 *      3. Call ARater.registerLaunch(context) on app launch<br/>
 *      4. Optionally call ARater.registerEvent(context) at special times<br/>
 */

public class ARater
{
	/************************************************************************
	* Configure these
	************************************************************************/

	/** App name */
	private static final String APP_NAME             = "Nsore Devotional";
	/** App package */
	private static final String APP_PACKAGE          = Connector.context.getPackageName();;

	/** Number of days before prompt */
	private static final int    LIMIT_DAYS           = 2;
	/** Number of launches before prompt */
	private static final int    LIMIT_LAUNCHES       = 5;
	/** Number of special events before prompt */
	private static final int    LIMIT_EVENTS         = 0;
	/** Wait this many days before re-prompt */
	private static final float  REMIND_IN_DAYS       = 1.0f;

	/** Rate dialog title */
	private static final String DIALOG_TITLE         = String.format("Rate %s", APP_NAME);
	/** Rate dialog message */
	private static final String DIALOG_MESSAGE       = String.format("If you enjoy using %s, would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!", APP_NAME);
	/** Rate dialog "Rate" button */
	private static final String DIALOG_RATE_BT       = "Rate";
	/** Rate dialog "Later" button */
	private static final String DIALOG_LATER_BT      = "Later";
	/** Rate dialog "No" button */
	private static final String DIALOG_NO_BT         = "No";

	/************************************************************************
	 * The business
	 ************************************************************************/

	private static final String __MARKET_URI         = String.format("market://details?id=%s", APP_PACKAGE);
	private static final String __PREF_NAME          = "ARater";
	private static final String __PREF_DONT_SHOW     = "ARater__DONT_SHOW";
	private static final String __PREF_LAUNCHES      = "ARater__LAUNCHES";
	private static final String __PREF_LAUNCH_DATE   = "ARater__LAUNCH_DATE";
	private static final String __PREF_REMINDED_DATE = "ARater__REMINDED_DATE";
	private static final String __PREF_EVENTS        = "ARater__EVENTS";
	private static final long   __LIMIT_MILLIS       = LIMIT_DAYS * 86400000;
	private static final long   __REMINDED_MILLIS    = (long) (REMIND_IN_DAYS * 86400000);

	/**
	 * Register an app launch. Place in your Application or main Activity in onCreate
	 * or onStart depending on how you'd like to use this
	 * @param context a context
	 */
	public static void registerLaunch(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(__PREF_NAME, Activity.MODE_PRIVATE);
		if (prefs.getBoolean(__PREF_DONT_SHOW, false)) return;

		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(__PREF_LAUNCHES, prefs.getLong(__PREF_LAUNCHES, 0) + 1);
		long firstLaunch = prefs.getLong(__PREF_LAUNCH_DATE, 0);
		if (firstLaunch == 0)
		{
			firstLaunch = System.currentTimeMillis();
			editor.putLong(__PREF_LAUNCH_DATE, firstLaunch);
		}
		editor.commit();

		criteriaCheck(context);
	}

	/**
	 * Register a special event
	 * @param context a context
	 */
	public static void registerEvent(final Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(__PREF_NAME, Activity.MODE_PRIVATE);
		if (prefs.getBoolean(__PREF_DONT_SHOW, false)) return;

		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(__PREF_EVENTS, prefs.getLong(__PREF_EVENTS, 0) + 1);
		editor.commit();

		criteriaCheck(context);
	}

	/**
	 * If criteria is met, show the rate dialog
	 * @param context a context
	 */
	private static void criteriaCheck(final Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(__PREF_NAME, Activity.MODE_PRIVATE);
		long launchDate = prefs.getLong(__PREF_LAUNCH_DATE, 0);
		long remindDate = prefs.getLong(__PREF_REMINDED_DATE, 0);
		long launches = prefs.getLong(__PREF_LAUNCHES, 0);
		long events = prefs.getLong(__PREF_EVENTS, 0);

		if (System.currentTimeMillis() >= launchDate + __LIMIT_MILLIS)
		if (launches >= LIMIT_LAUNCHES && events >= LIMIT_EVENTS)
		if (remindDate == 0 || System.currentTimeMillis() >= remindDate + __REMINDED_MILLIS)
			showRateDialog(context);
	}

	/**
	 * Show the rate dialog. You can call this directly to debug, otherwise called when all criteria is met
	 * @param context a context
	 */
	public static void showRateDialog(final Context context)
	{
		final SharedPreferences.Editor editor = context.getSharedPreferences(__PREF_NAME, 0).edit();
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(DIALOG_TITLE);
		builder.setMessage(DIALOG_MESSAGE);
		builder.setPositiveButton(DIALOG_RATE_BT, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogInterface, int i)
			{
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(__MARKET_URI)));
				if (editor != null)
				{
					editor.putBoolean(__PREF_DONT_SHOW, true);
					editor.commit();
				}
				dialogInterface.dismiss();
			}
		});
		builder.setNeutralButton(DIALOG_LATER_BT, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogInterface, int i)
			{
				SharedPreferences.Editor editor = context.getSharedPreferences(__PREF_NAME, Activity.MODE_PRIVATE).edit();
				editor.putLong(__PREF_REMINDED_DATE, System.currentTimeMillis());
				editor.commit();
				dialogInterface.dismiss();
			}
		});
		builder.setNegativeButton(DIALOG_NO_BT, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogInterface, int i)
			{
				if (editor != null)
				{
					editor.putBoolean(__PREF_DONT_SHOW, true);
					editor.commit();
				}
				dialogInterface.dismiss();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface dialogInterface)
			{
				dialogInterface.dismiss();
			}
		});
		builder.create().show();
	}
}
