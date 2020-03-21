import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Implementation of the analogue of the {@link java.util.Scanner} class.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 * @see java.util.Scanner
 */
public class FastScanner {
    final private int BUFFER_SIZE = 1 << 7;
    private byte[] buffer;
    private int bufferPointer, bytesRead;
    private InputStream in;

    /**
     * Constructs a new Scanner that produces values scanned from the specified input stream.
     *
     * @param inputStream An input stream to be scanned
     */
    public FastScanner(final InputStream inputStream) {
        in = inputStream;
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = bytesRead = 0;
    }

    /**
     * Constructs a new Scanner that produces values scanned from the specified {@link String}.
     *
     * @param s A string to scan
     */
    public FastScanner(String s) {
        in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        buffer = new byte[BUFFER_SIZE];
        bufferPointer = bytesRead = 0;
    }

    /**
     * Finds and returns the next complete token from this scanner.
     * A complete token is preceded and followed by input that matches the delimiter pattern.
     *
     * @return the next token
     */
    public String next() {
        StringBuilder str = new StringBuilder();
        byte c = read();
        while (Character.isWhitespace(c)) {
            c = read();
        }
        while (!Character.isWhitespace(c)) {
            str.append((char) c);
            c = read();
        }
        return str.toString();
    }

    /**
     * Scans the next token of the input as an int.
     *
     * @return the int scanned from the input
     */
    public int nextInt() {
        int ans = 0;
        int sign = 1;
        byte c = read();
        while (Character.isWhitespace(c)) {
            c = read();
        }
        if (c == '-') {
            sign = -1;
        }
        if (c == '-' || c == '+') {
            c = read();
        }
        while (!Character.isWhitespace(c) && bytesRead != -1) {
            if (c >= '0' && c <= '9') {
                ans = ans * 10 + (c - '0');
                c = read();
            } else {
                throw new InputMismatchException();
            }
        }
        return ans * sign;
    }

    /**
     * Scans the next token of the input as a long.
     *
     * @return the long scanned from the input
     */
    public long nextLong() {
        long ans = 0;
        int sign = 1;
        byte c = read();
        while (Character.isWhitespace(c))
            c = read();
        if (c == '-') {
            sign = -1;
        }
        if (c == '-' || c == '+') {
            c = read();
        }
        while (!Character.isWhitespace(c) && bytesRead != -1) {
            if (c >= '0' && c <= '9') {
                ans = ans * 10L + (c - '0');
                c = read();
            } else {
                throw new InputMismatchException();
            }
        }
        return ans * sign;
    }

    /**
     * Scans the next token of the input as a double.
     * This method will throw InputMismatchException if the next token cannot be translated into a valid double value.
     * If the translation is successful, the scanner advances past the input that matched.
     *
     * @return the double scanned from the input
     */
    public double nextDouble() {
        double ans = 0.0;
        int sign = 1;
        byte c = read();
        while (Character.isWhitespace(c)) {
            c = read();
        }
        if (c == '-') {
            sign = -1;
        }
        if (c == '-' || c == '+') {
            c = read();
        }
        while (!Character.isWhitespace(c) && c != '.' && bytesRead != -1) {
            if (c >= '0' && c <= '9') {
                ans = 10 * ans + (c - '0');
                c = read();
            } else {
                throw new InputMismatchException();
            }
        }
        if (c == '.') {
            c = read();
            double fraction = 1.0;
            while (!Character.isWhitespace(c) && bytesRead != -1) {
                if (c >= '0' && c <= '9') {
                    fraction /= 10;
                    ans += (c - '0') * fraction;
                    c = read();
                } else {
                    throw new InputMismatchException();
                }
            }
        }
        return ans * sign;
    }

    /**
     * Returns true if there is another line in the input of this scanner.
     *
     * @return true if and only if this scanner has another line of input
     */
    public boolean hasNextLine() {
        do {
            read();
            bufferPointer--;
        } while (bytesRead == 0);
        return bytesRead != -1;
    }

    /**
     * Returns true if this scanner has another token in its input.
     *
     * @return true if and only if this scanner has another token
     */
    public boolean hasNext() {
        byte c = read();
        int pointer = 1;
        while (Character.isWhitespace(c) && bufferPointer < bytesRead) {
            c = read();
            pointer++;
        }
        bufferPointer -= pointer;
        return !Character.isWhitespace(c) && bytesRead != -1;
    }

    /**
     * Advances this scanner past the current line and returns the input that was skipped.
     * This method returns the rest of the current line, excluding any line separator at the end.
     * The position is set to the beginning of the next line.
     *
     * @return the line that was skipped
     */
    public String nextLine() {
        StringBuilder str = new StringBuilder();
        byte c;
        String s = System.lineSeparator();
        char p = s.charAt(0);
        while ((c = read()) != -1) {
            if (c == p)
                break;
            str.append((char) c);
        }
        return str.toString();
    }

    private void fillBuffer() {
        try {
            do {
                bytesRead = in.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            } while (bytesRead != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bytesRead == -1)
            buffer[0] = -1;
    }

    private byte read() {
        if (bufferPointer == bytesRead) {
            fillBuffer();
        }
        return buffer[bufferPointer++];
    }

    /**
     * Closes this scanner.
     */
    public void close() {
        if (in == null)
            return;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}