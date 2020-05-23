package data;

import com.google.common.collect.EvictingQueue;
import data.model.AcceleratorData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataContainer {
    private final static int DATA_SIZE = 10000;
    private final EvictingQueue<AcceleratorData> data = EvictingQueue.create(DATA_SIZE);

    public synchronized void add(AcceleratorData chunk) {
        data.add(chunk);
    }

    public synchronized AcceleratorData getAndCleanAVG() {
        if (data.size() == 0) {
            return null;
        }

        int size = data.size();
        AcceleratorData result = new AcceleratorData();

        //Ужус-ужас, прилетевший на крыльях в 3 часа ночи. А не буду я его переписывать =)
        for (AcceleratorData d: data) {
            result.setAcceleratorX1(result.getAcceleratorX1() + d.getAcceleratorX1());
            result.setAcceleratorY1(result.getAcceleratorY1() + d.getAcceleratorY1());
            result.setAcceleratorZ1(result.getAcceleratorZ1() + d.getAcceleratorZ1());

            result.setAcceleratorX2(result.getAcceleratorX2() + d.getAcceleratorX2());
            result.setAcceleratorY2(result.getAcceleratorY2() + d.getAcceleratorY2());
            result.setAcceleratorZ2(result.getAcceleratorZ2() + d.getAcceleratorZ2());

            result.setGyroX1(result.getGyroX1() + d.getGyroX1());
            result.setGyroY1(result.getGyroY1() + d.getGyroY1());
            result.setGyroZ1(result.getGyroZ1() + d.getGyroZ1());

            result.setGyroX2(result.getGyroX2() + d.getGyroX2());
            result.setGyroY2(result.getGyroY2() + d.getGyroY2());
            result.setGyroZ2(result.getGyroZ2() + d.getGyroZ2());
        }

        result.setAcceleratorX1(result.getAcceleratorX1() / size);
        result.setAcceleratorY1(result.getAcceleratorY1() / size);
        result.setAcceleratorZ1(result.getAcceleratorZ1() / size);

        result.setAcceleratorX2(result.getAcceleratorX2() / size);
        result.setAcceleratorY2(result.getAcceleratorY2() / size);
        result.setAcceleratorZ2(result.getAcceleratorZ2() / size);

        result.setGyroX1(result.getGyroX1() / size);
        result.setGyroY1(result.getGyroY1() / size);
        result.setGyroZ1(result.getGyroZ1() / size);

        result.setGyroX2(result.getGyroX2() / size);
        result.setGyroY2(result.getGyroY2() / size);
        result.setGyroZ2(result.getGyroZ2() / size);

        data.clear();
        return result;
    }

    public AcceleratorData getLast() {
        return data.peek();
    }
}
