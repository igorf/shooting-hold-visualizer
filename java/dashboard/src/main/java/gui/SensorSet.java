package gui;

import data.filler.DataManager;
import data.helper.DataHelper;
import data.model.AccelerometerData;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.decimal4j.util.DoubleRounder;

@Slf4j
public class SensorSet {
    private static final int PRECISION_LEVEL = 6;
    private static final double PITCH_THRESHOLD = 0.055;
    private static final double YAW_THRESHOLD = 0.025;

    @Getter private GridPane pane;
    private Gauge pitch;
    private Gauge yaw;
    private Gauge mistake;
    private Gauge gyroX;
    private Gauge gyroY;
    private Gauge gyroZ;

    private Gauge pitchError;
    private Gauge yawError;
    private Label wrestError;

    public void init() {
        GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM);
        pitch   = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        yaw     = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        mistake = builder.decimals(3).maxValue(10).minValue(-10).unit("").build();
        gyroX   = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gyroY   = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();
        gyroZ   = builder.decimals(6).maxValue(10).minValue(-10).unit("").build();

        pitchError      = builder.decimals(6).maxValue(10).minValue(0).unit("").build();
        yawError        = builder.decimals(6).maxValue(10).minValue(0).unit("").build();
        wrestError = new Label();
        wrestError.setTextFill(Color.web("#FF5555"));
        wrestError.setFont(new Font("Courier", 30));


        VBox pitchBox           = getTopicBox("Тангаж", Color.rgb(77, 208, 225), pitch);
        VBox yawBox             = getTopicBox("Рыскание", Color.rgb(255, 183, 77), yaw);
        VBox wrestMistakeBox    = getTopicBox("Суммарное отклонение", Color.rgb(229, 115, 115), mistake);

        VBox gyroZBox = getTopicBox("Абсолютный тангаж",    Color.rgb(77, 208, 225), gyroZ);
        VBox gyroYBox = getTopicBox("Абсолютное рыскание",  Color.rgb(255, 183, 77), gyroY);
        VBox gyroXBox = getTopicBox("Абсолютный крен",      Color.rgb(110, 199, 99), gyroX);

        VBox pitchErrorBox  = getTopicBox("Запястье, тангаж",   Color.rgb(255, 90, 99), pitchError);
        VBox yawErrorBox    = getTopicBox("Запястье, рыскание", Color.rgb(255, 90, 99), yawError);
        VBox wrestErrorBox  = getBox("Запястье, итого",    Color.rgb(255, 90, 99), wrestError);

        pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(39, 44, 50), CornerRadii.EMPTY, Insets.EMPTY)));

        pane.add(pitchBox,          0, 0);
        pane.add(yawBox,            0, 1);
        pane.add(wrestMistakeBox,   0, 2);

        pane.add(gyroZBox,          1, 0);
        pane.add(gyroYBox,          1, 1);
        pane.add(gyroXBox,          1, 2);

        pane.add(pitchErrorBox,     2, 0);
        pane.add(yawErrorBox,       2, 1);
        pane.add(wrestErrorBox,     2, 2);
    }

    private VBox getTopicBox(final String TEXT, final Color color, final Gauge gauge) {
        gauge.setBarColor(color);
        gauge.setBarBackgroundColor(Color.rgb(39, 44, 50));
        gauge.setAnimated(false);

        return getBox(TEXT, color, gauge);
    }

    private VBox getBox(final String TEXT, final Color color, final Control gauge) {
        Rectangle bar = new Rectangle(200, 3);
        bar.setArcWidth(6);
        bar.setArcHeight(6);
        bar.setFill(color);

        Label label = new Label(TEXT);
        label.setTextFill(color);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 10, 0));

        VBox vBox = new VBox(bar, label, gauge);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public void refresh() {
        AccelerometerData data = DataManager.getInstance().getDataContainer().getAndCleanAvgPeak();
        AccelerometerData diff = DataManager.getInstance().getDataContainer().getAvgDiff();

        if (data != null) {
            pitch.setValue(DoubleRounder.round(diff.getDiffAY(),     PRECISION_LEVEL));
            yaw.setValue(DoubleRounder.round(diff.getDiffAZ(),       PRECISION_LEVEL));
            mistake.setValue(DoubleRounder.round(
                    Math.abs(diff.getDiffAZ()) + Math.abs(diff.getDiffAY()),
                    PRECISION_LEVEL));

            gyroX.setValue(DoubleRounder.round(data.avgGyroX(), PRECISION_LEVEL));
            gyroY.setValue(DoubleRounder.round(data.avgGyroY(), PRECISION_LEVEL));
            gyroZ.setValue(DoubleRounder.round(data.avgGyroZ(), PRECISION_LEVEL));

            double pitchErrorValue = (
                    Math.abs(DataHelper.dyAbs(diff)) > PITCH_THRESHOLD)
                    ? DataHelper.dyAbs(diff)
                    : 0;
            double yawErrorValue = (
                    Math.abs(DataHelper.dzAbs(diff)) > YAW_THRESHOLD)
                    ? DataHelper.dzAbs(diff)
                    : 0;

            pitchError.setValue(pitchErrorValue);
            yawError.setValue(yawErrorValue);

            if (pitchErrorValue + yawErrorValue > 0) {
                wrestError.setText("Ошибка!");
            } else {
                wrestError.setText("");
            }
        }
    }
}
