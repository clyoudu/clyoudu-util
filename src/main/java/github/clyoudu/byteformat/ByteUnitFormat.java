package github.clyoudu.byteformat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Create by IntelliJ IDEA
 * Formatting byte size to human readable format
 * @author chenlei
 * @dateTime 2018/12/7 17:35
 * @description ByteUnitFormat
 */
public enum ByteUnitFormat {
    B(Unit.B) {
        @Override
        long toB(long value, Rate rate) {
            return value;
        }
    },
    K(Unit.K) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate;
        }
    },
    M(Unit.M) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate * rate.rate;
        }
    },
    G(Unit.G) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate * rate.rate * rate.rate;
        }
    },
    T(Unit.T) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate * rate.rate * rate.rate * rate.rate;
        }
    },
    P(Unit.P) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate * rate.rate * rate.rate * rate.rate * rate.rate;
        }
    },
    E(Unit.E) {
        @Override
        long toB(long value, Rate rate) {
            return value * rate.rate * rate.rate * rate.rate * rate.rate * rate.rate * rate.rate;
        }
    };

    private Unit unit;

    ByteUnitFormat(Unit unit) {
        this.unit = unit;
    }

    /**
     * Formatting byte size to human readable format
     * @param value original value
     * @param rate advance rate
     * @param scale scale of value to be returned
     * @param roundingMode roundingMode for value to be returned
     * @param displayUnitName return a value end with unit name if true
     * @return human readable byte value as String
     */
    public final String humanReadable(long value, Rate rate, int scale, RoundingMode roundingMode, boolean displayUnitName) {
        long b = toB(value, rate);
        String suffix = displayUnitName ? B.unit.name : "";
        if (b < rate.rate) {
            return b + suffix;
        }
        int level = (int) (Math.log(b) / Math.log(rate.rate));

        return ByteUnitFormat.B.to(ByteUnitFormat.values()[level], b, rate, scale, roundingMode, displayUnitName);
    }

    public final String humanReadable(long value, Rate rate, boolean displayUnitName) {
        return humanReadable(value, rate, 1, RoundingMode.HALF_UP, displayUnitName);
    }

    public final String humanReadable(long value, boolean displayUnitName) {
        return humanReadable(value, Rate.IEC, displayUnitName);
    }

    public final String humanReadable(long value) {
        return humanReadable(value, Rate.IEC, true);
    }

    public final String to(ByteUnitFormat targetFormat, long value, Rate rate) {
        return to(targetFormat, value, rate, 2, RoundingMode.HALF_UP, true);
    }
    public final String to(ByteUnitFormat targetFormat, long value) {
        return to(targetFormat, value, Rate.IEC, 2, RoundingMode.HALF_UP, true);
    }

    /**
     * Converts the specified {@code long value} from this format to the specified {@code targetFormat}.
     * @param targetFormat target unit format
     * @param value specified value
     * @param rate advance rate
     * @param scale scale of value to be returned
     * @param roundingMode roundingMode for value to be returned
     * @param displayUnitName return a value end with unit name if true
     * @return return value with {@code targetFormat} as String
     */
    public final String to(ByteUnitFormat targetFormat, long value, Rate rate, int scale, RoundingMode roundingMode, boolean displayUnitName) {
        int level = targetFormat.unit.ordinal() - unit.ordinal();
        DecimalFormat df = new DecimalFormat("#" + (scale > 0 ? ("#." + repeat("#", scale)) : ""));
        String suffix = displayUnitName ? targetFormat.unit.name : "";

        if (level == 0) {//同级
            return value + suffix;
        } else if (level > 0) {//升级
            BigDecimal result = new BigDecimal(value);
            for (int i = 0; i < level; i++) {
                result = result.divide(new BigDecimal(rate.rate), 8, roundingMode);
            }
            return df.format(result.setScale(scale, roundingMode).doubleValue()) + suffix;
        } else {//降级
            level = Math.abs(level);
            BigDecimal result = new BigDecimal(value);
            for (int i = 0; i < level; i++) {
                result = result.multiply(new BigDecimal(rate.rate));
            }
            return df.format(result.setScale(scale, roundingMode).doubleValue()) + suffix;
        }
    }

    public static String repeat(String seq, int n) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(seq);
        }
        return s.toString();
    }

    abstract long toB(long value, Rate rate);

    public enum Unit {
        B("B"), K("K"), M("M"), G("G"), T("T"), P("P"), E("E");

        private String name;

        Unit(String name) {
            this.name = name;
        }
    }

    public enum Rate {
        SI(1000), IEC(1024);

        private int rate;

        Rate(int rate) {
            this.rate = rate;
        }
    }

    public static void main(String[] args) {
        System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.SI));
        System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.IEC));
        System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L, Rate.IEC));
        System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L));
        System.out.println(ByteUnitFormat.B.humanReadable(174541987L));
        System.out.println(M.humanReadable(1024L));
        System.out.println(M.humanReadable(1024L, false));
    }

}
