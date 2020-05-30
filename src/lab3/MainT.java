package lab3;//lab3.2
//tle

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MainT {
    public static int CONSTANT = 998244353;
    public static int NUMBER;
    public static int[] array;
    public static int[] count = new int[11];
    /*public static long[] powerCo = {
            1L,                     10L,
            100L,                   1000L,
            10000L,                 100000L,
            1000000L,               10000000L,
            100000000L,             1000000000L,
            10000000000L,           100000000000L,
            1000000000000L,         10000000000000L,
            100000000000000L,       1000000000000000L,
            10000000000000000L,     100000000000000000L,
            1000000000000000000L,   };*/
    public static long[] powerCo =
            {1L, 10L, 100L, 1000L, 10000L,
                    100000L, 1000000L, 10000000L, 100000000L, 1755647L, 17556470L,
                    175564700L, 757402647L, 586315999L, 871938225L,
                    733427426L, 346563789L, 470904831L, 716070898L};

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        NUMBER = in.nextInt();
        array = new int[NUMBER];
        long result = 0L;
        for (int i = 0; i < NUMBER; i++) {
            array[i] = in.nextInt();
            count[String.valueOf(array[i]).length()]++;
        }
        for (int i = 0; i < NUMBER; i++) {
            String tmp = String.valueOf(array[i]);
            int length = tmp.length();
            // 先对每个长度进行遍历
            for (int j = 1; j < count.length - 1; j++) {
                //对 数的每个位置进行遍历
                if (length <= j) {
                    for (int k = 0; k < length; k++) {
                        int power = 2 * length - 2 - 2 * k;
                        int a = tmp.charAt(k) - '0';
                        result = ((count[j] * a * 11 * powerCo[power]) % CONSTANT + result) % CONSTANT;
//                        result = ((count[j] * a * 11 * ((long) Math.pow(10, power) % CONSTANT)) % CONSTANT + result) % CONSTANT;
                    }
                } else {
                    int difference = length - j;
                    long left = Long.parseLong(tmp.substring(0, difference));
                    result = (result + (powerCo[2 * j] * left * 2 * count[j]) % CONSTANT) % CONSTANT;
//                    result = (result + (((long) Math.pow(10, 2 * j) % CONSTANT) * left * 2 * count[j]) % CONSTANT) % CONSTANT;
                    for (int k = difference; k < length; k++) {
                        int a = tmp.charAt(k) - '0';
                        int power = 2 * length - 2 - 2 * k;
                        result = ((a * 11 * count[j] * powerCo[power]) % CONSTANT + result) % CONSTANT;
//                        result = ((a * 11 * count[j] * ((long) Math.pow(10, power) % CONSTANT)) % CONSTANT + result) % CONSTANT;
                    }
                }
            }
        }
        out.println(result);
        out.close();
    }


    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n')
                    break;
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            }
            while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null)
                return;
            din.close();
        }
    }

}
