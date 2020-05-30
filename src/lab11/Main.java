package lab11;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static Complex[] coef;
    public static long[] ex_re;
    public static int[] re_ex;
    public static int countzero;
    public static int limit = 1;
    public static int length;
    public static int counter;
    public static final int max = 262144;
    public static int[] reverse = new int[max];

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int P = in.nextInt();
        //找原根
        ex_re = new long[P];
        re_ex = new int[P];
        findPrimitiveRoot(P);
        //扩大limit
        while (limit <= 2 * P) {
            limit <<= 1;
            length++;
        }
        //idea that comes from the website blog, to determine the bits array
        for (int i = 0; i < limit; i++) {
            reverse[i] = (reverse[i >> 1] >> 1 | ((i & 1) << (length - 1)));
        }
        coef = new Complex[limit];
        for (int i = 0; i < limit; i++) {
            coef[i] = new Complex();
        }
        int num;
        for (int i = 0; i < n; i++) {
            num = in.nextInt();
            if (num % P != 0) {
                coef[re_ex[num % P]].re += 1;
            } else {
                countzero++;
            }
        }
        FFT(coef, 1);
        System.out.println(Arrays.toString(coef));
        //注意这里是limit不是2P
        for (int i = 0; i < limit; i++) {
            coef[i] = coef[i].times(coef[i]);
        }
        //IFFT
        FFT(coef, -1);
        System.out.println(Arrays.toString(coef));
        long[] S = new long[P];
        int r;
        for (int i = 2; i < 2 * P - 1; i++) {
            r = i % (P - 1);
            S[(int) ex_re[r]] = S[(int) ex_re[r]] + Math.round(coef[i].re);
        }
        S[0] = 2 * n * countzero - countzero * countzero;
        for (int i = 0; i < P; i++) {
            out.println(S[i]);
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

    /*this fft algorithm comes from bilibili, belongs to hexiangwanli*/
    public static void FFT(Complex[] ySolution, int mode) {
        for (int i = 0; i < limit; i++) {
            if (i < reverse[i]) {
                Complex tmp = ySolution[i];
                ySolution[i] = ySolution[reverse[i]];
                ySolution[reverse[i]] = tmp;
            }
        }
        //start from the first level
        for (int i = 1; i <= length; i++) {
            int lim = 1 << i;//to get the level's all the number
            double theta = 2.0 * Math.PI / lim;
            Complex wn = new Complex(Math.cos(theta), mode * Math.sin(theta));
            for (int j = 0; j < limit; j += lim) {
                Complex w1 = new Complex(1, 0);
                for (int k = 0; k < (lim >> 1); k++) {
                    Complex tmp1 = w1.times(ySolution[j + k + (lim >> 1)]);
                    Complex tmp2 = ySolution[j + k];
                    ySolution[j + k] = tmp2.plus(tmp1);
                    ySolution[j + k + (lim >> 1)] = tmp2.minus(tmp1);
                    w1 = w1.times(wn);
                }
            }
        }
        //if the process is IFFT, then all the element should be divided by the limit
        if (mode == -1) {
            for (int i = 0; i < limit; i++) {
                ySolution[i].re = ySolution[i].re / limit;
            }
        }
    }

    static class Complex {
        double re;
        double im;

        public Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public Complex() {
            this(0, 0);
        }

        public Complex plus(Complex b) {
            return new Complex(this.re + b.re, this.im + b.im);
        }

        public Complex minus(Complex b) {
            return new Complex(this.re - b.re, this.im - b.im);
        }

        public Complex times(Complex b) {
            return new Complex(this.re * b.re - this.im * b.im, this.re * b.im + this.im * b.re);
        }

        @Override
        public String toString() {
            return "Complex{" +
                    "re=" + String.format("%.5f",re) +
                    ", im=" + String.format("%.5f",im) +
                    '}';
        }
    }

    public static void findPrimitiveRoot(int P) {
        int i;
        int an;
        for (i = 2; i < P; i++) {
            if (P % i == 0) {
                continue;
            }
            Arrays.fill(ex_re, 0);
            Arrays.fill(re_ex, 0);
            counter = 0;
            for (int j = 1; j < P; j++) {
                an = (int) fastPower(i, j, P);
                if (re_ex[an] != 0) {
                    break;
                } else {
                    ex_re[j] = an;
                    re_ex[an] = j;
                    counter++;
                }
            }
            if (counter == P - 1) {
                break;
            }
        }
        ex_re[0] = ex_re[P - 1];
    }

    //to get the fast power
    public static long fastPower(int a, int b, int P) {
        long ans = 1L, base = a;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = (ans % P * base % P) % P;
            }
            base = ((base % P) * (base % P)) % P;
            b >>= 1;
        }
        return ans;
    }

}
