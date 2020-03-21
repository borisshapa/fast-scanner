import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of the fast analogue of the {@link java.util.Scanner} class.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 * @see java.util.Scanner
 */
public class FastScanner implements AutoCloseable {
    final private int BUFFER_SIZE = 1 << 7;
    private byte[] buffer = new byte[BUFFER_SIZE];
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
     * Constructs a new Scanner that produces values scanned from the specified file.
     *
     * @param file A file to be scanned
     * @throws FileNotFoundException if source is not found
     */
    public FastScanner(File file) throws FileNotFoundException {
        in = new FileInputStream(file);
    }

    /**
     * Constructs a new Scanner that produces values scanned from standard system input
     */
    public FastScanner() {
        new FastScanner(System.in);
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
     * Scans the next token of the input as a char.
     *
     * @return the char scanned from the input
     * @throws IOException if an error occurred while reading the file
     */
    private char nextChar() throws IOException {
        char c;
        int octet1 = read();
        if (octet1 == -1) {
            return Character.MIN_VALUE;
        }
        int cntUnits = 0;
        for (int i = 7; i >= 0; i--) {
            if (((1 << i) & octet1) == 0) {
                break;
            }
            cntUnits++;
        }
        c = (char) (octet1 % (1 << (7 - cntUnits)));
        for (int i = 0; i < cntUnits - 1; i++) {
            int nextOctet = read();
            c = (char) ((c << 6) + (nextOctet % (1 << 6)));
        }
        return c;
    }

    /**
     * Finds and returns the next complete token from this scanner.
     * A complete token is preceded and followed by input that matches the delimiter pattern.
     *
     * @return the next token
     * @throws IOException if an error occurred while reading the file
     */
    public String next() throws IOException {
        StringBuilder str = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            str.append(c);
            c = nextChar();
        }
        return str.toString();
    }


    /**
     * Scans the next token of the input as an int.
     *
     * @return the int scanned from the input
     * @throws IOException if an error occurred while reading the file
     */
    public int nextInt() throws IOException {
        StringBuilder strToInt = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToInt.append(c);
            c = nextChar();
        }
        return Integer.parseInt(strToInt.toString());
    }

    /**
     * Scans the next token of the input as a long.
     *
     * @return the long scanned from the input
     * @throws IOException if an error occurred while reading the file
     */
    public long nextLong() throws IOException {
        StringBuilder strToLong = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToLong.append(c);
            c = nextChar();
        }
        return Long.parseLong(strToLong.toString());
    }

    /**
     * Scans the next token of the input as a double.
     * This method will throw InputMismatchException if the next token cannot be translated into a valid double value.
     * If the translation is successful, the scanner advances past the input that matched.
     *
     * @return the double scanned from the input
     * @throws IOException if an error occurred while reading the file
     */
    public double nextDouble() throws IOException {
        StringBuilder strToDouble = new StringBuilder();
        char c = nextChar();
        while (Character.isWhitespace(c)) {
            c = nextChar();
        }
        while (!Character.isWhitespace(c)) {
            strToDouble.append(c);
            c = nextChar();
        }
        return Double.parseDouble(strToDouble.toString());
    }

    /**
     * Returns true if there is another line in the input of this scanner.
     *
     * @return true if and only if this scanner has another line of input
     * @throws IOException if an error occurred while reading the file
     */
    public boolean hasNextLine() throws IOException {
        read();
        bufferPointer--;
        return bytesRead != -1;
    }

    /**
     * Returns true if this scanner has another token in its input.
     *
     * @return true if and only if this scanner has another token
     * @throws IOException if an error occurred while reading the file
     */
    public boolean hasNext() throws IOException {
        int c = read();
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
     * @throws IOException if an error occurred while reading the file
     */
    public String nextLine() throws IOException {
        StringBuilder str = new StringBuilder();
        char lineSep = System.lineSeparator().charAt(0);
        char c = nextChar();
        while (bytesRead != -1) {
            if (c == lineSep)
                break;
            str.append(c);
            c = nextChar();
        }
        return str.toString();
    }

    private void fillBuffer() throws IOException {
        do {
            bytesRead = in.read(buffer);
        } while (bytesRead == 0);
        bufferPointer = 0;
    }

    private int read() throws IOException {
        if (bufferPointer == bytesRead)
            fillBuffer();
        bufferPointer++;
        return (buffer[bufferPointer - 1] < 0) ? buffer[bufferPointer - 1] + (1 << 8) : buffer[bufferPointer - 1];
    }

    /**
     * Closes this scanner.
     */
    public void close() throws IOException {
        if (in == null)
            return;
        in.close();
    }
}
