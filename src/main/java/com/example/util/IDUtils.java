package com.example.util;

import java.util.UUID;

public class IDUtils {
    public static String createIdbyUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
