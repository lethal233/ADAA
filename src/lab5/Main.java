package lab5;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static int maxInterval = Integer.MAX_VALUE;
    public static int N;
    public static priorQueue q = new priorQueue();
    public static int[] count;
    public static int maxStamp;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        N = in.nextInt();
        Pair[] array = new Pair[N];
        count = new int[N + 1];
        int a = 0;
        int b = 0;
        int c = 0;
        for (int i = 0; i < N; i++) {
            a = in.nextInt();
            b = in.nextInt();
            c = b - a + 1;
            if (maxStamp < b) {
                maxStamp = b;
            }
            array[i] = new Pair(a, b, i);
            if (c < maxInterval) {
                maxInterval = c;
            }
        }
        quickSort(array, 0, array.length - 1);
        int result = binarySearch(array);
        out.println(result);
        out.close();
    }

    public static void quickSort(Pair[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Pair temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].start >= temp.start) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].start <= temp.start) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort(arr, low, left - 1);
        quickSort(arr, left + 1, high);

    }

    public static int binarySearch(Pair[] ary) {
        int result = 0;
        int minItvl = 0;
        int maxItvl = maxInterval + 1;
        int counter;
        int currentItvl = (minItvl + maxItvl) >> 1;
        int timeStamp;
        while (currentItvl > 0) {
            timeStamp = 1;
            counter = 0;
            Arrays.fill(count, 0);
            while (!q.isEmpty()) {
                q.popMin();
            }
            int ind = 0;
            while (timeStamp <= maxStamp) {
                for (; ind < ary.length; ind++) {
                    if (ary[ind].start == timeStamp) {
                        q.insertion(ary[ind]);
                    } else {
                        break;
                    }
                }
                if (q.isEmpty()) {
                    timeStamp++;
                    continue;
                } else {
                    Pair tmp = q.peek();
                    if (tmp.end < timeStamp) {
                        break;
                    } else if (tmp.end == timeStamp) {
                        count[tmp.index]++;
                        if (count[tmp.index] != currentItvl) {
                            break;
                        } else {
                            counter++;
                            q.popMin();
                        }
                    } else {
                        count[tmp.index]++;
                        if (count[tmp.index] == currentItvl) {
                            counter++;
                            q.popMin();
                        }
                    }
                }
                timeStamp++;
            }
            if (counter == N) {
                minItvl = currentItvl;
                if (result == currentItvl) {
                    break;
                }
                result = currentItvl;
                currentItvl = (currentItvl + maxItvl) >> 1;
            } else {
                maxItvl = currentItvl;
                currentItvl = (currentItvl + minItvl) >> 1;
            }
        }
        return result;
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
        int index;

        public Pair(int start, int end, int index) {
            this.start = start;
            this.end = end;
            this.index = index;
        }
    }

    static class priorQueue {
        private final int MAX = 5009;
        Pair[] priQ;
        int ind = 0;

        public priorQueue() {
            priQ = new Pair[MAX];
            for (int i = 1; i < MAX; i++) {
                priQ[i] = new Pair(0, 0, 0);
            }
        }

        public Pair popMin() {
            Pair temp = priQ[1];
            priQ[1] = priQ[ind];
            priQ[ind] = new Pair(0, 0, 0);
            ind--;
            downAdjustment();
            return temp;
        }

        public Pair peek() {
            return priQ[1];
        }

        public void insertion(Pair key) {
            priQ[++ind] = key;
            upAdjustment(ind);
        }

        public void upAdjustment(int toBeAdjust) {
            int c = toBeAdjust;
            int parent = c / 2;
            Pair temp = priQ[c];
            while (c > 1 && temp.end < priQ[parent].end) {
                if (temp.end >= priQ[parent].end) {
                    break;
                } else {
                    Pair tmp = priQ[c];
                    priQ[c] = priQ[parent];
                    priQ[parent] = tmp;
                    c = parent;
                    parent /= 2;
                }
            }
        }

        public void downAdjustment() {
            int c = 1;
            Pair temp = priQ[c];
            int child = 2 * c;
            while (child <= ind) {
                if (child + 1 <= ind && priQ[child].end > priQ[child + 1].end) {
                    child++;
                }
                if (temp.end <= priQ[child].end) {
                    break;
                }
                Pair tmp = priQ[c];
                priQ[c] = priQ[child];
                priQ[child] = tmp;
                c = child;
                child *= 2;
            }
        }

        public boolean isEmpty() {
            return ind == 0;
        }
    }
}
