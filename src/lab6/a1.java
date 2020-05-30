package lab6;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//取对数，将乘法改成加法
public class a1 {
    public static int N;
    public static int CONSTANT = 19260817;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        N = in.nextInt();
        int M = in.nextInt();
        int u = 0;
        int v = 0;
        long w = 0;
        Graph g = new Graph(N);
        for (int i = 0; i < M; i++) {
            u = in.nextInt();
            v = in.nextInt();
            w = in.nextLong();
            g.createEdge(u, v, w);
        }
        g.dijkstra(1);
        out.println(g.calculate());
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

    static class Graph {
        Vertex[] vertices;
        int v;
        priorQueue q;

        public Graph(int n) {
            this.v = n + 1;
            vertices = new Vertex[v];
            for (int i = 1; i < v; i++) {
                vertices[i] = new Vertex(i);
            }
            q = new priorQueue();
        }

        public void createEdge(int i, int j, long w) {
            vertices[i].adj.add(vertices[j]);
            vertices[i].adjEdge.add(w);
        }

        public void initial(int start) {
            vertices[start].distance = 0L;
            vertices[start].isVisited = true;
        }

        public void dijkstra(int start) {
            initial(start);
            for (int i = 1; i < v; i++) {
                q.insertion(vertices[i]);
            }
            while (!q.isEmpty()) {
                Vertex tmp = q.popMin();
                for (int i = 0; i < tmp.adj.size(); i++) {
                    if (!tmp.adj.get(i).isVisited) {
                        int ind = relax(tmp.key, tmp.adj.get(i).key, tmp.adjEdge.get(i));
                        if (ind != -1) {
                            vertices[tmp.adj.get(i).key].distance = tmp.distance + Math.log(tmp.adjEdge.get(i));
                            vertices[tmp.adj.get(i).key].parent = tmp;
                            q.upAdjustment(ind);
                        }
                    }
                }
                tmp.isVisited = true;
            }
        }

        public int relax(int u, int v, long w) {
            if (vertices[v].distance > vertices[u].distance + Math.log(w)) {
                return vertices[v].index;
            } else {
                return -1;
            }
        }

        public long calculate() {
            long result = 1L;
            Vertex a = vertices[v - 1];
            while (a.parent != null) {
                for (int i = 0; i < a.parent.adj.size(); i++) {
                    if (a.parent.adj.get(i) == a) {
                        result = (result * a.parent.adjEdge.get(i)) % CONSTANT;
                        break;
                    }
                }
                a = a.parent;
            }
            return result;
        }
    }

    static class Vertex {
        ArrayList<Vertex> adj;
        ArrayList<Long> adjEdge;
        int key;
        boolean isVisited;
        double distance;
        Vertex parent;
        int index;

        public Vertex(int key) {
            this.key = key;
            isVisited = false;
            distance = Long.MAX_VALUE;
            adj = new ArrayList<>();
            parent = null;
            adjEdge = new ArrayList<>();
            index = key;
        }
    }

    static class priorQueue {
        private final int MAX = 1000009;
        Vertex[] priQ;
        int ind = 0;

        public priorQueue() {
            priQ = new Vertex[MAX];
            for (int i = 1; i < MAX; i++) {
                priQ[i] = new Vertex(0);
            }
        }

        public Vertex popMin() {
            Vertex temp = priQ[1];
            priQ[1] = priQ[ind];
            priQ[1].index = 1;
            priQ[ind] = new Vertex(0);
            ind--;
            downAdjustment();
            return temp;
        }

        public void insertion(Vertex key) {
            priQ[++ind] = key;
            upAdjustment(ind);
        }

        public void upAdjustment(int toBeAdjust) {
            int c = toBeAdjust;
            int parent = c / 2;
            Vertex temp = priQ[c];
            while (c > 1 && temp.distance < priQ[parent].distance) {
                if (temp.distance >= priQ[parent].distance) {
                    break;
                } else {
                    Vertex tmp = priQ[c];
                    priQ[c] = priQ[parent];
                    priQ[parent] = tmp;
                    int tempInd = priQ[c].index;
                    priQ[c].index = priQ[parent].index;
                    priQ[parent].index = tempInd;
                    c = parent;
                    parent /= 2;
                }
            }
        }

        public void downAdjustment() {
            int c = 1;
            Vertex temp = priQ[c];
            int child = 2 * c;
            while (child <= ind) {
                if (child + 1 <= ind && priQ[child].distance > priQ[child + 1].distance) {
                    child++;
                }
                if (temp.distance <= priQ[child].distance) {
                    break;
                }
                Vertex tmp = priQ[c];
                priQ[c] = priQ[child];
                priQ[child] = tmp;
                int tempInd = priQ[c].index;
                priQ[c].index = priQ[child].index;
                priQ[child].index = tempInd;
                c = child;
                child *= 2;
            }
        }

        public boolean isEmpty() {
            return ind == 0;
        }
    }
}
