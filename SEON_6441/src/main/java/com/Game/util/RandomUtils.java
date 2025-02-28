package com.Game.util;

import java.util.List;
import java.util.Random;

/**
 * Utility methods for random operations.
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();
    
    /**
     * Shuffles a list in place.
     * 
     * @param p_list The list to shuffle.
     * @param <T> The type of elements in the list.
     */
    public static <T> void shuffle(List<T> p_list) {
        for (int i = p_list.size() - 1; i > 0; i--) {
            int l_index = RANDOM.nextInt(i + 1);
            T l_temp = p_list.get(l_index);
            p_list.set(l_index, p_list.get(i));
            p_list.set(i, l_temp);
        }
    }

}