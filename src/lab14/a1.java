package lab14;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class a1 {
    public static Queue<Integer> q = new LinkedList<>();
    public static int maxn = 1002;
    public static int[][] map = new int[maxn][maxn];
    public static int[] pre = new int[maxn];
    public static boolean[] flag = new boolean[maxn];
    public static int N, M;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        N = in.nextInt();
        M = in.nextInt();
        int color,i,j;
        for (int k = 1; k <= N; k++) {
            color = in.nextInt();
            if (color == 1) {
                map[0][k] = 1;
            } else if (color == 0) {
                map[k][N + 1] = 1;
            }
        }
        for (int k = 0; k < M; k++) {
            i = in.nextInt();
            j = in.nextInt();
            map[i][j]++;
            map[j][i]++;
        }
        out.println(EdmondsKarp(0, N + 1));
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

    public static boolean bfs(int start, int end) {
        Arrays.fill(flag, false);
        Arrays.fill(pre, -1);
        q.clear();
        flag[start] = true;
        q.offer(start);
        int head;
        while (!q.isEmpty()) {
            if ((head = q.poll()) == end) {
                return true;
            } else {
                for (int i = 1; i <= N + 1; i++) {
                    if (map[head][i] > 0 && !flag[i]) {
                        flag[i] = true;
                        q.offer(i);
                        pre[i] = head;
                    }
                }
            }
        }
        return false;
    }

    public static long EdmondsKarp(int start, int end) {
        long maxFlow = 0;
        int u;
        while (bfs(start, end)) {
            u = end;
            maxFlow++;
            while (u != start) {
                map[pre[u]][u]--;
                map[u][pre[u]]++;
                u = pre[u];
            }
        }
        return maxFlow;
    }
}
