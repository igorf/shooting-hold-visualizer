package data;

import com.google.common.collect.EvictingQueue;
import data.helper.DataHelper;
import data.model.AcceleratorData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataContainer {
    private final static int DATA_SIZE = 10000;
    private final static int AVG_SIZE = 10;
    private final static int DIFF_SIZE = 5;
    private final EvictingQueue<AcceleratorData> data = EvictingQueue.create(DATA_SIZE);
    private final EvictingQueue<AcceleratorData> average = EvictingQueue.create(AVG_SIZE);
    private final EvictingQueue<AcceleratorData> diff = EvictingQueue.create(DIFF_SIZE);

    public synchronized void add(AcceleratorData chunk) {
        data.add(chunk);
    }

    public synchronized AcceleratorData getAndCleanAvgPeak() {
        if (data.size() == 0) {
            return null;
        }

        AcceleratorData result = DataHelper.average(data);
        data.clear();
        average.add(result);

        AcceleratorData diff = DataHelper.diff(getAverage(), result);
        this.diff.add(diff);

        return result;
    }

    public AcceleratorData getAverage() {
        return DataHelper.average(average);
    }

    public AcceleratorData getAvgDiff() {
        return DataHelper.average(diff);
    }
}
