package licentaclient.abratu.com.licentaclient.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by apetho on 12/18/2017.
 */

public class FontManager {

    public static final String ROOT = "",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font ) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
