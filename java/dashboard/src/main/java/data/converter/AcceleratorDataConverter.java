package data.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.model.AccelerometerData;

import java.io.IOException;

public class AcceleratorDataConverter {
    private static ObjectMapper mapper = new ObjectMapper();

    public static AccelerometerData fromString(String value) throws IOException {
        return mapper.reader().readValue(value, AccelerometerData.class);
    }
}
