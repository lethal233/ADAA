package lab2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import java.io.File;
import java.lang.reflect.Method;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RunningTimeSurvey {
    //             task name            function name             run times upper
    static String[][] taskList = {
            {"LinearTimeTest", "linearTime", "10000000"},
            {"LinearTimeTest", "linearTimeCollections", "10000000"},
            {"NlognTimeTest", "NlognTime", "1000000"},
            {"QuadraticTimeTest", "QuadraticTime", "100000"},
            {"CubicTimeTest", "CubicTime", "1000"},
            {"ExponentialTimeTest", "ExponentialTime", "10"},
            {"FactorialTimeTest", "FactorialTime", "10"}

    };

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        try {
            File xlsFile = new File("RunningTimeSurvey.xls");
            // Create a workbook
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(xlsFile);

            // Create a worksheet
            WritableSheet sheet = workbook.createSheet("RunningTime", 0);
            // the first row
            for (int j = 1, n = 1; j <= 8; j++) {
                n = 10 * n;
                sheet.addCell(new Label(j + 1, 0, "n = " + n));
            }
            for (int i = 0; i < taskList.length; i++) {
                String[] taskInfo = taskList[i];

                Class<?> me = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
                Method method = me.getMethod(taskInfo[1], int.class);
                int upper = Integer.parseInt(taskInfo[2]);
                sheet.addCell(new Label(0, i + 1, taskInfo[0]));
                sheet.addCell(new Label(1, i + 1, taskInfo[1]));

                for (int j = 1, n = 1; Math.pow(10, j) <= upper; j++) {
                    n = 10 * n;
                    Long time = (Long) method.invoke(null, n);
                    // 向工作表中添加数据
                    sheet.addCell(new Label(j + 1, i + 1, time.toString()));
                }

            }
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static long linearTimeCollections(int n) {
        ArrayList<Long> arrayList = new ArrayList<Long>(n);
        generateArrayList(n, arrayList);
        long timeStart = System.currentTimeMillis();
        getMax(n, arrayList);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static long linearTime(int n) {
        long[] list = new long[n];
        generateList(n, list);
        long timeStart = System.currentTimeMillis();
        getMax(n, list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static long QuadraticTime(int n) {
        long[] list = new long[n];
        generateList(n, list);
        long timeStart = System.currentTimeMillis();
        InsertionSort(list);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static long CubicTime(int n) {
        long[][] matrix1 = new long[n][n];
        long[][] matrix2 = new long[n][n];
        long[][] result = new long[n][n];
        generateMatrix(n, matrix1);
        generateMatrix(n, matrix2);
        long timeStart = System.currentTimeMillis();
        matrixMultiplication(n, matrix1, matrix2, result);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static void generateMatrix(int n, long[][] matrix) {
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = k;
                k++;
            }
        }
        //shuffle
        Random rnd1 = new Random();
        Random rnd2 = new Random();
        for (int i = n; i > 1; i--) {
            for (int j = n; j > 1; j--) {
                int w = rnd1.nextInt(i);
                int s = rnd2.nextInt(j);
                long tmp = matrix[w][s];
                matrix[w][s] = matrix[i - 1][j - 1];
                matrix[i - 1][j - 1] = tmp;
            }
        }
    }

    public static long[][] matrixMultiplication(long n, long[][] a, long[][] b, long[][] c) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] = c[i][j] + a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    public static long getMax(long n, long[] list) {
        long max = list[0];
        for (int i = 1; i < n; i++) {
            if (list[i] > max) {
                max = list[i];
            }
        }
        return max;
    }

    public static void generateList(int n, long[] list) {
        for (int i = 0; i < n; i++) {
            list[i] = i;
        }
        // shuffle
        Random rnd = new Random();
        for (int i = list.length; i > 1; i--) {
            int j = rnd.nextInt(i);
            long temp = list[j];
            list[j] = list[i - 1];
            list[i - 1] = temp;
        }
    }

    public static void generateArrayList(int n, ArrayList<Long> arrayList) {
        for (long i = 0; i < n; i++) {
            arrayList.add(i);
        }
        // shuffle
        Collections.shuffle(arrayList);
    }

    public static long getMax(long n, ArrayList<Long> arrayList) {
        long max = arrayList.get(0);
        for (int i = 1; i < n; i++) {
            if (arrayList.get(i) > max) {
                max = arrayList.get(i);
            }
        }
        return max;
    }

    public static long NlognTime(int n) {
        //TODO:generate you test input data here
        long[] list = new long[n];
        generateList(n, list);
        long timeStart = System.currentTimeMillis();
        //TODO: write a algorithm
        MergeSort(list, 0, n - 1);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static long FactorialTime(int n) {
        // to generate you test input data
        long timeStart = System.currentTimeMillis();
        // to write a algorithm
        Factorial(n);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    public static long Factorial(int n) {
        if (n == 1)
            return 1;
        else {
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += Factorial(n - 1);
            }
            return sum;
        }

    }

    public static long ExponentialTime(int n) {
        long timeStart = System.currentTimeMillis();
        fibonacci(n);
        long timeEnd = System.currentTimeMillis();
        long timeCost = timeEnd - timeStart;
        return timeCost;
    }

    //O(nlogn)
    public static long[] MergeSort(long[] A, int sta, int end) {
        if (sta < end) {
            int mid = (sta + end) / 2;
            MergeSort(A, sta, mid);
            MergeSort(A, mid + 1, end);
            Merge(A, sta, mid, end);
        }
        return A;
    }

    public static void Merge(long[] A, int sta, int mid, int end) {
        long[] ary = new long[end - sta + 1];
        int i = sta;
        int j = mid + 1;
        for (int k = 0; k < ary.length; k++) {
            if (i <= mid && (j > end || A[i] <= A[j])) {
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

    //O(n^2)
    public static long[] InsertionSort(long[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j >= 1; j--) {
                if (a[j - 1] > a[j]) {
                    long temp = a[j - 1];
                    a[j - 1] = a[j];
                    a[j] = temp;
                }
            }
        }
        return a;
    }

    //O(2^n)
    public static long fibonacci(int n) {
        if (n < 3) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
