package lab8;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static Node[] nodes;
    public static int[] time = new int[100000009];
    public static long result = 0L;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int N = in.nextInt();
        nodes = new Node[N];
        int[] apt = new int[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node(in.nextInt(), in.nextInt(), in.nextLong());
        }
        //构建活跃点
        qsapt(nodes, 0, N - 1);
        int slot = 0;
        for (int i = 0; i < N; i++) {
            slot = Math.max(slot + 1, nodes[i].S);
            apt[i] = slot;
        }

        Arrays.fill(time, -2);
        qsv(nodes, 0, N - 1);
        for (int i = 0; i < N; i++) {
            nodes[i].ind = i;
        }
        int x;
        for (int i = 0; i < N; i++) {
            x = nodes[i].S;
            if (find(nodes[i], x)) {
                result += nodes[i].V;
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

    static class Node {
        int S;
        int T;
        long V;
        int ind;
        int sl;

        public Node(int s, int t, long v) {
            S = s;
            T = t;
            V = v;
            ind = -1;
            sl = 0;
        }
    }

    public static void qsv(Node[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Node temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].V <= temp.V) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].V >= temp.V) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        qsv(arr, low, left - 1);
        qsv(arr, left + 1, high);
    }

    public static void qsapt(Node[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Node temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].S >= temp.S) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].S <= temp.S) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        qsapt(arr, low, left - 1);
        qsapt(arr, left + 1, high);
    }

    public static boolean find(Node y, int x) {
        if (x > y.T) {
            return false;
        }
        if (time[x] == -2) {
            time[x] = y.ind;
            return true;
        }
        Node tmp = nodes[time[x]];
        if (y.T > tmp.T) {
            return find(y, x + 1);
        } else {
            if (find(tmp, x + 1)) {
                time[x] = y.ind;
                return true;
            } else {
                return false;
            }
        }
    }
}
