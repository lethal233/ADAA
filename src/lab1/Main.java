package lab1;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class Main {
    static int number;

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
            number = in.nextInt();//数量
            Man[] men = new Man[number];//男生的数组
            Map<String, Man> manMap = new LinkedHashMap<>();//男生名字到男生对象的映射
            Map<String, Woman> womanMap = new LinkedHashMap<>();//女生名字到女生对象的映射
            QueueArray freeMan = new QueueArray();//单身队列

            for (int i = 0; i < number; i++) {
                String tmp = in.next();
                men[i] = new Man(tmp);
                manMap.put(tmp, men[i]);
                freeMan.enQueue(men[i]);
                for (int j = 0; j < number; j++) {
                    String tmpW = in.next();
                    men[i].preList.add(tmpW);//注意，这里不能创建女生对象，不然会创建number*number个女生对象
                }
            }
            for (int i = 0; i < number; i++) {
                String tmp = in.next();
                Woman tmpWoman = new Woman(tmp);
                womanMap.put(tmp, tmpWoman);
                for (int j = 0; j < number; j++) {
                    String tmpM = in.next();
                    womanMap.get(tmp).preInverse.put(manMap.get(tmpM), j);
                }
            }
            while (!freeMan.isEmpty() && freeMan.top().count < number) {
                Man tmp = freeMan.top();
                Woman tmpWife = womanMap.get(tmp.preList.get(tmp.count));
                if (tmpWife.husband == null) {
                    tmp.wife = tmpWife;
                    tmpWife.husband = tmp;
                } else if (tmpWife.preInverse.get(tmpWife.husband) > tmpWife.preInverse.get(tmp)) {
                    tmpWife.husband.wife = null;
                    freeMan.enQueue(tmpWife.husband);
                    tmp.wife = tmpWife;
                    tmpWife.husband = tmp;
                } else {
                    freeMan.enQueue(tmp);
                }
                tmp.count++;
                freeMan.deQueue();
            }
            for (int i = 0; i < number; i++) {
                if (men[i].wife != null) {
                    out.println(men[i].name + " " + men[i].wife.name);
                } else {
                    out.println("impossible");
                }
            }
        }
    }


    static class Man {
        String name;
        List<String> preList;
        int count;
        Woman wife;

        public Man(String name) {
            this.name = name;
            count = 0;
            preList = new ArrayList<>();
        }
    }

    static class Woman {
        String name;
        Map<Man, Integer> preInverse;
        Man husband;

        public Woman(String name) {
            this.name = name;
            preInverse = new LinkedHashMap<>();
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

    static class QueueArray {
        int front = 0;
        int rear = 0;
        int MAX_SIZE;
        Man[] S;

        public QueueArray() {
            MAX_SIZE = number * number + 5;
            S = new Man[MAX_SIZE];
        }

        public void enQueue(Man a) {
            if (rear < MAX_SIZE) {
                S[rear] = a;
                rear++;
            }
        }

        public void deQueue() {
            if (rear > front) {
                front++;
            }
        }

        public Man top() {
            return S[front];
        }

        public boolean isEmpty() {
            return front == rear;
        }
    }
}