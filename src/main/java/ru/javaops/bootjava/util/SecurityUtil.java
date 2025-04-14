package ru.javaops.bootjava.util;

public class SecurityUtil {
    private static final int USER_ID = 100000;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return USER_ID;
    }
}
