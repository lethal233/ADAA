package lab7;
//这个题不能用最小堆，会超时，因为我们考虑到只用从小到大依次取，我们就可以直接快排，一个个取进行处理

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class a1 {
    public static long[][] matrix;
    public static int[] pre;
    public static int[] rnk;
    public static long result = 0L;
    public static int counter;
    public static Edge[] ary;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int m = in.nextInt();
        int total = n * m;
        counter = total;
        matrix = new long[n + 1][m + 1];
        pre = new int[total + 1];
        rnk = new int[total + 1];
        ary = new Edge[2 * total];
        int edgeCounter = 1;
        for (int i = 1; i <= total; i++) {
            pre[i] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int x = (i - 1) * m + j;
                matrix[i][j] = in.nextLong();
                if (j - 1 >= 1) {
                    long left = matrix[i][j - 1];
                    ary[edgeCounter++] = new Edge(x - 1, x, matrix[i][j] ^ left);
                }
                if (i - 1 >= 1) {
                    long up = matrix[i - 1][j];
                    ary[edgeCounter++] = new Edge(x - m, x, matrix[i][j] ^ up);
                }
            }
        }
        quickSort(ary, 1, edgeCounter - 1);
        for (int i = 1; i < edgeCounter; i++) {
            Edge tmp = ary[i];
            int x = unionSearch(tmp.u);
            int y = unionSearch(tmp.v);
            if (x != y) {
                if (rnk[x] > rnk[y]) {
                    pre[y] = x;
                } else {
                    pre[x] = y;
                    if (rnk[x] == rnk[y]) {
                        rnk[y]++;
                    }
                }
                counter--;
                result += tmp.w;
            }
            if (counter == 1) {
                break;
            }
        }
        out.println(result);
        out.close();
    }

    public static int unionSearch(int root) {
        int low = root;
        int tmp;
        while (root != pre[root]) {
            root = pre[root];
        }
        while (low != root) {
            tmp = pre[low];
            pre[low] = root;
            low = tmp;
        }
        return root;
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

    static class Edge  {
        int u;
        int v;
        long w;

        public Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public Edge() {
            this(0, 0, 0);
        }
    }

    public static void quickSort(Edge[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Edge temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].w >= temp.w) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].w <= temp.w) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort(arr, low, left - 1);
        quickSort(arr, left + 1, high);
    }

    public static void swap(Edge[] ary, int u, int v) {
        Edge tmp = ary[u];
        ary[u] = ary[v];
        ary[v] = tmp;
    }

//    public static void quickSort(Edge[] arr, int lo, int hi) {
//        if (lo < hi) {
//            int p = partition(arr, lo, hi);
//            quickSort(arr, lo, p - 1);
//            quickSort(arr, p + 1, hi);
//        }
//    }
//
//    public static int partition(Edge[] arr, int lo, int hi) {
//        Random r = new Random();
//        int p = r.nextInt(hi - lo + 1) + lo;
//        Edge pivot = arr[p];
//        swap(arr, p, hi);
//        int L = lo - 1;
//
//        for (int i = lo; i < hi; i++) {
//            if (arr[i].w <= pivot.w) {
//                ++L;
//                swap(arr, L, i);
//            }
//        }
//        swap(arr, L + 1, hi);
//        return L + 1;
//    }

}
