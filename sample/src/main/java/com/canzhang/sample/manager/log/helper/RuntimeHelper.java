package com.canzhang.sample.manager.log.helper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;


public class RuntimeHelper {

    /**
     * Exec the arguments, using root if necessary.
     *
     * @param args
     */
    public static Process exec(List<String> args) throws IOException {
        return Runtime.getRuntime().exec(toArray(args, String.class));
    }

    public static void destroy(Process process) {
        // if we're in JellyBean, then we need to kill the process as root, which requires all this
        // extra UnixProcess logic
            process.destroy();
    }


    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(clazz, list.size());
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}