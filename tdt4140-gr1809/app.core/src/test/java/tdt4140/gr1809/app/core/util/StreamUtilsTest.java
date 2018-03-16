package tdt4140.gr1809.app.core.util;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamUtilsTest {
    @Test
    public void testStreamUtilTakeWhile() {
        final List<Integer> integerList = StreamUtils.takeWhile(
                Stream.iterate(0, i -> i + 1),
                i -> i < 5)
                .collect(Collectors.toList());
        assertThat(integerList).containsExactly(0, 1, 2, 3, 4);
    }
}
