package lab4;
//lab4 2

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static int n;
    public static final int CONSTANT = 998244353;
    public static final int NUMBER = 15;
    public static long[] score = new long[NUMBER];
    public static long[] nowScore = new long[NUMBER];
    public static long[] differScore = new long[NUMBER];
    public static long sum = 0;
    public static long winCount;
    public static long drawCount;
    public static int prime = 37;
    public static Map<Long, Long> h = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        n = in.nextInt();
        for (int i = 1; i <= n; i++) {
            score[i] = in.nextLong();
            sum += score[i];
        }
        winCount = sum - n * n + n;
        drawCount = (sum - 3 * winCount) / 2;
        quickSort(score,1,n);
        out.println(DFS(1, 2) % CONSTANT);
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

    //idea that comes from the luogu
    public static long DFS(int i, int j) {
        long result = 0;
        if (i >= n) {
            return 1;
        }
        //if winning all the games cannot satisfy the condition, then prune it
        if (nowScore[i] + 3 * (n - j + 1) < score[i]) {
            return 0;
        }
        //if gamer i has already taken competition with other all people
        if (j > n) {
            for (int k = i + 1; k <= n; k++) {
                differScore[k] = score[k] - nowScore[k];
            }
            quickSort(differScore, i + 1, n);
            long key = 0;
            for (int k = i + 1; k <= n; k++) {
                key = key * prime + differScore[k];
            }
            if (!h.containsKey(key)) {
                h.put(key, DFS(i + 1, i + 2));
            }
            return h.get(key);
        }
        if (nowScore[i] + 3 <= score[i] && winCount != 0) {
            nowScore[i] += 3;
            winCount--;
            result += DFS(i, j + 1);
            nowScore[i] -= 3;
            winCount++;
        }
        if (nowScore[i] + 2 <= score[i] && nowScore[j] + 1 <= score[j] && winCount != 0) {
            nowScore[i] += 2;
            nowScore[j] += 1;
            winCount--;
            result += DFS(i, j + 1);
            nowScore[i] -= 2;
            nowScore[j] -= 1;
            winCount++;
        }
        if (nowScore[i] + 1 <= score[i] && nowScore[j] + 1 <= score[j] && drawCount != 0) {
            nowScore[i]++;
            nowScore[j]++;
            drawCount--;
            result += DFS(i, j + 1);
            nowScore[i]--;
            nowScore[j]--;
            drawCount++;
        }
        if (nowScore[j] + 2 <= score[j] && nowScore[i] + 1 <= score[i] && winCount != 0) {
            nowScore[j] += 2;
            nowScore[i] += 1;
            winCount--;
            result += DFS(i, j + 1);
            nowScore[j] -= 2;
            nowScore[i] -= 1;
            winCount++;
        }
        if (nowScore[j] + 3 <= score[j] && winCount != 0) {
            nowScore[j] += 3;
            winCount--;
            result += DFS(i, j + 1);
            nowScore[j] -= 3;
            winCount++;
        }
        return result % CONSTANT;
    }
    //template quicksorting that has been rectified by me
    public static void quickSort(long[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        long temp = arr[left];
        while (left < right) {
            while (left < right && arr[right] >= temp) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left] <= temp) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort(arr, low, left - 1);
        quickSort(arr, left + 1, high);
    }
}
