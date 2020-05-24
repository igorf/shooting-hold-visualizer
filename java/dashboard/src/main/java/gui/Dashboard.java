package gui;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import reader.DataReader;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class Dashboard extends Application {

    private static final int SENSOR_REFRESH_DELAY = 500;
    private static final int SENSOR_REFRESH_INTERVAL = 20;
    private SensorSet sensorSet = new SensorSet();

    private Timer timer = new Timer();
    private static SerialPort port;

    @Override
    public void init() {
        sensorSet.init();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(sensorSet.getPane());

        stage.setTitle("Shooting Dashboard");
        stage.setScene(scene);
        stage.show();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refresh());
            }
        }, SENSOR_REFRESH_DELAY, SENSOR_REFRESH_INTERVAL);
    }

    @Override
    public void stop() {
        DataReader.getInstance().stop();
        port.closePort();
        System.exit(0);
    }

    private void refresh() {
        sensorSet.refresh();
    }

    public static void main(String[] args) {
        for (SerialPort s: SerialPort.getCommPorts()) {
            System.out.println(s.getSystemPortName());
        }

        port = SerialPort.getCommPort("tty.usbmodem14101");
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        port.openPort();

        DataReader.getInstance().getReader().setSerialPort(port);
        DataReader.getInstance().start();

        launch(args);
    }
}
