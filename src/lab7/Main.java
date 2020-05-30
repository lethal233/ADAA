package lab7;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static int N;
    public static Edge[] ary;// the array of edges
    public static int[] pre;// the parent of each set
    public static int[] count;// the rank of each set
    public static int total;// the number of vertices
    public static long result = 0L;
    public static int counter = 0;// the length of the heap

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        N = in.nextInt();
        total = N + 1;
        int length = ((N + 1) * N) >> 1;
        ary = new Edge[length];
        pre = new int[N + 2];
        count = new int[N + 2];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N - i + 1; j++) {
                long weight = in.nextLong();
                ary[counter++] = new Edge(i, i + j, weight);
            }
        }
        for (int i = 1; i < N + 2; i++) {
            pre[i] = i;
        }
        buildHeap();
        while (!isEmpty()) {
            Edge tmp = popMin();
            int x = unionFind(tmp.u);
            int y = unionFind(tmp.v);
            if (x != y) {
                //optimized method that comes from website
                if (count[x] > count[y]) {
                    pre[y] = x;
                } else {
                    pre[x] = y;
                    if (count[x] == count[y]) {
                        count[y]++;
                    }
                }
                total--;
                result += tmp.w;
            }
            if (total == 1) {
                break;
            }
        }
        out.println(result);
        out.close();
    }

    public static int unionFind(int r) {
        int l = r;
        int tmp;
        while (r != pre[r]) {
            r = pre[r];
        }
        //shorten the path, from website
        while (l != r) {
            tmp = pre[l];
            pre[l] = r;
            l = tmp;
        }
        return r;
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

    static class Edge {
        int u;
        int v;
        long w;

        public Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public static void buildHeap() {
        for (int i = (ary.length - 1) / 2; i >= 0; i--) {
            int parent = i;
            int child = 2 * i + 1;
            while (child <= ary.length - 1) {
                if (child + 1 <= ary.length - 1 && ary[child].w > ary[child + 1].w) {
                    child++;
                }
                if (ary[parent].w > ary[child].w) {
                    Edge tmp = ary[parent];
                    ary[parent] = ary[child];
                    ary[child] = tmp;
                } else {
                    break;
                }
                parent = child;
                child = 2 * parent + 1;
            }
        }
    }

    public static Edge popMin() {
        Edge tmp = ary[0];
        ary[0] = ary[counter - 1];
        ary[counter - 1] = new Edge(0, 0, 0);
        counter--;
        downAdjustment();
        return tmp;
    }

    public static boolean isEmpty() {
        return counter == 0;
    }

    public static void downAdjustment() {
        int c = 0;
        Edge temp = ary[c];
        int child = 2 * c + 1;
        while (child <= counter - 1) {
            if (child + 1 <= counter - 1 && ary[child].w > ary[child + 1].w) {
                child++;
            }
            if (temp.w <= ary[child].w) {
                break;
            }
            Edge tmp = ary[c];
            ary[c] = ary[child];
            ary[child] = tmp;
            c = child;
            child = child * 2 + 1;
        }
    }
}

