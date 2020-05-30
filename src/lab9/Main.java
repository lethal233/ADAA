package lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static long[] lengths = new long[61];
    public static long[] vs = new long[61];
    public static long result;
    public static Map<Long, Long> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        fillInArr();
        int testcase = in.nextInt();
        long L, R;
        for (int i = 0; i < testcase; i++) {
            result = 0L;
            L = in.nextLong();
            R = in.nextLong();
            out.println(solution(L - 1, R));
        }
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

    public static void fillInArr() {
        lengths[0] = 0;
        vs[0] = 0;
        map.put(0L, 0L);
        lengths[1] = 1;
        vs[1] = 1;
        map.put(1L, 1L);
        for (int i = 2; i < lengths.length; i++) {
            lengths[i] = lengths[i - 1] * 2 + 1;
            vs[i] = vs[i - 1] * 2;
            map.put(lengths[i], vs[i]);
        }
    }

    public static long solution(long L, long R) {
        long r1, r2;
        int idx1, idx2;
        if (map.containsKey(L)) {
            r1 = map.get(L);
        } else {
            idx1 = findIndex(L);
            r1 = getR(L, idx1);
        }
        if (map.containsKey(R)) {
            r2 = map.get(R);
        } else {
            idx2 = findIndex(R);
            r2 = getR(R, idx2);
        }
        return r2 - r1;
    }

    public static long getR(long x, int idx) {
        long mark = 0L;
        long sta = 1;
        long end = lengths[idx];
        boolean flag = false;
        while (sta <= end) {
            long mid = (sta + end) >> 1;
            if (mid == x) {
                if (!flag) {
                    mark++;
                }
                mark += map.get(mid - sta);
                break;
            } else if (mid < x) {
                if (!flag) {
                    mark++;
                }
                flag = true;
                mark += map.get(mid - sta);
                sta = mid + 1;
            } else {
                flag = false;
                end = mid - 1;
            }
        }
        return mark;
    }

    public static int findIndex(long num) {
        int sta = 0;
        int end = lengths.length;
        int result = 0;
        while (sta <= end) {
            int mid = (sta + end) >> 1;
            if (lengths[mid] == num) {
                return mid;
            } else if (lengths[mid] > num) {
                end = mid - 1;
                result = mid;
            } else {
                sta = mid + 1;
            }
        }
        return result;
    }
}
