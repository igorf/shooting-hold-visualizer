package data.helper;

import data.model.AccelerometerData;

import java.util.Collection;

public class DataHelper {
    public static AccelerometerData average(Collection<AccelerometerData> data) {
        int size = data.size();
        AccelerometerData result = sum(data);

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

    public static AccelerometerData sum(Collection<AccelerometerData> data) {
        int size = data.size();
        AccelerometerData result = new AccelerometerData();

        //Ужус-ужас, прилетевший на крыльях в 3 часа ночи. А не буду я его переписывать =)
        for (AccelerometerData d: data) {
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

    public static AccelerometerData diff(AccelerometerData first, AccelerometerData second) {
        if (first == null && second == null) {
            return new AccelerometerData();
        }

        if (second == null) {
            return first;
        }

        if (first == null) {
            return second;
        }

        return new AccelerometerData(
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

    public static double dx(AccelerometerData data) {
        return data.getAcceleratorX2() - data.getAcceleratorX1();
    }

    public static double dy(AccelerometerData data) {
        return data.getAcceleratorY2() - data.getAcceleratorY1();
    }

    public static double dz(AccelerometerData data) {
        return data.getAcceleratorZ2() - data.getAcceleratorZ1();
    }

    public static double dxAbs(AccelerometerData data) {
        return Math.abs(dx(data));
    }

    public static double dyAbs(AccelerometerData data) {
        return Math.abs(dy(data));
    }

    public static double dzAbs(AccelerometerData data) {
        return Math.abs(dz(data));
    }

}
