package com.defch.fbdemo;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by DiegoFranco on 9/22/16.
 */

public class Utils
{
    //Dialog util for width screen
    public static void setWidthToDialog(Context context, Dialog dialog, boolean setDrim)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout((6 * width)/7, 0);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        if(setDrim)
        {
            params.dimAmount = 0.4f;
            params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dialog.getWindow().setAttributes( params );
        }
    }
}
