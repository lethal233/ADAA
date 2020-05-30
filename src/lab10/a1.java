package lab10;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class a1 {
    static Point[] points = new Point[500009];
    static int N;
    static Stack<Point> pointStack = new Stack<>();
    public static Point low;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        N = in.nextInt();
        for (int i = 0; i < N; i++) {
            points[i] = new Point(in.nextLong(), in.nextLong());
        }

        Arrays.sort(points, 0, N, new MyComparable());
        int now;
        pointStack.push(points[0]);
        pointStack.push(points[1]);
        for (int i = 2; i < N; i++) {
            Point h = points[i];
            Point m = pointStack.pop();
            Point t = pointStack.peek();

            now = crossProduct(t, m, h);
            if (now != -1) {
                pointStack.push(m);
                pointStack.push(h);
            } else {
                i--;
            }
        }
        Point tmp = pointStack.peek();
        int size = pointStack.size();
        for (int i = 1; i < N; i++) {
            if (!points[i].equals(tmp) && crossProduct(points[0], points[i], tmp) == 0) {
                size++;
            }
        }
        out.println(size);
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

    static class Point {
        long x;
        long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    static class MyComparable implements Comparator<Point> {
        Point low = getLowest(points);

        @Override
        public int compare(Point o1, Point o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            int theta = crossProduct(low, o1, o2);
            if (theta > 0) {
                return -1;
            } else if (theta < 0) {
                return 1;
            } else {
                long distance1 = ((low.x - o1.x) * (low.x - o1.x)) +
                        ((low.y - o1.y) * (low.y - o1.y));
                long distance2 = ((low.x - o2.x) * (low.x - o2.x)) +
                        ((low.y - o2.y) * (low.y - o2.y));
                if (distance1 < distance2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    //    public static void quickSort(Point[] arr, int low, int high) {
//        if (arr.length <= 0) {
//            return;
//        }
//        if (low >= high) {
//            return;
//        }
//        int left = low;
//        int right = high;
//        Point temp = arr[left];
//        while (left < right) {
//            while (left < right && arr[right] >= temp) {
//                right--;
//            }
//            arr[left] = arr[right];
//            while (left < right && arr[left] <= temp) {
//                left++;
//            }
//            arr[right] = arr[left];
//        }
//        arr[left] = temp;
//        quickSort(arr, low, left - 1);
//        quickSort(arr, left + 1, high);
//    }
    public static Point getLowest(Point[] points) {
        Point l = points[0];
        for (int i = 1; i < N; i++) {
            Point tmp = points[i];
            if (tmp.y < l.y || (tmp.y == l.y && tmp.x < l.x)) {
                l = tmp;
            }
        }
        return l;
    }

    public static int crossProduct(Point p1, Point p2, Point p3) {
        long cp = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        if (cp == 0) {
            return 0; // collinear
        } else if (cp > 0) {
            return 1;//counter
        } else return -1;//clockwise
    }
}
