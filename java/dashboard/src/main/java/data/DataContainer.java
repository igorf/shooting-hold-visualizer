package data;

import com.google.common.collect.EvictingQueue;
import data.helper.DataHelper;
import data.model.AccelerometerData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataContainer {
    private final static int DATA_SIZE = 10000;
    private final static int PEAK_SIZE = 5;
    private final static int AVG_SIZE = 15;

    private final EvictingQueue<AccelerometerData> data = EvictingQueue.create(DATA_SIZE);
    private final EvictingQueue<AccelerometerData> peakData = EvictingQueue.create(PEAK_SIZE);
    private final EvictingQueue<AccelerometerData> averageOfLastData = EvictingQueue.create(AVG_SIZE);


    public synchronized void add(AccelerometerData chunk) {
        data.add(chunk);
        if (!peakData.isEmpty()) {
            averageOfLastData.add(peakData.peek());
        } else {
            averageOfLastData.add(chunk);
        }
        peakData.add(chunk);
    }

    public AccelerometerData getAverage() {
        return DataHelper.average(averageOfLastData);
    }

    public AccelerometerData getPeak() {
        return DataHelper.average(peakData);
    }
}
