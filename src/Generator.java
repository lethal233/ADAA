import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;


public class Generator {
    public static void main(String[] args) throws FileNotFoundException {

        PrintStream ps = new PrintStream("lab12.txt");
        System.setOut(ps);
        Random r = new Random();
        int m = 1000001;
//        int N = 100000;
////        int d = 1;
//        System.out.println(N + " " + 97);
//        for (int i = 0; i < N; i++) {
//            System.out.print(r.nextInt(m) + " ");
//        }
//        System.out.println(30);
//        for (int i = 0; i < 90; i++) {
//            System.out.println("1 1 1");
//        }
        System.out.println(1000000+" "+1000000);
        for (int i = 0; i < 1000000; i++) {
            System.out.print(r.nextInt(m)+" ");
        }
//        System.out.println(N + " " + d);
//        for (int i = 0; i < N; i++) {
////            System.out.println((int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max) + " " + (int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max) + " " + (int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max));
////            System.out.println((int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max) + " " + (int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max));
//            System.out.println((int) Math.pow(-1, r.nextInt(2)) * r.nextInt(max));
////            System.out.println((int)Math.pow(-1,r.nextInt(2)));
//        }
    }
}