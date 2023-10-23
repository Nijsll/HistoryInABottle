package util;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class UtilFunctions {
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
}
