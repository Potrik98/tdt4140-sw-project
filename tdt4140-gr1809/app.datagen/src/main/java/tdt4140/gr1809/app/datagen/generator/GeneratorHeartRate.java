package tdt4140.gr1809.app.datagen.generator;

import tdt4140.gr1809.app.core.model.DataPoint;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratorHeartRate extends AbstractGenerator {
	public GeneratorHeartRate(final UUID userId) {
		super(userId, DataPoint.DataType.HEART_RATE);
	}

	public List<Integer> generateRandomValues(final int count) {
		return Stream.iterate(getRandomNumberInInterval(40, 140), // Startverdi
				previous -> previous
						+ (2 * random.nextInt(1) - 1) // Vil være enten 1 eller -1 for opp eller ned.
						* random.nextInt(20) // Endring
				)
				.limit(count)
				.collect(Collectors.toList());
	}
}
