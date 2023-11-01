package util;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Util {
    public static <T> T getRandomFromSet(Set<T> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("The input Set is empty.");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(set.size());
        Iterator<T> iterator = set.iterator();

        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    public static void verboseLog(String s) {
        if (Global.verbose) System.out.println(s);
    }

    public static boolean chance(double v) {
        return (v > ThreadLocalRandom.current().nextDouble(1));
    }

    public static String getBoolString(boolean b) {
        return b ? " Y " : " N ";
    }
}
