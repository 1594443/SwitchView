package com.custom.switchview;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author 1594443
 * Created on 2025/04/21
 * Description:
 */
public class PxUtils {
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int spToPx(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
