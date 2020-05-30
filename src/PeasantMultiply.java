public class PeasantMultiply {
    public static void main(String[] args) {
        System.out.println(result(10,9));
    }


    public static long result(int a, int b) {
        long prod = 0;
        while (a > 0) {
            if (a % 2 != 0) {
                prod+=b;
            }
            a/=2;
            b*=2;
        }
        return prod;
    }
//除以2是很快的，类似于“快速幂” a*b 只用把b加loga次
}
