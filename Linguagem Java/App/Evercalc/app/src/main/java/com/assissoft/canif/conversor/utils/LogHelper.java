package com.assissoft.canif.conversor.utils;

import android.util.Log;
import com.assissoft.canif.conversor.model.DefConversor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Marcos on 01/09/2016.
 *
 */
public class LogHelper {

    static void d(String tag, Object... messages) {
        // Only log DEBUG if build type is DEBUG
        if (DefConversor.EXIBE_LOG) {
            log(tag, Log.DEBUG, null, messages);
        }
    }

    public static void e(String tag, Throwable t, Object... messages) {
        log(tag, Log.ERROR, t, messages);
    }

    private static void log(String tag, int level, Throwable t, Object... messages) {
        if (DefConversor.EXIBE_LOG) {
            AtomicReference<String> msg = new AtomicReference<>();

            // handle this common case without the extra cost of creating a stringbuffer:
            if (t != null && messages != null && messages.length == 1) msg.set(messages[0].toString());
            else {
                StringBuilder sb = new StringBuilder();

                if (messages != null)
                    for (Object m : messages) {
                        sb.append(m);
                    }

                if (t != null) sb.append("\n").append(Log.getStackTraceString(t));

                msg.set(sb.toString());
            }

            Log.println(level, tag, msg.get());
        }
    }
}
