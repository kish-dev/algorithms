package com.company;

import java.util.*;

class Graph {
    private int vertexes;
    //HashSet, потому что добавление за константу,
    //множество остается уникальным, что нужно для meta-graph
    private final HashSet<Integer>[] adj;

    Graph(int v) {
        vertexes = v;
        adj = new HashSet[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new HashSet();
    }

    void addEdge(int root, int outGoing) {
        adj[root].add(outGoing);
    }

    Graph reverse() {
        Graph graphReverse = new Graph(vertexes);

        for (int i = 0; i < vertexes; ++i) {
            Iterator<Integer> j = this.adj[i].iterator();
            while (j.hasNext()) {
                int root = j.next();
                graphReverse.adj[root].add(i);
            }
        }
        return graphReverse;
    }

    void reverseVisit(LinkedList<Integer> list, int v, boolean[] visited) {
        visited[v] = true;

        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n]) {
                reverseVisit(list, n, visited);
            }
        }
        list.addFirst(v);
    }

    void DFSReverse(LinkedList<Integer> list) {
        boolean visited[] = new boolean[vertexes];

        for (int i = 0; i < vertexes; ++i) {
            if (!visited[i]) {
                reverseVisit(list, i, visited);
            }
        }
    }

    void preVisitGraph(int v, int[] numSCC, int curSCC) {
        numSCC[v] = curSCC;
    }

    void visit(int v, int[] numSCC, int curSCC, boolean[] visited) {
        visited[v] = true;
        preVisitGraph(v, numSCC, curSCC);

        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n]) {
                visit(n, numSCC, curSCC, visited);
            }
        }
    }

    void DFS(LinkedList<Integer> list, int[] numSCC) {
        int curSCC = 0;
        boolean visited[] = new boolean[vertexes];
        Iterator<Integer> i = list.listIterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n]) {
                visit(n, numSCC, curSCC++, visited);
            }
        }
    }

    Graph getMetaGraph(int[] numSCC, int countSCC) {
        Graph metaGraph = new Graph(countSCC);
        for (int i = 0; i < adj.length; ++i) {
            Iterator<Integer> j = adj[i].iterator();
            int root = i;
            while (j.hasNext()) {
                int vertex = j.next();
                if (numSCC[root] != numSCC[vertex]) {
                    //добавляем за константу и возвращаем true, если нет элемента, если элемент есть,
                    //за константу возвращает false
                    metaGraph.adj[numSCC[root]].add(numSCC[vertex]);

                }
            }
        }
        return metaGraph;
    }

    static void printSCC(int[] numSCC, LinkedList<Integer>[] arrayOfListsNumSCC) {
        for (int i = 0; i < arrayOfListsNumSCC.length; ++i) {
            Iterator<Integer> j = arrayOfListsNumSCC[i].listIterator();
            System.out.print("SCC " + i + ": ");
            while (j.hasNext()) {
                int vertex = j.next();
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }

    void print() {
        for (int i = 0; i < this.adj.length; ++i) {
            Iterator<Integer> j = this.adj[i].iterator();
            System.out.print(i + "   ");
            while (j.hasNext()) {
                int vertex = j.next();
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Graph graph = new Graph(n);
        Graph graphReverse = new Graph(n);
        for (int i = 0; i < n; ++i) {
            int root = scanner.nextInt();
            int outGoingVertex = scanner.nextInt();
            while (outGoingVertex != -1) {
                graph.addEdge(root, outGoingVertex);
                outGoingVertex = scanner.nextInt();
            }
        }
        graphReverse = graph.reverse();

        LinkedList<Integer> list = new LinkedList<>();

        graphReverse.DFSReverse(list);

        int[] numSCC = new int[n];

        graph.DFS(list, numSCC);

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < numSCC.length; ++i) {
            if (numSCC[i] > max) {
                max = numSCC[i];
            }
        }

        int countSCC = max + 1;
        LinkedList<Integer>[] arrayOfListsNumSCC = new LinkedList[countSCC];

        for (int i = 0; i < arrayOfListsNumSCC.length; ++i) {
            arrayOfListsNumSCC[i] = new LinkedList();
        }
        for (int i = 0; i < numSCC.length; ++i) {
            arrayOfListsNumSCC[numSCC[i]].add(i);
        }

        printSCC(numSCC, arrayOfListsNumSCC);

        Graph metaGraph = graph.getMetaGraph(numSCC, countSCC);

        System.out.println("Meta-graph:");

        metaGraph.print();
    } //добавить комментарии
}
