package gui;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import reader.DataReader;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

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
        CommandLine cmd = parseOptions(args);
        if (cmd != null) {
            connectToComPort(cmd.getOptionValue("port"));
            launch(args);
        } else {
            System.exit(-1);
        }
    }

    private static CommandLine parseOptions(String ... args) {
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "port");
        portOption.setRequired(true);
        options.addOption(portOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Options error: ", e);
            formatter.printHelp("dashboard", options);
            log.info("Possible com ports: {}", Arrays
                    .stream(SerialPort.getCommPorts())
                    .map(SerialPort::getSystemPortName)
                    .collect(Collectors.toList()));
        }

        return null;
    }

    private static void connectToComPort(String portName) {
        port = SerialPort.getCommPort(portName);
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        if (port.openPort()) {
            DataReader.getInstance().getReader().setSerialPort(port);
            DataReader.getInstance().start();
        } else {
            throw new RuntimeException("Could not open given port " + portName);
        }
    }
}
