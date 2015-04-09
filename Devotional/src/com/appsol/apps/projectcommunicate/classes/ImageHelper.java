package com.appsol.apps.projectcommunicate.classes;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageHelper {
	public static Bitmap getCircledBitmap(Bitmap bitmap,int pixels)
	{
		Bitmap output=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Config.ARGB_8888);
		Canvas canvas= new Canvas(output);
		final Paint paint= new Paint();
		final int color= 0xff424242;
		final Rect rectangle= new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
		RectF rectf= new RectF(rectangle);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		final float roundPix=pixels;
		canvas.drawRoundRect(rectf, roundPix, roundPix, paint);
		canvas.drawCircle(0, 0, 40, paint);
		//paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rectangle,  rectangle, paint);
		return output;
	}
public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int pixels)
{
	Bitmap output=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Config.ARGB_8888);
	Canvas canvas= new Canvas(output);
	final int color= 0xff424242;
	final Paint paint= new Paint();
	final Rect rectangle= new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
	RectF rectf= new RectF(rectangle);
	final float roundPix=pixels;
	
	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(color);
	canvas.drawRoundRect(rectf, roundPix, roundPix, paint);
	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(bitmap, rectangle,  rectangle, paint);
	return output;
}
}
