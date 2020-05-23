package reader;

import data.filler.DataManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reader.serial.SerialPortReader;

import java.io.IOException;
import java.util.List;

@Slf4j
public class DataReader {
    private static final int CONVERT_DELAY = 150;
    private static final int READ_DELAY = 100;

    private static DataReader INSTANCE;

    @Getter private SerialPortReader reader;
    private StreamToLinesBuffer buffer;
    private Thread readerThread;
    private Thread bufferThread;


    public synchronized static DataReader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataReader();
        }
        return INSTANCE;
    }

    private DataReader() {
        super();
        buffer = new StreamToLinesBuffer();
        reader = new SerialPortReader(buffer);
    }

    private void startReader() {
        readerThread = new Thread(() -> {
            while(true) {
                try {
                    reader.readChunk();
                } catch (Exception ex) {
                    log.error("Could not read from source", ex);
                }
                try {
                    Thread.sleep(READ_DELAY);
                } catch (InterruptedException e) {
                    log.debug("Interrupted");
                }
            }
        });
        readerThread.start();
    }

    private void startBufferToData() {
        bufferThread = new Thread(() -> {
            while(true) {
                try {
                    List<String> strings = buffer.getFullElements();
                    for (String s: strings) {
                        try {
                            DataManager.getInstance().put(s);
                        } catch (IOException io) {
                            log.error("Could not translate message to data: {}", s, io);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Could not translate buffer", ex);
                }

                try {
                    Thread.sleep(CONVERT_DELAY);
                } catch (InterruptedException e) {
                    log.debug("Interrupted");
                }
            }
        });
        bufferThread.start();
    }

    public void start() {
        startReader();
        startBufferToData();
    }

    public void stop() {
        try {
            readerThread.interrupt();
        } catch (Exception ex) {
            log.error("Could not stop Reader thread: ", ex);
        }

        try {
            bufferThread.interrupt();
        } catch (Exception ex) {
            log.error("Could not stop buffer thread thread: ", ex);
        }
    }
}
