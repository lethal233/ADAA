package lab8;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class a1 {

    public static priorQueue q = new priorQueue();

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
            int testcase = in.nextInt();
            for (int i = 0; i < testcase; i++) {
                String a;
                int size;
                int count = 0;
                long result = 0;
                Node[] nodes = new Node[26];
                for (int j = 0; j < nodes.length; j++) {
                    nodes[j] = new Node(j, 0, 0);
                }
                a = in.next();
                size = a.length();
                for (int j = 0; j < size; j++) {
                    nodes[a.charAt(j) - 'a'].frequency++;
                }
                for (Node node : nodes) {
                    if (node.frequency != 0) {
                        q.insertion(node);
                        count++;
                    }
                }
                if (count == 1) {
                    out.println(a.length());
                    q.popMin();
                    continue;
                }
                while (!q.isEmpty()) {
                    Node tmp1 = q.popMin();
                    Node tmp2 = q.popMin();
                    int dep1 = tmp1.depth;
                    int dep2 = tmp2.depth;
                    Node father;
                    if (dep1 == dep2) {
                        father = new Node(-1, tmp1.frequency + tmp2.frequency, tmp1.depth + 1);
                        father.left = tmp1;
                        father.right = tmp2;
                    } else if (dep1 > dep2) {
                        tmp2.depth = dep1;
                        father = new Node(-1, tmp1.frequency + tmp2.frequency, tmp1.depth + 1);
                        father.left = tmp1;
                        father.right = tmp2;
                        updateDepth(father);
                    } else {
                        tmp1.depth = dep2;
                        father = new Node(-1, tmp1.frequency + tmp2.frequency, tmp1.depth + 1);
                        father.left = tmp1;
                        father.right = tmp2;
                        updateDepth(father);
                    }
                    q.insertion(father);
                }

                int allDepth = q.popMin().depth;
                for (Node node : nodes) {
                    if (node.frequency != 0) {
                        result += node.frequency * (allDepth - node.depth);
                    }
                }
                out.println(result);
            }

        }
    }

    //update the depth
    public static void updateDepth(Node r) {
        if (r.left != null) {
            r.left.depth = r.depth - 1;
            updateDepth(r.left);
        }
        if (r.right != null) {
            r.right.depth = r.depth - 1;
            updateDepth(r.right);
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

    static class Node {
        int ch;
        long frequency;
        int depth;
        Node left;
        Node right;

        public Node(int ch, long frequency, int depth) {
            this.ch = ch;
            this.frequency = frequency;
            this.depth = depth;
            left = null;
            right = null;
        }

        @Override
        public String toString() {
            return (char) (ch + (int) 'a') + " " + frequency + " ";
        }
    }

    static class priorQueue {
        private final int MAX = 10009;
        Node[] priQ;
        int ind = 0;

        public priorQueue() {
            priQ = new Node[MAX];
            for (int i = 1; i < MAX; i++) {
                priQ[i] = new Node(-1, 0, 0);
            }
        }

        public Node popMin() {
            Node temp = priQ[1];
            priQ[1] = priQ[ind];
            priQ[ind] = new Node(-1, 0, 0);
            ind--;
            downAdjustment();
            return temp;
        }

        public void insertion(Node key) {
            priQ[++ind] = key;
            upAdjustment(ind);
        }

        public void upAdjustment(int toBeAdjust) {
            int c = toBeAdjust;
            int parent = c / 2;
            Node temp = priQ[c];
            while (c > 1 && temp.frequency < priQ[parent].frequency) {
                if (temp.frequency >= priQ[parent].frequency) {
                    break;
                } else {
                    Node tmp = priQ[c];
                    priQ[c] = priQ[parent];
                    priQ[parent] = tmp;
                    c = parent;
                    parent /= 2;
                }
            }
        }

        public void downAdjustment() {
            int c = 1;
            Node temp = priQ[c];
            int child = 2 * c;
            while (child <= ind) {
                if (child + 1 <= ind && priQ[child].frequency > priQ[child + 1].frequency) {
                    child++;
                }
                if (temp.frequency <= priQ[child].frequency) {
                    break;
                }
                Node tmp = priQ[c];
                priQ[c] = priQ[child];
                priQ[child] = tmp;
                c = child;
                child *= 2;
            }
        }

        public boolean isEmpty() {
            return ind == 1;
        }
    }
}