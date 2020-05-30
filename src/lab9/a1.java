package lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class a1 {
    public static Map<Long, Student> studentMap = new HashMap<>();
    public static long re;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();

        PrintWriter out = new PrintWriter(System.out);
        int N = in.nextInt();
        Student[] students = new Student[N];
        int a;
        int b;
        long c;

        int j = 0;
        for (int i = 0; i < N; i++) {
            a = in.nextInt();
            b = in.nextInt();
            c = (long) a * 100 + (long) b;
            if (!studentMap.containsKey(c)) {
                students[j] = new Student(a, b);
                studentMap.put(c, students[j]);
                j++;
            } else {
                studentMap.get(c).cnt++;
            }
        }
        Arrays.sort(students, 0, j, new MyComparator(1));
        mergeSort(students, 0, j - 1);

        long result = 0;
        long counter = 0;
        for (int i = 0; i < j; i++) {
            result = result + counter * students[i].cnt + students[i].cnt * (students[i].cnt - 1);
            counter += students[i].cnt;
        }
        out.println(result - re);
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

    static class Student {
        int a;
        int b;
        int cnt;

        public Student(int a, int b) {
            this.a = a;
            this.b = b;
            cnt = 1;
        }
    }

    static class MyComparator implements Comparator<Student> {
        int flag;

        @Override
        public int compare(Student x, Student y) {
            if (flag == 1) {
                if (x.a > y.a) {
                    return 1;
                } else if (x.a < y.a) {
                    return -1;
                } else {
                    return Integer.compare(x.b, y.b);
                }
            } else {
                if (x.b > y.b) {
                    return 1;
                } else if (x.b < y.b) {
                    return -1;
                } else {
                    return Integer.compare(x.a, y.a);
                }
            }
        }

        public MyComparator(int flag) {
            this.flag = flag;
        }
    }


    public static void mergeSort(Student[] A, int sta, int end) {
        if (sta < end) {
            int mid = (sta + end) >> 1;
            mergeSort(A, sta, mid);
            mergeSort(A, mid + 1, end);
            merge(A, sta, mid, end);
        }
    }

    public static void merge(Student[] A, int sta, int mid, int end) {
        Student[] ary = new Student[end - sta + 1];
        int i = sta;
        int j = mid + 1;
        for (int k = 0; k < ary.length; k++) {
            if (i <= mid && (j > end || A[i].b <= A[j].b)) {
                ary[k] = A[i];
                i++;
            } else {
                ary[k] = A[j];
                re += mid + 1 - i;
                j++;
            }
        }
        for (int k = 0; k < ary.length; k++) {
            A[k + sta] = ary[k];
        }
    }
}
