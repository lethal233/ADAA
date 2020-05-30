package lab6;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {
    public static long dis = 0L;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        int m = in.nextInt();
        int u, v;
        long w;
        Graph g = new Graph(n);
        for (int i = 0; i < m; i++) {
            u = in.nextInt();
            v = in.nextInt();
            w = in.nextLong();
            g.createEdge(u, v, w);
        }
        for (int i = 1; i <= n; i++) {
            g.vertices[i].a = in.nextInt();
            g.vertices[i].b = in.nextInt();
        }
        g.dijkstraMore();
        out.println(g.vertices[n].distance);
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
        int v;
        Vertex[] vertices;
        priorQueue q;

        public Graph(int v) {
            this.v = v + 1;
            vertices = new Vertex[this.v];
            for (int i = 1; i < this.v; i++) {
                vertices[i] = new Vertex(0, 0, i);
            }
            q = new priorQueue();
        }


        public void createEdge(int i, int j, long w) {
            vertices[i].adj.add(vertices[j]);
            vertices[i].adjEdge.add(w);
        }

        public void dijkstraMore() {
            vertices[1].distance = 0L;
            vertices[1].isVisited = true;
            for (int i = 1; i < v; i++) {
                q.insertion(vertices[i]);
            }
            while (!q.isEmpty()) {
                Vertex tmp = q.popMin();
                for (int i = 0; i < tmp.adj.size(); i++) {
                    if (!tmp.adj.get(i).isVisited) {
                        int ind = relax(tmp.key, tmp.adj.get(i).key, tmp.adjEdge.get(i));
                        if (ind != -1) {
                            vertices[tmp.adj.get(i).key].distance=dis;
                            q.upAdjustment(ind);
                        }
                    }
                }
                tmp.isVisited = true;
            }
        }

        public int relax(int u, int v, long w) {
            long tmp = vertices[u].distance + w;
            long coef = tmp / (vertices[v].a + vertices[v].b);
            long low = (coef + 1) * vertices[v].a + coef * vertices[v].b;
            long high = low + vertices[v].b;
            if (tmp >= low && tmp < high) {
                if (vertices[v].distance > vertices[u].distance + w) {
                    dis = vertices[u].distance + w;
                    return vertices[v].index;
                } else {
                    return -1;
                }
            } else {
                if (vertices[v].distance > low - tmp + w + vertices[u].distance) {
                    dis = low - tmp + w + vertices[u].distance;
                    return vertices[v].index;
                } else {
                    return -1;
                }
            }

        }
    }

    static class Vertex {
        int a;
        int b;
        int key;
        boolean isVisited;
        long distance;
        int index;
        ArrayList<Vertex> adj;
        ArrayList<Long> adjEdge;

        public Vertex(int a, int b, int ind) {
            this.a = a;
            this.b = b;
            adj = new ArrayList<>();
            adjEdge = new ArrayList<>();
            isVisited = false;
            key = ind;
            index = ind;
            distance = Long.MAX_VALUE;
        }
    }

    static class priorQueue {
        private final int MAX = 10009;
        Vertex[] priQ;
        int ind = 0;

        public priorQueue() {
            priQ = new Vertex[MAX];
            for (int i = 1; i < MAX; i++) {
                priQ[i] = new Vertex(0, 0, 0);
            }
        }

        public Vertex popMin() {
            Vertex temp = priQ[1];
            priQ[1] = priQ[ind];
            priQ[1].index = 1;
            priQ[ind] = new Vertex(0, 0, 0);
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
