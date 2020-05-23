package gui;

import data.filler.DataManager;
import data.model.AcceleratorData;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.decimal4j.util.DoubleRounder;

@Slf4j
public class SensorSet {
    private static final int PRECISION_LEVEL = 2;

    @Getter private GridPane pane;
    private Gauge ax1;
    private Gauge ay1;
    private Gauge az2;
    private Gauge az1;
    private Gauge ax2;
    private Gauge ay2;
    private Gauge gx1;
    private Gauge gy1;
    private Gauge gz2;
    private Gauge gz1;
    private Gauge gx2;
    private Gauge gy2;

    public void init() {
        GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.SIMPLE);
        ax1 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        ay1 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        az2 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        az1 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        ax2 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        ay2 = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        gx1 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gy1 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gz2 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gz1 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gx2 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gy2 = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();

        VBox ax1box = getTopicBox("Accel X1", Color.rgb(77, 208, 225),  ax1);
        VBox ay1box = getTopicBox("Accel Y1", Color.rgb(255, 183, 77),  ay1);
        VBox az1box = getTopicBox("Accel Z1", Color.rgb(129, 199, 132), az1);
        VBox ax2box = getTopicBox("Accel X2", Color.rgb(149, 117, 205), ax2);
        VBox ay2box = getTopicBox("Accel Y2", Color.rgb(186, 104, 200), ay2);
        VBox az2box = getTopicBox("Accel Z2", Color.rgb(229, 115, 115), az2);

        VBox gx1box = getTopicBox("Gyro X1", Color.rgb(77, 208, 225),  gx1);
        VBox gy1box = getTopicBox("Gyro Y1", Color.rgb(255, 183, 77),  gy1);
        VBox gz1box = getTopicBox("Gyro Z1", Color.rgb(129, 199, 132), gz1);
        VBox gx2box = getTopicBox("Gyro X2", Color.rgb(149, 117, 205), gx2);
        VBox gy2box = getTopicBox("Gyro Y2", Color.rgb(186, 104, 200), gy2);
        VBox gz2box = getTopicBox("Gyro Z2", Color.rgb(229, 115, 115), gz2);

        pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(39, 44, 50), CornerRadii.EMPTY, Insets.EMPTY)));

        pane.add(ax1box, 0, 0);
        pane.add(ay1box, 0, 1);
        pane.add(az1box, 0, 2);

        pane.add(ax2box, 1, 0);
        pane.add(ay2box, 1, 1);
        pane.add(az2box, 1, 2);

        pane.add(gx1box, 2, 0);
        pane.add(gy1box, 2, 1);
        pane.add(gz1box, 2, 2);

        pane.add(gx2box, 3, 0);
        pane.add(gy2box, 3, 1);
        pane.add(gz2box, 3, 2);
    }

    private VBox getTopicBox(final String TEXT, final Color color, final Gauge gauge) {
        Rectangle bar = new Rectangle(200, 3);
        bar.setArcWidth(6);
        bar.setArcHeight(6);
        bar.setFill(color);

        Label label = new Label(TEXT);
        label.setTextFill(color);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 10, 0));

        gauge.setBarColor(color);
        gauge.setBarBackgroundColor(Color.rgb(39, 44, 50));
        gauge.setAnimated(true);

        VBox vBox = new VBox(bar, label, gauge);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public void refresh() {
        AcceleratorData data = DataManager.getInstance().getDataContainer().getAndCleanAVG();
        if (data != null) {
            ax1.setValue(DoubleRounder.round(data.getAcceleratorX1(), PRECISION_LEVEL));
            ay1.setValue(DoubleRounder.round(data.getAcceleratorY1(), PRECISION_LEVEL));
            az1.setValue(DoubleRounder.round(data.getAcceleratorZ1(), PRECISION_LEVEL));

            ax2.setValue(DoubleRounder.round(data.getAcceleratorX2(), PRECISION_LEVEL));
            ay2.setValue(DoubleRounder.round(data.getAcceleratorY2(), PRECISION_LEVEL));
            az2.setValue(DoubleRounder.round(data.getAcceleratorZ2(), PRECISION_LEVEL));

            gx1.setValue(DoubleRounder.round(data.getGyroX1(), PRECISION_LEVEL));
            gy1.setValue(DoubleRounder.round(data.getGyroY1(), PRECISION_LEVEL));
            gz1.setValue(DoubleRounder.round(data.getGyroZ1(), PRECISION_LEVEL));

            gx2.setValue(DoubleRounder.round(data.getGyroX2(), PRECISION_LEVEL));
            gy2.setValue(DoubleRounder.round(data.getGyroY2(), PRECISION_LEVEL));
            gz2.setValue(DoubleRounder.round(data.getGyroZ2(), PRECISION_LEVEL));
        }
    }
}
