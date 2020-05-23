package data.filler;

import data.DataContainer;
import data.converter.AcceleratorDataConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DataManager  {
    private static DataManager instance;

    @Getter
    private final DataContainer dataContainer = new DataContainer();

    private DataManager() {
        super();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void put(String data) throws IOException {
        if (!data.isEmpty()) {
            dataContainer.add(AcceleratorDataConverter.fromString(data));
        }
    }
}
