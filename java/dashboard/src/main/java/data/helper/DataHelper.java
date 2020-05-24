package data.helper;

import data.model.AcceleratorData;

import java.util.Collection;

public class DataHelper {
    public static AcceleratorData average(Collection<AcceleratorData> data) {
        int size = data.size();
        AcceleratorData result = sum(data);

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

        return result;
    }

    public static AcceleratorData sum(Collection<AcceleratorData> data) {
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

        return result;
    }

    public static AcceleratorData diff(AcceleratorData first, AcceleratorData second) {
        if (first == null && second == null) {
            return new AcceleratorData();
        }

        if (second == null) {
            return first;
        }

        if (first == null) {
            return second;
        }

        return new AcceleratorData(
                first.getAcceleratorX1() - second.getAcceleratorX1(),
                first.getAcceleratorY1() - second.getAcceleratorY1(),
                first.getAcceleratorZ1() - second.getAcceleratorZ1(),
                first.getAcceleratorX2() - second.getAcceleratorX2(),
                first.getAcceleratorY2() - second.getAcceleratorY2(),
                first.getAcceleratorZ2() - second.getAcceleratorZ2(),
                first.getGyroX1() - second.getGyroX1(),
                first.getGyroY1() - second.getGyroY1(),
                first.getGyroZ1() - second.getGyroZ1(),
                first.getGyroX2() - second.getGyroX2(),
                first.getGyroY2() - second.getGyroY2(),
                first.getGyroZ2() - second.getGyroZ2()
        );
    }
}
