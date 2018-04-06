package tdt4140.gr1809.app.core.util;

import java.util.concurrent.Callable;

public class StreamUtils {
    public static <T> T uncheckCall(Callable<T> callable) {
        try { return callable.call(); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static void uncheckRun(RunnableExc r) {
        try { r.run(); } catch (Exception e) { throw new RuntimeException(e); }
    }

    public interface RunnableExc { void run() throws Exception; }
}