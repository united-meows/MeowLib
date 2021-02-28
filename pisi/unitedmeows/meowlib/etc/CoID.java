package pisi.unitedmeows.meowlib.etc;

import java.util.Random;

/** Complex IDentity */
public class CoID implements Comparable<CoID> {

    //SS-UDUD-LLD-SULUUULL-DD
    public static final String ALL_UPPERCASE = "ABCDEFGHIJKLMNOPRSTUVYZXQ";
    public static final String DIGITS = "0123456789";
    public static final String ALL_LOWERCASE = "abcdefghijklmnoprstuvyzxq";
    public static final String SPECIAL_CHARS = "COMPLEX2173";
    private static final Random random;


    static {
        random = new Random();
    }

    private final String value;

    public CoID(String val) {
        value = val;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CoID generate() {
        //SS-UDUD-LLD-SULUUULL-DD
        StringBuilder builder = new StringBuilder();
        // SS
        builder.append(nextSpecial());
        builder.append(nextSpecial());
        builder.append('$');

        //UDUD
        builder.append(nextUpper());
        builder.append(nextDigit());
        builder.append(nextUpper());
        builder.append(nextDigit());
        builder.append('-');

        //LLD
        builder.append(nextLower());
        builder.append(nextLower());
        builder.append(nextDigit());
        builder.append('-');

        //SULUUULL
        builder.append(nextSpecial()); // S
        builder.append(nextUpper()); // U
        builder.append(nextLower()); // L
        builder.append(nextUpper()); // U
        builder.append(nextUpper()); // U
        builder.append(nextUpper()); // U
        builder.append(nextLower()); // L
        builder.append(nextLower()); // L
        builder.append('-');
        //DD
        builder.append(nextDigit());
        builder.append(nextDigit());
        return new CoID(builder.toString());

    }

    private static char nextUpper() {
        return ALL_UPPERCASE.charAt(random.nextInt(ALL_LOWERCASE.length()));
    }

    private static char nextLower() {
        return ALL_LOWERCASE.charAt(random.nextInt(ALL_LOWERCASE.length()));
    }

    private static char nextSpecial() {
        return SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
    }

    private static char nextDigit() {
        return DIGITS.charAt(random.nextInt(DIGITS.length()));
    }


    //TODO: Check for pattern
    @Deprecated
    public static boolean isLegal(String coid) {
        return true;
    }

    @Override
    public int compareTo(CoID o) {
        return toString().equals(o.toString()) ? 1 : 0;
    }
}
