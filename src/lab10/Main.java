package lab10;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "";
        Reader in = new Reader(file);
        PrintWriter out = new PrintWriter(System.out);
        int N = in.nextInt();
        int d = in.nextInt();
        if (d == 1) {
            int[] ary1d = new int[N];
            for (int i = 0; i < N; i++) {
                ary1d[i] = in.nextInt();
            }
            quickSort1d(ary1d, 0, N - 1);
            int sh = Integer.MAX_VALUE;
            int one = 0;
            int two = 1;
            for (int i = 1; i < N; i++) {
                if (ary1d[i] - ary1d[i - 1] < sh) {
                    one = i - 1;
                    two = i;
                    sh = ary1d[i] - ary1d[i - 1];
                }
            }
            out.println(ary1d[one]);
            out.println(ary1d[two]);
        } else if (d == 2) {
            Point2D[] ary2d = new Point2D[N];
            for (int i = 0; i < N; i++) {
                ary2d[i] = new Point2D(in.nextLong(), in.nextLong());
            }
            Pair2D p = closestPair2d(ary2d);
            out.println(p.p1.x + " " + p.p1.y);
            out.println(p.p2.x + " " + p.p2.y);
        } else {
            Point3D[] ary3d = new Point3D[N];
            for (int i = 0; i < N; i++) {
                ary3d[i] = new Point3D(in.nextLong(), in.nextLong(), in.nextLong());
            }
            Pair3D p = closestPair3d(ary3d);
            out.println(p.p1.x + " " + p.p1.y + " " + p.p1.z);
            out.println(p.p2.x + " " + p.p2.y + " " + p.p2.z);
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

    static class Point2D {
        long x;
        long y;

        public Point2D(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Pair2D {
        Point2D p1;
        Point2D p2;

        public Pair2D(Point2D p1, Point2D p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    static class Point3D {
        long x;
        long y;
        long z;

        public Point3D(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static class Pair3D {
        Point3D p1;
        Point3D p2;

        public Pair3D(Point3D p1, Point3D p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    static class Area1 {
        Point3D p;
        List<Area2> record;
        List<Integer> location;

        public Area1(Point3D p) {
            this.p = p;
            record = new ArrayList<>();
            location = new ArrayList<>();
        }
    }

    static class Area2 {
        List<Point3D> list;

        public Area2() {
            list = new ArrayList<>();
        }
    }

    public static void quickSort1d(int[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        int temp = arr[left];
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
        quickSort1d(arr, low, left - 1);
        quickSort1d(arr, left + 1, high);
    }

    public static void quickSort2dx(Point2D[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Point2D temp = arr[left];
        while (left < right) {
            while (left < right && (arr[right].x > temp.x || (arr[right].x == temp.x && arr[right].y >= temp.y))) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && (arr[left].x < temp.x || (arr[left].x == temp.x && arr[left].y <= temp.y))) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort2dx(arr, low, left - 1);
        quickSort2dx(arr, left + 1, high);
    }

    public static void quickSort2dy(Point2D[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Point2D temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].y >= temp.y) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].y <= temp.y) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort2dy(arr, low, left - 1);
        quickSort2dy(arr, left + 1, high);
    }

    public static double distance2d(Point2D a, Point2D b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static Pair2D closestPair2d(Point2D[] arr) {
        quickSort2dx(arr, 0, arr.length - 1);
        Point2D[] arrx = Arrays.copyOf(arr, arr.length);
        quickSort2dy(arr, 0, arr.length - 1);
        Point2D[] arry = Arrays.copyOf(arr, arr.length);
        return closet2(arrx, arry, 0, arr.length - 1);
    }

    //idea from website using index of left and right
    public static Pair2D closet2(Point2D[] arrx, Point2D[] arry, int left, int right) {
        if (right - left == 1) {
            return new Pair2D(arrx[left], arrx[right]);
        }
        if (right - left == 2) {
            double d1 = distance2d(arrx[left], arrx[left + 1]);
            double d2 = distance2d(arrx[left], arrx[right]);
            double d3 = distance2d(arrx[left + 1], arrx[right]);
            if (d1 <= d2) {
                if (d1 <= d3) {
                    return new Pair2D(arrx[left], arrx[left + 1]);
                } else {
                    return new Pair2D(arrx[left + 1], arrx[right]);
                }
            } else {
                if (d2 <= d3) {
                    return new Pair2D(arrx[left], arrx[right]);
                } else {
                    return new Pair2D(arrx[left + 1], arrx[right]);
                }
            }
        }

        int mid = (left + right) / 2;
        Point2D midval = arrx[mid];
        Point2D[] arryLeft = new Point2D[mid - left + 1];
        Point2D[] arryRight = new Point2D[right - mid];
        int l = 0, r = 0;
        for (int i = 0; i < right - left + 1; i++) {
            if (arry[i].x < midval.x || (arry[i].x == midval.x && arry[i].y <= midval.y)) {
                arryLeft[l++] = arry[i];
            } else {
                arryRight[r++] = arry[i];
            }
        }
        Pair2D lp = closet2(arrx, arryLeft, left, mid);
        Pair2D rp = closet2(arrx, arryRight, mid + 1, right);
        double lpd = distance2d(lp.p1, lp.p2);
        double rpd = distance2d(rp.p1, rp.p2);
        double delta = Math.min(lpd, rpd);
        double dmin = delta;

        List<Point2D> Sy = new ArrayList<>();
        for (Point2D point2D : arry) {
            if (Math.abs(point2D.x - midval.x) <= delta) {
                Sy.add(point2D);
            }
        }

        Pair2D m = null;
        for (int i = 0; i < Sy.size(); i++) {
            if (Sy.get(i).x < midval.x || (Sy.get(i).x == midval.x && Sy.get(i).y <= midval.y)) {
                for (Point2D p2 : Sy) {
                    if ((p2.x > midval.x || (p2.x == midval.x && p2.y > midval.y)) && Math.abs(p2.y - Sy.get(i).y) <= delta) {
                        double tmpdis = distance2d(Sy.get(i), p2);
                        if (dmin > tmpdis) {
                            dmin = tmpdis;
                            if (Sy.get(i).x < p2.x) {
                                m = new Pair2D(Sy.get(i), p2);
                            } else if (Sy.get(i).x == p2.x) {
                                if (Sy.get(i).y <= p2.y) {
                                    m = new Pair2D(Sy.get(i), p2);
                                } else {
                                    m = new Pair2D(p2, Sy.get(i));
                                }
                            } else {
                                m = new Pair2D(p2, Sy.get(i));
                            }
                        }
                    }
                }
            }
        }
        if (m != null) {
            return m;
        } else if (lpd < rpd) {
            return lp;
        } else {
            return rp;
        }

    }

    public static void quickSort3dx(Point3D[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Point3D temp = arr[left];
        while (left < right) {
            while (left < right && (arr[right].x > temp.x || (arr[right].x == temp.x && arr[right].z >= temp.z))) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && (arr[left].x < temp.x || (arr[left].x == temp.x && arr[left].z <= temp.z))) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort3dx(arr, low, left - 1);
        quickSort3dx(arr, left + 1, high);
    }

    public static void quickSort3dz(Point3D[] arr, int low, int high) {
        if (arr.length <= 0) {
            return;
        }
        if (low >= high) {
            return;
        }
        int left = low;
        int right = high;
        Point3D temp = arr[left];
        while (left < right) {
            while (left < right && arr[right].z >= temp.z) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left].z <= temp.z) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = temp;
        quickSort3dz(arr, low, left - 1);
        quickSort3dz(arr, left + 1, high);
    }

    public static double distance3d(Point3D a, Point3D b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
    }

    public static Pair3D cls3d(Point3D[] arrx, Point3D[] arrz, int left, int right) {
        if (right - left == 1) {
            return new Pair3D(arrx[left], arrx[right]);
        }
        if (right - left == 2) {
            double d1 = distance3d(arrx[left], arrx[left + 1]);
            double d2 = distance3d(arrx[left], arrx[right]);
            double d3 = distance3d(arrx[left + 1], arrx[right]);
            if (d1 <= d2) {
                if (d1 <= d3) {
                    return new Pair3D(arrx[left], arrx[left + 1]);
                } else {
                    return new Pair3D(arrx[left + 1], arrx[right]);
                }
            } else {
                if (d2 <= d3) {
                    return new Pair3D(arrx[left], arrx[right]);
                } else {
                    return new Pair3D(arrx[left + 1], arrx[right]);
                }
            }
        }
        int mid = (left + right) / 2;
        Point3D midval = arrx[mid];
        Point3D[] arrzleft = new Point3D[mid - left + 1];
        Point3D[] arrzright = new Point3D[right - mid];
        int l = 0, r = 0;
        for (Point3D point3D : arrz) {
            if (point3D.x < midval.x || (point3D.x == midval.x && point3D.z <= midval.z)) {
                arrzleft[l++] = point3D;
            } else {
                arrzright[r++] = point3D;
            }
        }
        Pair3D lp = cls3d(arrx, arrzleft, left, mid);
        Pair3D rp = cls3d(arrx, arrzright, mid + 1, right);
        double lpd = distance3d(lp.p1, lp.p2);
        double rpd = distance3d(rp.p1, rp.p2);
        double delta = Math.min(lpd, rpd);
        double dmin = delta;

        long ymin = arrz[0].y;
        long ymax = arrz[0].y;
        for (int i = 1; i < arrz.length; i++) {
            if (arrz[i].y < ymin) {
                ymin = arrz[i].y;
            }
            if (arrz[i].y > ymax) {
                ymax = arrz[i].y;
            }
        }
        int number = (int) ((ymax - ymin) / 2 / delta) + 1;
        Area1[] S1 = new Area1[arrz.length];
        Area2[] S2 = new Area2[number];
        for (int i = 0; i < S2.length; i++) {
            S2[i] = new Area2();
        }
        int bound = constructS1andS2(arrz, midval.x, S1, S2, ymin, delta);
        Pair3D m = null;
        for (int i = 0; i < bound; i++) {
            for (int j = 0; j < S1[i].location.size(); j++) {
                if (S1[i].record.get(j).list.size() != 0) {
                    for (int k = 0; k < S1[i].record.get(j).list.size(); k++) {
                        double tmpdis = distance3d(S1[i].p, S1[i].record.get(j).list.get(k));
                        if (Math.abs(S1[i].p.z - S1[i].record.get(j).list.get(k).z) <= delta && tmpdis < dmin) {
                            dmin = tmpdis;
                            if (S1[i].p.x < S1[i].record.get(j).list.get(k).x) {
                                m = new Pair3D(S1[i].p, S1[i].record.get(j).list.get(k));
                            } else if (S1[i].p.x == S1[i].record.get(j).list.get(k).x) {
                                if (S1[i].p.y < S1[i].record.get(j).list.get(k).y) {
                                    m = new Pair3D(S1[i].p, S1[i].record.get(j).list.get(k));
                                } else if (S1[i].p.y == S1[i].record.get(j).list.get(k).y) {
                                    if (S1[i].p.z <= S1[i].record.get(j).list.get(k).z) {
                                        m = new Pair3D(S1[i].p, S1[i].record.get(j).list.get(k));
                                    } else {
                                        m = new Pair3D(S1[i].record.get(j).list.get(k), S1[i].p);
                                    }
                                } else {
                                    m = new Pair3D(S1[i].record.get(j).list.get(k), S1[i].p);
                                }
                            } else {
                                m = new Pair3D(S1[i].record.get(j).list.get(k), S1[i].p);
                            }
                        }
                    }
                }
            }
        }
        if (m != null) {
            return m;
        } else if (lpd < rpd) {
            return lp;
        } else {
            return rp;
        }
    }

    public static Pair3D closestPair3d(Point3D[] arr) {
        quickSort3dx(arr, 0, arr.length - 1);
        Point3D[] arrx = Arrays.copyOf(arr, arr.length);
        quickSort3dz(arr, 0, arr.length - 1);
        Point3D[] arrz = Arrays.copyOf(arr, arr.length);
        return cls3d(arrx, arrz, 0, arr.length - 1);
    }

    public static int constructS1andS2(Point3D[] arrz, long xstar, Area1[] S1, Area2[] S2, long ymin, double delta) {
        int i = 0;
        for (Point3D point3D : arrz) {
            if (point3D.x <= xstar && Math.abs(point3D.x - xstar) <= delta) {
                long tmp = point3D.y - ymin;
                if (tmp < delta) {
                    S1[i] = new Area1(point3D);
                    S1[i].record.add(S2[0]);
                    S1[i].location.add(S2[0].list.size() - 1);//-1 是临界条件
                } else {
                    int k = (int) (((double) tmp - delta) / delta / 2);
                    S1[i] = new Area1(point3D);
                    S1[i].record.add(S2[k]);
                    S1[i].location.add(S2[k].list.size() - 1);
                    if (k != S2.length - 1) {
                        S1[i].record.add(S2[k + 1]);
                        S1[i].location.add(S2[k + 1].list.size() - 1);
                    }
                }
                i++;
            }
            if (point3D.x > xstar && Math.abs(point3D.x - xstar) <= delta) {
                int n = (int) ((point3D.y - ymin) / 2 / delta);
                S2[n].list.add(point3D);
            }
        }
        return i;
    }
}



