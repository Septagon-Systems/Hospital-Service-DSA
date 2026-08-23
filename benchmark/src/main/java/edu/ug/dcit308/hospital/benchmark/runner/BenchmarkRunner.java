package edu.ug.dcit308.hospital.benchmark.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;
import edu.ug.dcit308.hospital.algo.sort.InsertionSort;
import edu.ug.dcit308.hospital.algo.sort.MergeSort;
import edu.ug.dcit308.hospital.algo.sort.QuickSort;
import edu.ug.dcit308.hospital.algo.sort.SelectionSort;
import edu.ug.dcit308.hospital.benchmark.core.Algorithm;
import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;
import edu.ug.dcit308.hospital.ds.deque.ArrayDeque;
import edu.ug.dcit308.hospital.ds.hash.CustomHashTable;
import edu.ug.dcit308.hospital.ds.linked.DoublyLinkedList;
import edu.ug.dcit308.hospital.ds.linked.SinglyLinkedList;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.ds.queue.CircularQueue;
import edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue;
import edu.ug.dcit308.hospital.ds.stack.ArrayStack;
import edu.ug.dcit308.hospital.ds.tree.BTree;
import edu.ug.dcit308.hospital.ds.tree.BinarySearchTree;
import edu.ug.dcit308.hospital.ds.tree.RedBlackTree;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.KruskalMST;
import edu.ug.dcit308.hospital.graph.mst.PrimMST;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath;
import edu.ug.dcit308.hospital.graph.traversal.GraphTraversal;
import edu.ug.dcit308.hospital.scheduling.strategy.HospitalDispatchCentre;
import edu.ug.dcit308.hospital.scheduling.strategy.Resource;
import edu.ug.dcit308.hospital.scheduling.strategy.ResourcePool;
import edu.ug.dcit308.hospital.scheduling.strategy.ServiceRequest;

/**
 * Deterministic benchmark execution engine for all core data structures,
 * algorithms, and emergency healthcare service operations.
 *
 * <p>Executes runs across standard scales [100, 500, 1000, 5000, 10000, 20000]
 * with 3 iterations per size, capturing nanosecond timing and heap memory delta.</p>
 */
public class BenchmarkRunner {

    /** Scales required by the project brief: 100, 500, 1000, 5000, 10000, 20000. */
    public static final int[] DEFAULT_SIZES = {100, 500, 1000, 5000, 10000, 20000};

    /** Number of repeated runs per (algorithm, size) pair. */
    public static final int DEFAULT_ITERATIONS = 3;

    /** Fixed deterministic seed ensuring 100% reproducible benchmark datasets. */
    public static final long RANDOM_SEED = 42L;

    private final Map<String, Algorithm> registry = new LinkedHashMap<>();

    public BenchmarkRunner() {
        registerAllCoreAlgorithms();
    }

    private void registerAllCoreAlgorithms() {
        // --- 1. Sorting Algorithms (Pod 2) ---
        registry.put("InsertionSort", InsertionSort::sort);
        registry.put("SelectionSort", data -> {
            Integer[] boxed = box(data);
            new SelectionSort<Integer>().sort(boxed);
        });
        registry.put("MergeSort", MergeSort::sort);
        registry.put("QuickSort", QuickSort::sort);

        // --- 2. Search Algorithms (Pod 1) ---
        registry.put("LinearSearch", data -> LinearSearch.search(data, -1));
        registry.put("BinarySearch", data -> {
            QuickSort.sort(data);
            Integer[] boxed = box(data);
            new BinarySearch().search(boxed, -1);
        });

        // --- 3. Linear Data Structures (Pods 1, 3, 4) ---
        registry.put("DynamicArrayList-Append", data -> {
            DynamicArrayList<Integer> list = new DynamicArrayList<>();
            for (int x : data) list.add(x);
        });
        registry.put("SinglyLinkedList-Insert", data -> {
            SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
            for (int x : data) list.addFirst(x);
        });
        registry.put("DoublyLinkedList-Insert", data -> {
            DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
            for (int x : data) list.addFirst(x);
        });
        registry.put("ArrayStack-PushPop", data -> {
            ArrayStack<Integer> stack = new ArrayStack<>();
            for (int x : data) stack.push(x);
            while (!stack.isEmpty()) stack.pop();
        });
        registry.put("CircularQueue-EnqueueDequeue", data -> {
            CircularQueue<Integer> q = new CircularQueue<>(data.length + 1);
            for (int x : data) q.enqueue(x);
            while (!q.isEmpty()) q.dequeue();
        });
        registry.put("ArrayDeque-AddRemove", data -> {
            ArrayDeque<Integer> dq = new ArrayDeque<>();
            for (int x : data) dq.addFirst(x);
            while (!dq.isEmpty()) dq.removeFirst();
        });
        registry.put("MinHeap-EnqueueDequeue", data -> {
            MinHeapPriorityQueue<Integer> pq = new MinHeapPriorityQueue<>();
            for (int x : data) pq.enqueue(x);
            while (!pq.isEmpty()) pq.dequeue();
        });

        // --- 4. Trees & Hash Structures (Pod 5) ---
        registry.put("BinarySearchTree-InsertSearch", data -> {
            BinarySearchTree<Integer, Integer> bst = new BinarySearchTree<>();
            for (int x : data) bst.insert(x, x);
            for (int i = 0; i < Math.min(data.length, 100); i++) bst.search(data[i]);
        });
        registry.put("RedBlackTree-InsertSearch", data -> {
            RedBlackTree<Integer, Integer> rbt = new RedBlackTree<>();
            for (int x : data) rbt.insert(x, x);
            for (int i = 0; i < Math.min(data.length, 100); i++) rbt.search(data[i]);
        });
        registry.put("BTree-InsertSearch", data -> {
            BTree<Integer, Integer> btree = new BTree<>(3);
            for (int x : data) btree.insert(x, x);
            for (int i = 0; i < Math.min(data.length, 100); i++) btree.get(data[i]);
        });
        registry.put("CustomHashTable-PutGet", data -> {
            CustomHashTable<Integer, Integer> table = new CustomHashTable<>();
            for (int x : data) table.put(x, x);
            for (int i = 0; i < Math.min(data.length, 100); i++) table.get(data[i]);
        });

        // --- 5. Graph Routing & MST (Pod 6) ---
        registry.put("GraphTraversal-BFS", data -> {
            HospitalNetworkGraph graph = buildBenchmarkGraph(data);
            GraphTraversal.traverseBFS(graph, 0);
        });
        registry.put("GraphTraversal-DFS", data -> {
            HospitalNetworkGraph graph = buildBenchmarkGraph(data);
            GraphTraversal.traverseDFS(graph, 0);
        });
        registry.put("DijkstraShortestPath", data -> {
            HospitalNetworkGraph graph = buildBenchmarkGraph(data);
            DijkstraShortestPath.findShortestPath(graph, 0);
        });
        registry.put("KruskalMST", data -> {
            HospitalNetworkGraph graph = buildBenchmarkGraph(data);
            KruskalMST.compute(graph);
        });
        registry.put("PrimMST", data -> {
            HospitalNetworkGraph graph = buildBenchmarkGraph(data);
            PrimMST.compute(graph, 0);
        });

        // --- 6. Live Healthcare Multi-Queue & Stack Undo (Pod 6 & Scheduling) ---
        registry.put("DispatchCentre-AdmitServeUndo", data -> {
            int n = Math.min(data.length, 500);
            Resource[] resources = new Resource[n];
            for (int i = 0; i < n; i++) {
                resources[i] = new Resource(i, 0, (Math.abs(data[i]) % 100) + 10.0, 300.0);
            }
            ResourcePool pool = new ResourcePool(resources);
            HospitalDispatchCentre centre = new HospitalDispatchCentre(n, n, pool);
            for (int i = 0; i < n; i++) {
                int triage = (Math.abs(data[i]) % 3) + 1;
                centre.admitRequest(new ServiceRequest(i, 1, 2, triage, 0, 75.0, i, false));
            }
            for (int i = 0; i < n / 2; i++) {
                centre.serveNextRequest();
            }
            for (int i = 0; i < n / 4; i++) {
                centre.undoLastAction();
            }
        });
    }

    public void registerAlgorithm(String name, Algorithm algorithm) {
        if (name == null || algorithm == null) {
            throw new IllegalArgumentException("Name and Algorithm must not be null");
        }
        registry.put(name, algorithm);
    }

    public List<String> getRegisteredAlgorithmNames() {
        return Collections.unmodifiableList(new ArrayList<>(registry.keySet()));
    }

    /**
     * Runs a single (algorithm, inputSize) experiment for {@code iterations}
     * repetitions and returns one {@link BenchmarkResult} per repetition.
     */
    public List<BenchmarkResult> runExperiment(String algorithmName, int inputSize, int iterations) {
        if (algorithmName == null || !registry.containsKey(algorithmName)) {
            throw new IllegalArgumentException("No algorithm registered under name: " + algorithmName);
        }
        if (inputSize <= 0) {
            throw new IllegalArgumentException("Input size must be positive: " + inputSize);
        }
        if (iterations <= 0) {
            throw new IllegalArgumentException("Iterations must be positive: " + iterations);
        }

        Algorithm algorithm = registry.get(algorithmName);
        List<BenchmarkResult> results = new ArrayList<>(iterations);
        int[] baseline = generateData(inputSize);

        for (int iter = 1; iter <= iterations; iter++) {
            int[] data = baseline.clone();

            System.gc();
            long memBefore = usedMemoryBytes();
            long start = System.nanoTime();

            algorithm.run(data);

            long elapsedNs = System.nanoTime() - start;
            long memAfter = usedMemoryBytes();

            double memoryDeltaKb = Math.max(0, memAfter - memBefore) / 1024.0;
            results.add(new BenchmarkResult(algorithmName, inputSize, iter, elapsedNs, memoryDeltaKb));
        }

        return results;
    }

    /**
     * Runs every registered algorithm across {@code sizes}, {@code iterations} times each.
     */
    public List<BenchmarkResult> runAll(int[] sizes, int iterations) {
        if (sizes == null || sizes.length == 0) {
            sizes = DEFAULT_SIZES;
        }
        List<BenchmarkResult> all = new ArrayList<>();
        for (String name : registry.keySet()) {
            for (int size : sizes) {
                all.addAll(runExperiment(name, size, iterations));
            }
        }
        return all;
    }

    public List<BenchmarkResult> runAll() {
        return runAll(DEFAULT_SIZES, DEFAULT_ITERATIONS);
    }

    public static int[] generateData(int size) {
        Random rnd = new Random(RANDOM_SEED + size);
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = rnd.nextInt(1_000_000);
        }
        return data;
    }

    private static HospitalNetworkGraph buildBenchmarkGraph(int[] data) {
        int v = Math.min(data.length, 500);
        HospitalNetworkGraph graph = new HospitalNetworkGraph(v);
        for (int i = 0; i < v - 1; i++) {
            double weight = ((Math.abs(data[i]) % 50) + 1) * 1.5;
            graph.addEdge(i, i + 1, weight);
            if (i + 2 < v) {
                graph.addEdge(i, i + 2, weight * 1.2);
            }
        }
        return graph;
    }

    private static Integer[] box(int[] data) {
        Integer[] boxed = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            boxed[i] = data[i];
        }
        return boxed;
    }

    private static long usedMemoryBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
