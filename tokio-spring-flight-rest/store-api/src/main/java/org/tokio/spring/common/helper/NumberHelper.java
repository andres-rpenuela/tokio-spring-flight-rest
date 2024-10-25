package org.tokio.spring.common.helper;


public class NumberHelper {
    public static final int ZERO = 0;
    public static final int THEN = 10;

    private NumberHelper() {}

    /**
     * Generate a number integer random between 0 and Max
     * result € [0,Max)
     * @param max
     * @return
     */
    public static int integerRandom(int max){
        return integerRandom(ZERO,max);
    }
    /**
     * Generate a number integer random between Min and Max
     * result € [Min,Max)
     * @param min
     * @param max
     * @return
     */
    public static int integerRandom(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
