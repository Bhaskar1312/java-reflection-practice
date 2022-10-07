package internal;

public class ImageBuffer {
    private static final int MAX_SIZE = 500;
    private final byte[] buffer;

    public ImageBuffer(Integer size) {
        this.buffer = new byte[size];
    }

    ImageBuffer(byte [] buffer) {
        this.buffer = buffer;
    }

    private ImageBuffer() {
        this.buffer = new byte[MAX_SIZE];
    }
}