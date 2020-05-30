import java.util.Scanner;

public class GS {
    public static void main(String[] args) {
        int number;
        Scanner input = new Scanner(System.in);
        number = input.nextInt();
        solution s = new solution(number);
        for (int i = 1; i <= number; i++) {
            int[] tmp = new int[number];
            for (int j = 0; j < number; j++) {
                tmp[j] = input.nextInt();
            }
            s.addMan(i, tmp);
        }
        for (int i = 1; i <= number; i++) {
            int[] tmp = new int[number];
            for (int j = 0; j < number; j++) {
                tmp[j] = input.nextInt();
            }
            s.addWoman(i, tmp);
        }
        s.solve();
        for (int i = 1; i <= number; i++) {
            System.out.print(s.husband[i] + " ");
        }
        System.out.println();
        for (int i = 1; i <= number; i++) {
            System.out.print(s.wife[i] + " ");
        }

    }

    static class Man {
        int id;
        int count;
        int[] preferList;
        boolean[] proposed;

        public Man(int id, int n) {
            this.id = id;
            preferList = new int[n + 1];
            count = 0;
            proposed = new boolean[n + 1];
        }
    }

    static class Woman {
        int id;
        int[] preferList;
        int[] inverseList;

        public Woman(int id, int n) {
            this.id = id;
            preferList = new int[n + 1];
            inverseList = new int[n + 1];
        }
    }

    static class solution {
        Man[] men;
        Woman[] women;
        QueueArray freeMan;
        int[] wife;
        int[] husband;
        int total;

        public solution(int number) {
            men = new Man[number + 1];
            women = new Woman[number + 1];
            freeMan = new QueueArray(number);
            for (int i = 1; i < number + 1; i++) {
                men[i] = new Man(i, number);
                women[i] = new Woman(i, number);
            }
            wife = new int[number + 1];
            husband = new int[number + 1];
            total = number;
        }

        void addMan(int i, int... list) {

            for (int k = 0; k < total; k++) {
                men[i].preferList[k + 1] = list[k];
            }

        }

        void addWoman(int i, int... list) {
            for (int k = 0; k < total; k++) {
                women[i].preferList[k + 1] = list[k];
                women[i].inverseList[list[k]] = k + 1;
            }
        }

        void init() {
            for (int i = 1; i <= total; i++) {
                freeMan.enQueue(men[i]);
            }
        }

        void solve() {
            init();
            while (!freeMan.isEmpty() && freeMan.top().count != total) {
                Man tmp = freeMan.top();
                int i = 1;
                for (; i <= total; i++) {
                    if (!tmp.proposed[i]) {
                        break;
                    }
                }
                if (husband[tmp.preferList[i]] == 0) {
                    wife[tmp.id] = tmp.preferList[i];
                    husband[tmp.preferList[i]] = tmp.id;
                } else if (women[tmp.preferList[i]].inverseList[tmp.id] < women[tmp.preferList[i]].inverseList[husband[tmp.preferList[i]]]) {
                    int tmpid = husband[tmp.preferList[i]];
                    wife[tmp.id] = tmp.preferList[i];
                    wife[tmpid] = 0;
                    husband[tmp.preferList[i]] = tmp.id;
                    freeMan.enQueue(men[tmpid]);
                } else{
                    freeMan.enQueue(men[tmp.id]);//very important which has been without consideration
                }
                men[tmp.id].count++;
                men[tmp.id].proposed[i] = true;
                freeMan.deQueue();
                for (int j = 1; j <= total; j++) {
                    System.out.print(husband[j] + " ");
                }
                System.out.println();
                for (int j = 1; j <= total; j++) {
                    System.out.print(wife[j] + " ");
                }
                System.out.println("\n\n");
            }
        }
    }

    static class QueueArray {
        int front = 0;
        int rear = 0;
        int MAX_SIZE;
        Man[] S;

        public QueueArray(int size) {
            MAX_SIZE = size * size + 5;
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
