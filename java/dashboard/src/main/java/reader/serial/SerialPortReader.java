package reader.serial;

import com.fazecast.jSerialComm.SerialPort;
import lombok.Setter;
import reader.StreamToLinesBuffer;

public class SerialPortReader {
    @Setter
    private SerialPort serialPort;
    private final StreamToLinesBuffer streamToLinesBuffer;

    public SerialPortReader(StreamToLinesBuffer streamToLinesBuffer) {
        this.streamToLinesBuffer = streamToLinesBuffer;
    }

    public void readChunk() {
        int toRead = serialPort.bytesAvailable();
        if (toRead > 0) {
            byte[] readBuffer = new byte[toRead];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
            if (numRead > 0) {
                streamToLinesBuffer.addToBuffer(readBuffer);
            }
        }
    }
}
