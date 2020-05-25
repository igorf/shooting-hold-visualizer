package data;

import com.google.common.collect.EvictingQueue;
import data.helper.DataHelper;
import data.model.AccelerometerData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataContainer {
    private final static int DATA_SIZE = 10000;
    private final static int AVG_SIZE = 10;
    private final static int DIFF_SIZE = 5;

    private final EvictingQueue<AccelerometerData> data = EvictingQueue.create(DATA_SIZE);
    private final EvictingQueue<AccelerometerData> average = EvictingQueue.create(AVG_SIZE);
    private final EvictingQueue<AccelerometerData> diff = EvictingQueue.create(DIFF_SIZE);

    public synchronized void add(AccelerometerData chunk) {
        data.add(chunk);
    }

    public synchronized AccelerometerData getAndCleanAvgPeak() {
        if (data.size() == 0) {
            return null;
        }

        AccelerometerData result = DataHelper.average(data);
        data.clear();
        average.add(result);

        AccelerometerData diff = DataHelper.diff(getAverage(), result);
        this.diff.add(diff);

        return result;
    }

    public AccelerometerData getAverage() {
        return DataHelper.average(average);
    }

    public AccelerometerData getAvgDiff() {
        return DataHelper.average(diff);
    }

    //TODO: Move to once count per cache!!!
    private double getAccelerometerDiffFromAvgZ1() {
        return Math.abs(getAvgDiff().getAcceleratorZ1() - getAverage().getAcceleratorZ1());
    }

    private double getAccelerometerDiffFromAvgZ2() {
        return Math.abs(getAvgDiff().getAcceleratorZ2() - getAverage().getAcceleratorZ2());
    }

    private double getAccelerometerDiffFromAvgY1() {
        return Math.abs(getAvgDiff().getAcceleratorY1() - getAverage().getAcceleratorY1());
    }

    private double getAccelerometerDiffFromAvgY2() {
        return Math.abs(getAvgDiff().getAcceleratorY2() - getAverage().getAcceleratorY2());
    }

    public boolean isFarAccelerometerMovedMoreZ() {
        return getAccelerometerDiffFromAvgZ2() > getAccelerometerDiffFromAvgZ1();
    }

    public boolean isFarAccelerometerMovedMoreY() {
        return getAccelerometerDiffFromAvgY2() > getAccelerometerDiffFromAvgY1();
    }
}
