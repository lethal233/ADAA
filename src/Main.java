import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static Complex[] coef;//result
    public static long[] ex_re;
    public static int[] re_ex;
    public static int countzero;
    public static int limit = 1;
    public static int length;
    public static int counter;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int P = in.nextInt();
        ex_re = new long[P];
        re_ex = new int[P];
        findPrimitiveRoot(P);
        while (limit < 2 * P) {
            limit <<= 1;
            length++;
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
        coef = FFT(limit, coef);
        System.out.println(Arrays.toString(coef));
        for (int i = 0; i < limit; i++) {

            coef[i] = coef[i].times(coef[i]);
        }
        System.out.println(Arrays.toString(coef));
        coef = IFFT(limit, coef);
//        System.out.println(Arrays.toString(coef));
        long[] S = new long[P];
        int r;
        for (int i = 0; i < coef.length; i++) {
            r = i % (P - 1);
            S[(int) ex_re[r]] = S[(int) ex_re[r]] + Math.round(coef[i].re + 0.5);
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

    public static Complex[] FFT(int n, Complex[] ary) {
        if (n == 1) {
            return ary;
        }
        Complex[] even = new Complex[n >> 1];
        Complex[] odd = new Complex[n >> 1];
        for (int i = 0; i < n; i += 2) {
            even[i >> 1] = ary[i];
            odd[i >> 1] = ary[i + 1];
        }

        Complex[] solutionEven = FFT(n >> 1, even);
        Complex[] solutionOdd = FFT(n >> 1, odd);
        Complex[] ySolution = new Complex[n];
        for (int i = 0; i < n; i++) {
            ySolution[i] = new Complex(0, 0);
        }
//        Complex wn = new Complex(Math.cos(2.0 * Math.PI / n), Math.sin(2.0 * Math.PI / n));
//        Complex w = new Complex(1, 0);
        for (int i = 0; i < (n >> 1); i++) {
            Complex wk = new Complex(Math.cos(-2.0 * Math.PI * i / n), Math.sin(-2.0 * Math.PI * i / n));
            Complex tmp = wk.times(solutionOdd[i]);
            ySolution[i] = solutionEven[i].plus(tmp);
            ySolution[i + (n >> 1)] = solutionEven[i].minus(tmp);
//            w = w.times(wn);
        }
        return ySolution;
    }

    public static Complex[] IFFT(int n, Complex[] ary) {
        if (n == 1) {
            return ary;
        }
        Complex[] even = new Complex[n >> 1];
        Complex[] odd = new Complex[n >> 1];
        for (int i = 0; i < n; i += 2) {
            even[i >> 1] = ary[i];
            odd[i >> 1] = ary[i + 1];
        }
        Complex[] e = IFFT(n >> 1, even);
        Complex[] d = IFFT(n >> 1, odd);
        Complex[] y = new Complex[n];
        for (int i = 0; i < n; i++) {
            y[i] = new Complex(0, 0);
        }
//        Complex wn = new Complex(Math.cos(2.0 * Math.PI / n), -Math.sin(2.0 * Math.PI / n));
//        Complex w = new Complex(1, 0);

        for (int i = 0; i < (n >> 1); i++) {
            Complex wk = new Complex(Math.cos(2.0 * Math.PI * i / n), Math.sin(2.0 * Math.PI * i / n));
            Complex tmp = wk.times(d[i]);
            y[i] = (e[i].plus(tmp)).divide(n);
            y[i + (n >> 1)] = (e[i].minus(tmp)).divide(n);
//            w = w.times(wn);
        }
        return y;
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
//        public Complex(Complex a) {
//            this(a.re, a.im);
//        }
        public Complex plus(Complex b) {
            return new Complex(this.re + b.re, this.im + b.im);
        }

        public Complex minus(Complex b) {
            return new Complex(this.re - b.re, this.im - b.im);
        }

        public Complex times(Complex b) {
            return new Complex(this.re * b.re - this.im * b.im, this.re * b.im + this.im * b.re);
        }

        public Complex divide(double alpha) {
            return new Complex(re / alpha, im/alpha);
        }

        @Override
        public String toString() {
            return "Complex{" +
                    "re=" + String.format("%.4f",re) +
                    ", im=" + String.format("%.4f",im) +
                    '}';
        }
    }


    public static void findPrimitiveRoot(int P) {
        int i;
        int an;
        for (i = 2; i < P; i++) {
            Arrays.fill(ex_re, 0);
            Arrays.fill(re_ex, 0);
            counter = 0;
            for (int j = 1; j < P; j++) {
                if (ex_re[j] != 0) {
                    break;
                } else {
                    an = (int) fastPower(i, j, P);
                    ex_re[j] = an;
                    re_ex[an] = j;
                    counter++;
                }
            }
            if (counter == P - 1) {
                break;
            }
        }
//        System.out.println("primitive root is " + i);
//        System.out.println("re_ex: " + Arrays.toString(re_ex));
//        System.out.println("ex_re: " + Arrays.toString(ex_re));
    }

    public static long fastPower(int a, int b, int P) {
        long ans = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = (ans * a) % P;
            }
            a *= a;
            b >>= 1;
        }
        return ans;
    }

}
