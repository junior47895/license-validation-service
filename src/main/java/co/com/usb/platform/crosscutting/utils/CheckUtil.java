package co.com.usb.platform.crosscutting.utils;

/**
 * CheckUtil
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */
public final class CheckUtil {

    private CheckUtil() {
        super();
    }

    public static boolean checkNull(final Object object) {
        return (object != null && !object.toString().isEmpty());
    }

}
