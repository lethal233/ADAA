package lab4;
// 第一题

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class test {
    public static int n;
    public static int[][] direction = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    public static int step = 0;
    public static int[][] maps;
    public static int needSteps = 0;
    public static int counter = 0;
    public static int gccounter = 0;
//    public static nodeQueue q = new nodeQueue();


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Task solver = new Task();
        solver.solve(in, out);
        out.close();
    }

    static class Task {
        public void solve(InputReader in, PrintWriter out) {
            n = in.nextInt();
            maps = new int[n + 2][n + 2];
            for (int i = 1; i <= n; i++) {
                String a = in.next();
                char[] tmp = a.toCharArray();
                for (int j = 1; j <= n; j++) {
                    if (tmp[j - 1] == '.') {
                        maps[i][j] = 1;
                        needSteps++;
                    }
                }
            }
            long start = System.currentTimeMillis();
            DFS(1, 1, n, 1);
            long end = System.currentTimeMillis();
            out.println(counter);
            System.out.println(end-start);
        }
    }


    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public char[] nextCharArray() {
            return next().toCharArray();
        }

        //         public boolean hasNext() {
//             try {
//                 return reader.ready();
//             } catch(IOException e) {
//                 throw new RuntimeException(e);
//             }
//         }
        public boolean hasNext() {
            try {
                String string = reader.readLine();
                if (string == null) {
                    return false;
                }
                tokenizer = new StringTokenizer(string);
                return tokenizer.hasMoreTokens();
            } catch (IOException e) {
                return false;
            }
        }

        public BigInteger nextBigInteger() {
            return new BigInteger(next());
        }

        public BigDecimal nextBigDecinal() {
            return new BigDecimal(next());
        }
    }


    public static void DFS(int nowx, int nowy, int endx, int endy) {
        maps[nowx][nowy] = 0;
        step++;
        gccounter++;
        if (gccounter == 400000) {
            gccounter=0;
            System.gc();
        }
        if (nowx == endx && nowy == endy) {
            if (step == needSteps) {
                counter++;
            }
            step--;
            maps[nowx][nowy] = 1;
            return;
        }
        int[][] rec = new int[2][3];
//        int[][] ex = new int[2][3];
        int c = 0;
        int number = 0;
        for (int i = 0; i < 4; i++) {
            int nextx = nowx + direction[i][0];
            int nexty = nowy + direction[i][1];
            if (maps[nextx][nexty] == 1) {
                if (judgeEdge(nowx, nowy, nextx, nexty) == 1 && !(nextx == n && nexty == 1)) {
                    rec[0][c] = nextx;
                    rec[1][c] = nexty;
                    c++;
                }
//                ex[0][number]=nextx;
//                ex[1][number]=nexty;
                number++;
            }
        }
        if (number == 0) {
            step--;
            maps[nowx][nowy] = 1;
            return;
        }
//        if (number == 2) {
//            if (!BFS(ex[0][0], ex[1][0], ex[0][1], ex[1][1])) {
//                recover();
//                step--;
//                maps[nowx][nowy]=1;
//                return;
//            }
//        }
        if (c > 1) {
            step--;
            maps[nowx][nowy] = 1;

            return;
        }
        if (c == 1) {
            DFS(rec[0][0], rec[1][0], n, 1);
            step--;
            maps[nowx][nowy] = 1;
            return;
        }
        for (int i = 0; i < 4; i++) {
            int nextx = nowx + direction[i][0];
            int nexty = nowy + direction[i][1];
            if (nextx >= 1 && nextx <= n && nexty >= 1 && nexty <= n && maps[nextx][nexty] == 1) {
                DFS(nextx, nexty, n, 1);
            }
        }
        maps[nowx][nowy] = 1;
        step--;
    }

//    public static boolean BFS(int startx, int starty, int targetx, int targety) {
//        q.insert(new Node(startx, starty));
//        maps[startx][starty] = 2;
//        while (!q.isEmpty()) {
//            Node tmp = q.remove();
//            for (int i = 0; i < 4; i++) {
//                int x = tmp.x + direction[i][0];
//                int y = tmp.y + direction[i][1];
//                if (x == targetx && y == targety) {
//                    return true;
//                }
//                if (x >= 1 && x <= n && y >= 1 && y <= n && maps[x][y] == 1) {
//                    maps[x][y] = 2;
//                    q.insert(new Node(x, y));
//                }
//            }
//        }
//        return false;
//    }
//
//    public static void recover() {
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                if (maps[i][j] == 2) {
//                    maps[i][j] = 1;
//                }
//            }
//        }
//        q.clear();
//    }

    public static int judgeEdge(int fromx, int fromy, int checkx, int checky) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int x = checkx + direction[i][0];
            int y = checky + direction[i][1];
            if (!(x == fromx && y == fromy) && maps[x][y] == 1) {
                count++;
            }
        }
        return count;
    }

//    static class Node {
//        int x;
//        int y;
//
//        public Node(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//    }

//    static class nodeQueue {
//        private final int SIZE = 100;
//        private Node[] ndAry;
//        private int front;
//        private int rear;
//
//        public nodeQueue() {
//            ndAry = new Node[SIZE];
//            front = 0;
//            rear = 0;
//        }
//
//        public void insert(Node a) {
//            if (rear < SIZE) {
//                ndAry[rear++] = a;
//            }
//        }
//
//        public Node remove() {
//            Node a = ndAry[front];
//            front++;
//            return a;
//        }
//
//        public boolean isEmpty() {
//            return front == rear;
//        }
//
//        public void clear() {
//            front = 0;
//            rear = 0;
//        }
//    }
}