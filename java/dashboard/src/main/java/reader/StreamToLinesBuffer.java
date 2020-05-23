package reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StreamToLinesBuffer {
    private String buffer = "";

    public synchronized void addToBuffer(byte[] readBuffer) {
        buffer += new String(readBuffer);
    }

    public synchronized List<String> getFullElements() {
        String lines[] = buffer.split("(\r\n|\r|\n)", -1);
        List<String> result = new ArrayList<>(lines.length);

        result.addAll(Arrays.asList(lines).subList(0, lines.length - 1));

        if (lines[lines.length - 1].endsWith("}")) {
            result.add(lines[lines.length - 1]);
            buffer = "";
        } else {
            buffer = lines[lines.length - 1];
        }

        return result;
    }
}
