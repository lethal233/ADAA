package lab11;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class a1 {

    public static long[] lengths = new long[61];
    public static long[] vs = new long[61];
    public static long[] cs = new long[61];
    public static long[] ns = new long[61];

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        fillInArray();
        int testcase = in.nextInt();
        long end;
        for (int i = 0; i < testcase; i++) {
            end = in.nextLong();
            Pair tmp = count_vcn(end);
            out.println(tmp.v + " " + tmp.c + " " + tmp.n);
        }

        out.close();
    }

    static class Pair {
        long v;
        long c;
        long n;

        public Pair(long v, long c, long n) {
            this.v = v;
            this.c = c;
            this.n = n;
        }
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

    public static void fillInArray() {
        lengths[0] = 1;
        vs[0] = 1;
        cs[0] = 0;
        ns[0] = 0;

        for (int i = 1; i < 61; i++) {
            lengths[i] = lengths[i - 1] * 2;
            vs[i] = vs[i - 1] + ns[i - 1];
            cs[i] = cs[i - 1] + vs[i - 1];
            ns[i] = ns[i - 1] + cs[i - 1];
        }
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
            } else {
                sta = mid + 1;
                result = mid;
            }
        }
        return result;
    }

    public static Pair count_vcn(long L) {
        if (L == 0) {
            return new Pair(0, 0, 0);
        }
        int index = findIndex(L);
        Pair tmp = new Pair(vs[index], cs[index], ns[index]);
        Pair tp2 = count_vcn(L - lengths[index]);
        return new Pair(tmp.v + tp2.n, tmp.c + tp2.v, tmp.n + tp2.c);
    }
}
