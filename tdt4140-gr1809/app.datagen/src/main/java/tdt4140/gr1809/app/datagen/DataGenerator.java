package tdt4140.gr1809.app.datagen;

import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.datagen.generator.Generator;

import java.util.UUID;

public class DataGenerator {
    private static final String usage = "DataGenerator <DataType> <UserId> <count>";

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments provided!");
            System.out.println(usage);
            return;
        }

        DataPoint.DataType dataType;
        UUID userId;
        int count;
        try {
            dataType = DataPoint.DataType.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown data type: " + args[0]);
            return;
        }
        try {
            userId = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID: " + args[1]);
            return;
        }
        try {
            count = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + args[2]);
            return;
        }

        Generator generator = Generator.getDataGenerator(userId, dataType);
        DataClient dataClient = new DataClient();
        generator.generateDataPoints(count).forEach(dataClient::createDataPoint);
    }
}
