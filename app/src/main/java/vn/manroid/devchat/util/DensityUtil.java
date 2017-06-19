package vn.manroid.devchat.util;

import android.content.Context;

/**
 * Created by manro on 10/06/2017.
 */

public class DensityUtil {

    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
