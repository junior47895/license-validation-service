package co.com.usb.platform.crosscutting.utils;

import java.math.BigInteger;
import java.util.UUID;

/**
 * UUIDUtils
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */
public final class UUIDUtils {

    public static final BigInteger TWOEXP64 = BigInteger.ONE.shiftLeft(64);

    private UUIDUtils() {

    }

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String randomUUIDWithDash() {
        return UUID.randomUUID().toString();
    }

    /*
     * Transforma un UUID en un BigInteger tomando la parte hi y lo,
     * pasasandolas a dominio positivo en caso de ser negativas sumandoles la
     * base de 2 exp 64 y luego sumandolas contra la base de 2 exp 64.
     *
     * @param uuid UUID para transformar
     */
    public static BigInteger fromUUIDToBigInteger(final UUID uuid) {
        BigInteger lo = BigInteger.valueOf(uuid.getLeastSignificantBits());
        BigInteger hi = BigInteger.valueOf(uuid.getMostSignificantBits());

        if (hi.signum() < 0) {
            hi = hi.add(TWOEXP64);
        }

        if (lo.signum() < 0) {
            lo = lo.add(TWOEXP64);
        }

        BigInteger result = lo.add(hi.multiply(TWOEXP64));
        return result;
    }
}
