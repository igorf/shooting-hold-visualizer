package data.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AcceleratorData {
    @JsonAlias("ax1") double acceleratorX1;
    @JsonAlias("ay1") double acceleratorY1;
    @JsonAlias("az1") double acceleratorZ1;
    @JsonAlias("ax2") double acceleratorX2;
    @JsonAlias("ay2") double acceleratorY2;
    @JsonAlias("az2") double acceleratorZ2;
    @JsonAlias("gx1") double gyroX1;
    @JsonAlias("gy1") double gyroY1;
    @JsonAlias("gz1") double gyroZ1;
    @JsonAlias("gx2") double gyroX2;
    @JsonAlias("gy2") double gyroY2;
    @JsonAlias("gz2") double gyroZ2;

    public double getDiffAX() {
        return acceleratorX2 - acceleratorX1;
    }

    public double getDiffAY() {
        return acceleratorY2 - acceleratorY1;
    }

    public double getDiffAZ() {
        return acceleratorZ2 - acceleratorZ1;
    }

    public double getDiffGX() {
        return gyroX2 - gyroX1;
    }

    public double getDiffGY() {
        return gyroY2 - gyroY1;
    }

    public double getDiffGZ() {
        return gyroZ2 - gyroZ1;
    }

    public double avgGyroX() {
        return (gyroX1 + gyroX2) / 2d;
    }

    public double avgGyroY() {
        return (gyroY1 + gyroY2) / 2d;
    }

    public double avgGyroZ() {
        return (gyroZ1 + gyroZ2) / 2d;
    }
}
