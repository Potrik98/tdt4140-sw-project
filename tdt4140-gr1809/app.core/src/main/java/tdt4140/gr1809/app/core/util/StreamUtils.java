package tdt4140.gr1809.app.core.util;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> p) {
        class Taking extends Spliterators.AbstractSpliterator<T> implements Consumer<T> {
            private static final int CANCEL_CHECK_COUNT = 63;
            private final Spliterator<T> s;
            private int count;
            private T t;
            private final AtomicBoolean cancel = new AtomicBoolean();
            private boolean takeOrDrop = true;

            Taking(Spliterator<T> s) {
                super(s.estimateSize(), s.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED));
                this.s = s;
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                boolean test = true;
                if (takeOrDrop &&                        // If can take
                        (count != 0 || !cancel.get()) && // and if not cancelled
                        s.tryAdvance(this) &&    // and if advanced one element
                        (test = p.test(t))) {            // and test on element passes
                    action.accept(t);                    // then accept element
                    return true;
                } else {
                    // Taking is finished
                    takeOrDrop = false;
                    // Cancel all further traversal and splitting operations
                    // only if test of element failed (short-circuited)
                    if (!test)
                        cancel.set(true);
                    return false;
                }
            }

            @Override
            public Comparator<? super T> getComparator() {
                return s.getComparator();
            }

            @Override
            public void accept(T t) {
                count = (count + 1) & CANCEL_CHECK_COUNT;
                this.t = t;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }
        }
        return StreamSupport.stream(new Taking(stream.spliterator()), stream.isParallel()).onClose(stream::close);
    }
}
