package lab5;//快读模板1：更快，但没有next()用于读字符串

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class a1 {

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int testcase = in.nextInt();
        for (int i = 0; i < testcase; i++) {
            int number = 0;
            int N = in.nextInt();
            Pair[] ary = new Pair[N];
            for (int j = 0; j < N; j++) {
                int a = in.nextInt();
                int b = in.nextInt();
                ary[j] = new Pair(a, b);
            }
//            MergeSort(ary, 0, ary.length - 1);
            quickSort(ary, 0, ary.length - 1);
            int endtime = 0;
            for (int j = 0; j < N; j++) {
                if (ary[j].start > endtime) {
                    endtime = ary[j].end;
                    number++;
                }
            }
            out.println(number);
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

    static class Pair {
        int start;
        int end;

        public Pair(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static Pair[] MergeSort(Pair[] A, int sta, int end) {
        if (sta < end) {
            int mid = (sta + end) / 2;
            MergeSort(A, sta, mid);
            MergeSort(A, mid + 1, end);
            Merge(A, sta, mid, end);
        }
        return A;
    }

    public static void Merge(Pair[] A, int sta, int mid, int end) {
        Pair[] ary = new Pair[end - sta + 1];
        int i = sta;
        int j = mid + 1;
        for (int k = 0; k < ary.length; k++) {
            if (i <= mid && (j > end || A[i].end <= A[j].end)) {
                ary[k] = A[i];
                i++;
            } else {
                ary[k] = A[j];
                j++;
            }
        }
        for (int t = 0; t < ary.length; t++) {
            A[t + sta] = ary[t];
        }
    }

    public static void quickSort(Pair[] arr, int low, int high) {
        if (arr.length <= 0) return;
        if (low >= high) return;
        int left = low;
        int right = high;
        Pair temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].end >= temp.end) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].end <= temp.end) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort(arr, low, left - 1);
        quickSort(arr, left + 1, high);

    }

}
