package licentaclient.abratu.com.licentaclient.utils;

/**
 * Created by apetho on 12/18/2017.
 */

public class StringUtils {

    public static boolean isNotEmpty( String str ) {
        return str != null && ! str.isEmpty();
    }

    public static boolean isEmpty( String str ) {
        return str == null || str.isEmpty();
    }
}
