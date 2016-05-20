package com.went_gone.mobilesafe.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
	/**
	 * bytes转换成bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	/**
	 * drawable转化成bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitamp(Drawable drawable){
		Bitmap bitmap = null;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = 
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
		bitmap = Bitmap.createBitmap(w,h,config);
        /*//注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);   
        drawable.setBounds(0, 0, w, h);   
        drawable.draw(canvas);*/
        return bitmap;
    }
}
