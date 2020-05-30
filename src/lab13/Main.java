package lab13;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    private static final int mod = 998244353;
    public static int[] st = new int[100009];
    public static long[][][] dp = new long[2][201][3];
    // 0 for greater than the previous one, 1 for equivalence, 2 for less than
    public static int index = 0;
    public static long previous, latter;
    public static int tmp, n;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        n = in.nextInt();
        for (int i = 1; i <= n; i++) {
            st[i] = in.nextInt();
        }
        init();
        out.println(solution());
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

    public static void init() {
        //initialization
        //Suppose that the zeroth day is 0, then the first day is greater than the zeroth day
        if (st[1] == -1) {
            for (int i = 1; i <= 200; i++) {
                dp[index][i][0] = 1;
            }
        } else {
            dp[index][st[1]][0] = 1;
        }
    }

    //the code idea is from luogu cf1067a, rectified by me
    public static long solution() {
        for (int i = 2; i <= n; i++) {
            previous = 0;
            latter = 0;
            tmp = index ^ 1;
            for (int j = 1; j <= 200; j++) {
                long tempmod = dp[index][j][0] % mod + dp[index][j][1] % mod + dp[index][j][2] % mod;
                if (st[i] == -1 || st[i] == j) { //if the st[i] equals to -1 or j, which means it can choose value of j
                    dp[tmp][j][0] = previous;
                    dp[tmp][j][1] = tempmod % mod;
                } else {
                    dp[tmp][j][0] = 0;
                    dp[tmp][j][1] = 0;
                }
                previous = (tempmod + previous % mod) % mod;
            }
            for (int j = 200; j > 0; j--) {
                if (st[i] == -1 || st[i] == j) {
                    dp[tmp][j][2] = latter;
                } else {
                    dp[tmp][j][2] = 0;
                }
                latter = (latter % mod + dp[index][j][1] % mod + dp[index][j][2] % mod) % mod;
            }
            index ^= 1;
        }
        long result = 0;
        // because in the last one, we consider an-1>=an, so we the dp[index][i][0] is omitted.
        for (int i = 1; i <= 200; i++) {
            result = (result % mod + dp[index][i][1] % mod + dp[index][i][2] % mod) % mod;
        }
        return result;
    }
}
