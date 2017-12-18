package licentaclient.abratu.com.licentaclient.utils;

import java.util.Collection;
import java.util.List;

/**
 * Created by apetho on 12/18/2017.
 */

public class CollectionUtils {

    public static boolean isNotEmpty( Collection collection ) {
        return collection != null && ! collection.isEmpty();
    }

    public static final <T> boolean containsList(List<T> list, T status ) {
        boolean retVal = false;
        for (T str : list) {
            if (str.equals(status)) {
                retVal = true;
            }
        }
        return retVal;
    }
}
