package edu.ug.dcit308.hospital.gui.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Locale;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import edu.ug.dcit308.hospital.algo.search.BinarySearch;
import edu.ug.dcit308.hospital.algo.search.LinearSearch;
import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator;
import edu.ug.dcit308.hospital.app.util.StudentParameterCalculator.StudentParameters;
import edu.ug.dcit308.hospital.benchmark.core.BenchmarkResult;
import edu.ug.dcit308.hospital.benchmark.runner.BenchmarkRunner;
import edu.ug.dcit308.hospital.db.ConnectionManager;
import edu.ug.dcit308.hospital.db.importer.CsvImporter;
import edu.ug.dcit308.hospital.db.model.Hospital;
import edu.ug.dcit308.hospital.db.model.Route;
import edu.ug.dcit308.hospital.db.repository.HospitalRepository;
import edu.ug.dcit308.hospital.db.repository.RouteRepository;
import edu.ug.dcit308.hospital.db.repository.ServiceRequestRepository;
import edu.ug.dcit308.hospital.db.schema.SchemaInitializer;
import edu.ug.dcit308.hospital.ds.hash.CustomHashTable;
import edu.ug.dcit308.hospital.ds.list.DynamicArrayList;
import edu.ug.dcit308.hospital.ds.map.CustomMap;
import edu.ug.dcit308.hospital.ds.tree.BTree;
import edu.ug.dcit308.hospital.ds.tree.BinarySearchTree;
import edu.ug.dcit308.hospital.ds.tree.RedBlackTree;
import edu.ug.dcit308.hospital.graph.core.HospitalNetworkGraph;
import edu.ug.dcit308.hospital.graph.mst.KruskalMST;
import edu.ug.dcit308.hospital.graph.mst.PrimMST;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath;
import edu.ug.dcit308.hospital.graph.shortestpath.DijkstraShortestPath.PathResult;
import edu.ug.dcit308.hospital.graph.traversal.GraphTraversal;
import edu.ug.dcit308.hospital.scheduling.strategy.ActionRecord;
import edu.ug.dcit308.hospital.scheduling.strategy.CapacityOptimizer;
import edu.ug.dcit308.hospital.scheduling.strategy.DispatchAssignment;
import edu.ug.dcit308.hospital.scheduling.strategy.HospitalDispatchCentre;
import edu.ug.dcit308.hospital.scheduling.strategy.OptimizationResult;
import edu.ug.dcit308.hospital.scheduling.strategy.Resource;
import edu.ug.dcit308.hospital.scheduling.strategy.ResourcePool;
import edu.ug.dcit308.hospital.scheduling.strategy.ServiceRequest;

/**
 * Embedded HTTP Server providing REST APIs and static dashboard assets for the
 * Ghana Smart Hospital Operations Optimizer (DCIT 204 / 308) GUI subproject.
 * Strictly enforces zero {@code java.util} collection classes.
 */
public class WebServer {

    private final int port;
    private HttpServer server;

    // Custom Data Structures
    private DynamicArrayList<Hospital> hospitalList;
    private DynamicArrayList<Route> routeList;
    private HospitalNetworkGraph networkGraph;
    private CustomHashTable<Integer, Hospital> hashTable;
    private RedBlackTree<Integer, Hospital> rbTree;
    private BinarySearchTree<Integer, Hospital> bst;
    private BTree<Integer, Hospital> bTree;
    private Integer[] sortedHospitalIds;
    private HospitalDispatchCentre dispatchCentre;
    private int requestSeqCounter = 5000;

    // Shift Batch & Queue Capacity Limits
    private DynamicArrayList<ServiceRequest> catalogRequests = new DynamicArrayList<>();
    private int catalogCursor = 0;
    private int urgentShiftLimit = 5;
    private int normalShiftLimit = 6;
    private int preemptionShiftLimit = 2;
    private ResourcePool activeResourcePool;

    private static final String[] SUPPLY_NAMES = {
            "Emergency Blood Plasma Packs (O-Neg)",
            "Portable ICU Ventilator Unit",
            "Emergency Surgical Trauma Kit",
            "Advanced Life Support Defibrillator",
            "Broad-Spectrum Antivenom Vials",
            "Pediatric Resuscitation Kit",
            "Portable Oxygen Concentrator",
            "Dialysis Filtration Membrane Pack"
    };
    private static final double[] SUPPLY_WEIGHTS = { 12.0, 25.0,  8.0, 15.0,  3.0,  6.0, 18.0, 10.0 };
    private static final double[] SUPPLY_VALUES  = { 90.0, 95.0, 80.0, 85.0, 70.0, 75.0, 65.0, 60.0 };

    public WebServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        initializeBackend();

        server = HttpServer.create(new InetSocketAddress(port), 0);

        // Static Web Dashboard Files
        server.createContext("/", new StaticFileHandler());

        // REST API Endpoints
        server.createContext("/api/system/status", this::handleSystemStatus);
        server.createContext("/api/hospitals", this::handleHospitals);
        server.createContext("/api/routes", this::handleRoutes);
        server.createContext("/api/route/dijkstra", this::handleDijkstra);
        server.createContext("/api/route/mst", this::handleMst);
        server.createContext("/api/route/traversal", this::handleTraversal);
        server.createContext("/api/search", this::handleSearch);
        server.createContext("/api/dispatch/status", this::handleDispatchStatus);
        server.createContext("/api/dispatch/queues", this::handleDispatchQueues);
        server.createContext("/api/dispatch/repopulate", this::handleDispatchRepopulate);
        server.createContext("/api/dispatch/serve", this::handleDispatchServe);
        server.createContext("/api/dispatch/batch", this::handleDispatchBatch);
        server.createContext("/api/dispatch/admit", this::handleDispatchAdmit);
        server.createContext("/api/dispatch/undo", this::handleDispatchUndo);
        server.createContext("/api/optimizer/knapsack", this::handleKnapsack);
        server.createContext("/api/benchmark/run", this::handleBenchmark);
        server.createContext("/api/student/calc", this::handleStudentCalc);

        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        System.out.println("=============================================================");
        System.out.printf("✓ Ghana Smart Hospital Operations Optimizer (DCIT 204/308) GUI running!\n");
        System.out.printf("✓ Dashboard URL: http://localhost:%d\n", port);
        System.out.println("=============================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void initializeBackend() {
        try (Connection conn = ConnectionManager.getConnection()) {
            SchemaInitializer.initialize(conn);
            CsvImporter.importAll(conn, "data/seed");

            HospitalRepository hospRepo = new HospitalRepository(conn);
            RouteRepository routeRepo = new RouteRepository(conn);
            ServiceRequestRepository reqRepo = new ServiceRequestRepository(conn);

            hospitalList = new DynamicArrayList<>();
            for (Hospital h : hospRepo.findAll()) {
                hospitalList.add(h);
            }

            routeList = new DynamicArrayList<>();
            for (Route r : routeRepo.findAll()) {
                routeList.add(r);
            }

            int n = hospitalList.size();
            networkGraph = new HospitalNetworkGraph(n + 1, false);
            hashTable = new CustomHashTable<>();
            rbTree = new RedBlackTree<>();
            bst = new BinarySearchTree<>();
            bTree = new BTree<>(2);
            sortedHospitalIds = new Integer[n];

            for (int i = 0; i < n; i++) {
                Hospital h = hospitalList.get(i);
                int id = h.getHospitalId();
                hashTable.put(id, h);
                rbTree.insert(id, h);
                bst.insert(id, h);
                bTree.insert(id, h);
                sortedHospitalIds[i] = id;
            }

            for (int i = 0; i < routeList.size(); i++) {
                Route r = routeList.get(i);
                if (r.getSourceHospitalId() <= n && r.getDestinationHospitalId() <= n) {
                    networkGraph.addEdge(r.getSourceHospitalId(), r.getDestinationHospitalId(), r.getDistanceKm());
                }
            }

            Resource[] resources = new Resource[] {
                    new Resource(1, 0, 150.0, 300.0),
                    new Resource(2, 0, 120.0, 250.0),
                    new Resource(3, 0, 140.0, 280.0),
                    new Resource(4, 0, 160.0, 300.0),
                    new Resource(5, 2, 200.0, 150.0),
                    new Resource(6, 2, 180.0, 150.0),
                    new Resource(7, 2, 190.0, 150.0),
                    new Resource(8, 1,  50.0,  50.0),
                    new Resource(9, 1,  45.0,  50.0),
                    new Resource(10, 1, 55.0,  50.0)
            };
            this.activeResourcePool = new ResourcePool(resources);
            this.dispatchCentre = new HospitalDispatchCentre(normalShiftLimit, 20, activeResourcePool);

            catalogRequests = new DynamicArrayList<>();
            for (edu.ug.dcit308.hospital.db.model.ServiceRequest dr : reqRepo.findAll()) {
                int resType = 0;
                String cat = dr.getRequestType().toLowerCase();
                if (cat.contains("blood")) resType = 1;
                else if (cat.contains("icu") || cat.contains("ventilator")) resType = 2;

                ServiceRequest sr = new ServiceRequest(
                        dr.getRequestId(),
                        dr.getOriginHospitalId(),
                        dr.getTargetHospitalId(),
                        dr.getPatientTriageLevel(),
                        resType,
                        dr.getWeightKg(),
                        dr.getRequestId(),
                        dr.getPatientTriageLevel() == 1
                );
                catalogRequests.add(sr);
            }
            populateShiftBatch();
        } catch (Exception e) {
            System.err.println("! Error initializing backend: " + e.getMessage());
        }
    }

    private synchronized void populateShiftBatch() {
        if (catalogRequests.isEmpty()) return;

        int urgCount = dispatchCentre.getPendingUrgentCount();
        int normCount = dispatchCentre.getPendingNormalCount();
        int premptCount = dispatchCentre.getPendingPreemptionCount();

        int checked = 0;
        int totalCatalog = catalogRequests.size();

        while (checked < totalCatalog && (urgCount < urgentShiftLimit || normCount < normalShiftLimit || premptCount < preemptionShiftLimit)) {
            ServiceRequest base = catalogRequests.get(catalogCursor % totalCatalog);
            catalogCursor = (catalogCursor + 1) % totalCatalog;
            checked++;

            ServiceRequest sr = new ServiceRequest(
                    ++requestSeqCounter,
                    base.getOriginHospitalId(),
                    base.getTargetHospitalId(),
                    base.getTriageLevel(),
                    base.getResourceType(),
                    base.getWeightKg(),
                    requestSeqCounter,
                    base.isPreempted()
            );

            if (sr.isPreempted() && premptCount < preemptionShiftLimit) {
                dispatchCentre.admitRequest(sr);
                premptCount++;
            } else if (sr.getTriageLevel() <= 2 && urgCount < urgentShiftLimit) {
                dispatchCentre.admitRequest(sr);
                urgCount++;
            } else if (sr.getTriageLevel() >= 3 && normCount < normalShiftLimit) {
                dispatchCentre.admitRequest(sr);
                normCount++;
            }
        }
        dispatchCentre.clearActionStack();
    }

    // -------------------------------------------------------------------------
    // API Handlers (Safe & Exception-Guarded)
    // -------------------------------------------------------------------------

    private void handleSystemStatus(HttpExchange exchange) {
        try {
            String json = String.format(Locale.US, "{\"hospitals\":%d,\"routes\":%d,\"pendingRequests\":%d,\"servedRequests\":%d,\"status\":\"ONLINE\"}",
                    hospitalList.size(), routeList.size(), dispatchCentre.getTotalPendingCount(), dispatchCentre.getServedCount());
            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleHospitals(HttpExchange exchange) {
        try {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < hospitalList.size(); i++) {
                Hospital h = hospitalList.get(i);
                if (i > 0) sb.append(",");
                sb.append(String.format(Locale.US, "{\"id\":%d,\"name\":\"%s\",\"region\":\"%s\",\"district\":\"%s\",\"town\":\"%s\"," +
                                "\"lat\":%.6f,\"lng\":%.6f,\"type\":\"%s\",\"ownership\":\"%s\",\"icu\":%d,\"ambulances\":%d}",
                        h.getHospitalId(), escapeJson(h.getName()), escapeJson(h.getRegion()),
                        escapeJson(h.getDistrict()), escapeJson(h.getTown()),
                        h.getLatitude(), h.getLongitude(), escapeJson(h.getFacilityType()),
                        escapeJson(h.getOwnershipType()), h.getIcuCapacity(), h.getAmbulanceCount()));
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleRoutes(HttpExchange exchange) {
        try {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < routeList.size(); i++) {
                Route r = routeList.get(i);
                if (i > 0) sb.append(",");
                sb.append(String.format(Locale.US, "{\"id\":%d,\"from\":%d,\"to\":%d,\"distanceKm\":%.2f,\"travelTime\":%.1f,\"condition\":\"%s\"}",
                        r.getRouteId(), r.getSourceHospitalId(), r.getDestinationHospitalId(),
                        r.getDistanceKm(), r.getAvgTravelTimeMins(), escapeJson(r.getRoadCondition())));
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDijkstra(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String srcStr = params.get("source");
            String dstStr = params.get("dest");
            int source = (srcStr != null) ? Integer.parseInt(srcStr) : 1;
            int dest = (dstStr != null) ? Integer.parseInt(dstStr) : 16;

            long start = System.nanoTime();
            PathResult result = DijkstraShortestPath.findShortestPath(networkGraph, source, dest);
            long elapsedNs = System.nanoTime() - start;

            int[] path = result.reconstructPath();
            StringBuilder waypointsJson = new StringBuilder("[");
            for (int i = 0; i < path.length; i++) {
                if (i > 0) waypointsJson.append(",");
                int nodeId = path[i];
                Hospital h = hashTable.get(nodeId);
                double legDist = (i == 0) ? 0 : networkGraph.getWeight(path[i - 1], nodeId);
                waypointsJson.append(String.format(Locale.US, "{\"id\":%d,\"name\":\"%s\",\"region\":\"%s\",\"lat\":%.6f,\"lng\":%.6f,\"legKm\":%.2f}",
                        nodeId, (h != null ? escapeJson(h.getName()) : "#" + nodeId),
                        (h != null ? escapeJson(h.getRegion()) : ""),
                        (h != null ? h.getLatitude() : 0.0),
                        (h != null ? h.getLongitude() : 0.0),
                        legDist));
            }
            waypointsJson.append("]");

            String json = String.format(Locale.US, "{\"reachable\":%b,\"totalDistanceKm\":%.2f,\"travelTimeMins\":%.1f,\"executionTimeNs\":%d,\"waypoints\":%s}",
                    result.getDistance() != Double.POSITIVE_INFINITY,
                    result.getDistance(), result.getDistance(), elapsedNs, waypointsJson.toString());

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleMst(HttpExchange exchange) {
        try {
            long startKruskal = System.nanoTime();
            KruskalMST.WeightedEdge[] kruskalEdges = KruskalMST.compute(networkGraph);
            long timeKruskalNs = System.nanoTime() - startKruskal;

            double kruskalTotalKm = 0;
            StringBuilder edgesJson = new StringBuilder("[");
            for (int i = 0; i < kruskalEdges.length; i++) {
                if (i > 0) edgesJson.append(",");
                KruskalMST.WeightedEdge e = kruskalEdges[i];
                kruskalTotalKm += e.weight;
                Hospital h1 = hashTable.get(e.source);
                Hospital h2 = hashTable.get(e.destination);
                edgesJson.append(String.format(Locale.US, "{\"source\":%d,\"dest\":%d,\"weight\":%.2f,\"fromName\":\"%s\",\"toName\":\"%s\"}",
                        e.source, e.destination, e.weight,
                        (h1 != null ? escapeJson(h1.getName()) : "#" + e.source),
                        (h2 != null ? escapeJson(h2.getName()) : "#" + e.destination)));
            }
            edgesJson.append("]");

            long startPrim = System.nanoTime();
            PrimMST.MSTEdge[] primEdges = PrimMST.compute(networkGraph, 1);
            long timePrimNs = System.nanoTime() - startPrim;

            double primTotalKm = 0;
            for (PrimMST.MSTEdge e : primEdges) {
                primTotalKm += e.weight;
            }

            String json = String.format(Locale.US, "{\"kruskalEdges\":%s,\"kruskalTotalKm\":%.2f,\"kruskalTimeNs\":%d,\"primTotalKm\":%.2f,\"primTimeNs\":%d}",
                    edgesJson.toString(), kruskalTotalKm, timeKruskalNs, primTotalKm, timePrimNs);

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleTraversal(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String sStr = params.get("start");
            int startId = (sStr != null) ? Integer.parseInt(sStr) : 1;

            int[] bfs = GraphTraversal.traverseBFS(networkGraph, startId);
            int[] dfs = GraphTraversal.traverseDFS(networkGraph, startId);

            String json = String.format(Locale.US, "{\"startNode\":%d,\"bfsCount\":%d,\"dfsCount\":%d,\"bfs\":%s,\"dfs\":%s}",
                    startId, bfs.length, dfs.length, intArrayToJson(bfs), intArrayToJson(dfs));

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleSearch(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String idStr = params.get("id");
            int id = (idStr != null) ? Integer.parseInt(idStr) : 1;

            int iters = 5000;

            long s1 = System.nanoTime();
            for (int i = 0; i < iters; i++) hashTable.get(id);
            long hashNs = Math.max(1, (System.nanoTime() - s1) / iters);

            long s2 = System.nanoTime();
            for (int i = 0; i < iters; i++) rbTree.search(id);
            long rbNs = Math.max(1, (System.nanoTime() - s2) / iters);

            long s3 = System.nanoTime();
            for (int i = 0; i < iters; i++) bst.search(id);
            long bstNs = Math.max(1, (System.nanoTime() - s3) / iters);

            long s4 = System.nanoTime();
            for (int i = 0; i < iters; i++) bTree.get(id);
            long bTreeNs = Math.max(1, (System.nanoTime() - s4) / iters);

            BinarySearch bs = new BinarySearch();
            long s5 = System.nanoTime();
            for (int i = 0; i < iters; i++) bs.search(sortedHospitalIds, id);
            long binaryNs = Math.max(1, (System.nanoTime() - s5) / iters);

            long s6 = System.nanoTime();
            for (int i = 0; i < iters; i++) LinearSearch.search(sortedHospitalIds, id);
            long linearNs = Math.max(1, (System.nanoTime() - s6) / iters);

            Hospital hHash = hashTable.get(id);

            if (hHash == null) {
                sendJsonResponse(exchange, 404, "{\"error\":\"Facility not found\"}");
                return;
            }

            String json = String.format(Locale.US, "{\"found\":true,\"hospital\":{\"id\":%d,\"name\":\"%s\",\"region\":\"%s\",\"town\":\"%s\"," +
                            "\"type\":\"%s\",\"icu\":%d,\"ambulances\":%d}," +
                            "\"timings\":{\"hashNs\":%d,\"rbNs\":%d,\"bstNs\":%d,\"bTreeNs\":%d,\"binaryNs\":%d,\"linearNs\":%d}}",
                    hHash.getHospitalId(), escapeJson(hHash.getName()), escapeJson(hHash.getRegion()),
                    escapeJson(hHash.getTown()), escapeJson(hHash.getFacilityType()),
                    hHash.getIcuCapacity(), hHash.getAmbulanceCount(),
                    hashNs, rbNs, bstNs, bTreeNs, binaryNs, linearNs);

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchStatus(HttpExchange exchange) {
        try {
            String json = String.format(Locale.US, "{\"urgent\":%d,\"preemption\":%d,\"normal\":%d,\"totalPending\":%d,\"undoDepth\":%d,\"served\":%d,\"availableResources\":%d,\"totalResources\":%d}",
                    dispatchCentre.getPendingUrgentCount(), dispatchCentre.getPendingPreemptionCount(),
                    dispatchCentre.getPendingNormalCount(), dispatchCentre.getTotalPendingCount(),
                    dispatchCentre.pendingActions(), dispatchCentre.getServedCount(),
                    dispatchCentre.getResourcePool().getAvailableCount(), dispatchCentre.getResourcePool().size());

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private static String getResourceTypeName(int type) {
        switch (type) {
            case 0: return "Ambulance Vehicle";
            case 1: return "Blood Transfusion Pack";
            case 2: return "ICU Bed / Ventilator";
            default: return "Medical Resource";
        }
    }

    private static String getTriageName(int triage, boolean preempted) {
        if (preempted) return "Critical Preemption (P1)";
        if (triage == 1) return "Critical Emergency (P1)";
        if (triage == 2) return "Urgent Transfer (P2)";
        return "Normal Routine (P3)";
    }

    private void handleDispatchServe(HttpExchange exchange) {
        try {
            DispatchAssignment assignment = dispatchCentre.serveNextRequest();
            if (assignment == null) {
                populateShiftBatch();
                assignment = dispatchCentre.serveNextRequest();
            }
            if (assignment == null) {
                sendJsonResponse(exchange, 200, "{\"success\":false,\"message\":\"No more pending requests\"}");
                return;
            }

            ServiceRequest req = assignment.getRequest();
            Resource res = assignment.getResource();
            Hospital hFrom = hashTable.get(req.getOriginHospitalId());
            Hospital hTo = hashTable.get(req.getTargetHospitalId());

            String json = String.format(Locale.US,
                    "{\"success\":true,\"requestId\":%d,\"triageLevel\":%d,\"triageName\":\"%s\"," +
                    "\"serviceType\":\"%s\",\"weightKg\":%.1f,\"fromName\":\"%s\",\"toName\":\"%s\"," +
                    "\"resource\":{\"id\":%d,\"type\":%d,\"typeName\":\"%s\",\"costGhc\":%.2f}}",
                    req.getId(), req.getTriageLevel(),
                    escapeJson(getTriageName(req.getTriageLevel(), req.isPreempted())),
                    escapeJson(getResourceTypeName(req.getResourceType())),
                    req.getWeightKg(),
                    (hFrom != null ? escapeJson(hFrom.getName()) : "#" + req.getOriginHospitalId()),
                    (hTo != null ? escapeJson(hTo.getName()) : "#" + req.getTargetHospitalId()),
                    (res != null ? res.getId() : 0),
                    (res != null ? res.getResourceType() : 0),
                    escapeJson(res != null ? getResourceTypeName(res.getResourceType()) : "Resource"),
                    (res != null ? res.getCostPerUse() : 0.0));

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchBatch(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String cStr = params.get("count");
            int count = (cStr != null) ? Integer.parseInt(cStr) : 5;

            DynamicArrayList<DispatchAssignment> assignments = new DynamicArrayList<>();
            for (int i = 0; i < count; i++) {
                DispatchAssignment da = dispatchCentre.serveNextRequest();
                if (da == null) {
                    populateShiftBatch();
                    da = dispatchCentre.serveNextRequest();
                }
                if (da != null) {
                    assignments.add(da);
                } else {
                    break;
                }
            }

            StringBuilder reqsJson = new StringBuilder("[");
            for (int i = 0; i < assignments.size(); i++) {
                if (i > 0) reqsJson.append(",");
                DispatchAssignment da = assignments.get(i);
                ServiceRequest sr = da.getRequest();
                Resource res = da.getResource();
                Hospital hFrom = hashTable.get(sr.getOriginHospitalId());
                Hospital hTo = hashTable.get(sr.getTargetHospitalId());
                reqsJson.append(String.format(Locale.US,
                        "{\"requestId\":%d,\"triageLevel\":%d,\"triageName\":\"%s\",\"serviceType\":\"%s\",\"weightKg\":%.1f," +
                        "\"fromName\":\"%s\",\"toName\":\"%s\"," +
                        "\"sourceQueue\":\"%s\",\"resource\":{\"id\":%d,\"type\":%d,\"typeName\":\"%s\",\"costGhc\":%.2f}}",
                        sr.getId(), sr.getTriageLevel(),
                        escapeJson(getTriageName(sr.getTriageLevel(), sr.isPreempted())),
                        escapeJson(getResourceTypeName(sr.getResourceType())),
                        sr.getWeightKg(),
                        (hFrom != null ? escapeJson(hFrom.getName()) : "#" + sr.getOriginHospitalId()),
                        (hTo != null ? escapeJson(hTo.getName()) : "#" + sr.getTargetHospitalId()),
                        (sr.isPreempted() ? "Urgent Deque" : (sr.getTriageLevel() <= 2 ? "MinHeap PriorityQueue" : "Normal CircularQueue")),
                        (res != null ? res.getId() : 0),
                        (res != null ? res.getResourceType() : 0),
                        escapeJson(res != null ? getResourceTypeName(res.getResourceType()) : "Resource"),
                        (res != null ? res.getCostPerUse() : 0.0)));
            }
            reqsJson.append("]");

            String json = String.format(Locale.US, "{\"served\":%d,\"remainingPending\":%d,\"requests\":%s}",
                    assignments.size(), dispatchCentre.getTotalPendingCount(), reqsJson.toString());

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchQueues(HttpExchange exchange) {
        try {
            // Snapshot Undo Stack (top 6 items)
            edu.ug.dcit308.hospital.ds.stack.ArrayStack<ActionRecord> stack = dispatchCentre.getActionStack();
            edu.ug.dcit308.hospital.ds.stack.ArrayStack<ActionRecord> tempStack = new edu.ug.dcit308.hospital.ds.stack.ArrayStack<>();
            DynamicArrayList<ActionRecord> recentActions = new DynamicArrayList<>();
            int stackLimit = Math.min(6, stack.size());
            for (int i = 0; i < stackLimit; i++) {
                ActionRecord ar = stack.pop();
                recentActions.add(ar);
                tempStack.push(ar);
            }
            while (!tempStack.isEmpty()) {
                stack.push(tempStack.pop());
            }

            StringBuilder stackJson = new StringBuilder("[");
            for (int i = 0; i < recentActions.size(); i++) {
                if (i > 0) stackJson.append(",");
                ActionRecord ar = recentActions.get(i);
                ServiceRequest sr = ar.getRequest();
                Hospital hFrom = hashTable.get(sr.getOriginHospitalId());
                Hospital hTo = hashTable.get(sr.getTargetHospitalId());
                stackJson.append(String.format(Locale.US,
                        "{\"action\":\"%s\",\"requestId\":%d,\"triage\":%d,\"triageName\":\"%s\",\"serviceType\":\"%s\",\"weightKg\":%.1f," +
                        "\"from\":\"%s\",\"to\":\"%s\",\"resourceId\":%d,\"resourceName\":\"%s\",\"costGhc\":%.2f}",
                        ar.getActionType(), sr.getId(), sr.getTriageLevel(),
                        escapeJson(getTriageName(sr.getTriageLevel(), sr.isPreempted())),
                        escapeJson(getResourceTypeName(sr.getResourceType())),
                        sr.getWeightKg(),
                        (hFrom != null ? escapeJson(hFrom.getName()) : "#" + sr.getOriginHospitalId()),
                        (hTo != null ? escapeJson(hTo.getName()) : "#" + sr.getTargetHospitalId()),
                        (ar.getResource() != null ? ar.getResource().getId() : 0),
                        escapeJson(ar.getResource() != null ? getResourceTypeName(ar.getResource().getResourceType()) : "Resource"),
                        (ar.getResource() != null ? ar.getResource().getCostPerUse() : 0.0)));
            }
            stackJson.append("]");

            // Snapshot Normal CircularQueue (first 6 items)
            edu.ug.dcit308.hospital.ds.queue.CircularQueue<ServiceRequest> nq = dispatchCentre.getNormalQueue();
            edu.ug.dcit308.hospital.ds.queue.CircularQueue<ServiceRequest> tempNq = new edu.ug.dcit308.hospital.ds.queue.CircularQueue<>(Math.max(10, nq.size() + 10));
            DynamicArrayList<ServiceRequest> nqItems = new DynamicArrayList<>();
            int nqLimit = Math.min(6, nq.size());
            for (int i = 0; i < nqLimit; i++) {
                ServiceRequest sr = nq.dequeue();
                nqItems.add(sr);
                tempNq.enqueue(sr);
            }
            while (!nq.isEmpty()) {
                tempNq.enqueue(nq.dequeue());
            }
            while (!tempNq.isEmpty()) {
                nq.enqueue(tempNq.dequeue());
            }

            StringBuilder nqJson = new StringBuilder("[");
            for (int i = 0; i < nqItems.size(); i++) {
                if (i > 0) nqJson.append(",");
                ServiceRequest sr = nqItems.get(i);
                Hospital hFrom = hashTable.get(sr.getOriginHospitalId());
                Hospital hTo = hashTable.get(sr.getTargetHospitalId());
                nqJson.append(String.format(Locale.US,
                        "{\"requestId\":%d,\"triage\":%d,\"triageName\":\"%s\",\"serviceType\":\"%s\",\"weightKg\":%.1f,\"from\":\"%s\",\"to\":\"%s\"}",
                        sr.getId(), sr.getTriageLevel(),
                        escapeJson(getTriageName(sr.getTriageLevel(), sr.isPreempted())),
                        escapeJson(getResourceTypeName(sr.getResourceType())),
                        sr.getWeightKg(),
                        (hFrom != null ? escapeJson(hFrom.getName()) : "#" + sr.getOriginHospitalId()),
                        (hTo != null ? escapeJson(hTo.getName()) : "#" + sr.getTargetHospitalId())));
            }
            nqJson.append("]");

            // Snapshot Urgent MinHeap PQ (first 6 items)
            edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue<ServiceRequest> uq = dispatchCentre.getUrgentQueue();
            edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue<ServiceRequest> tempUq = new edu.ug.dcit308.hospital.ds.queue.MinHeapPriorityQueue<>();
            DynamicArrayList<ServiceRequest> uqItems = new DynamicArrayList<>();
            int uqLimit = Math.min(6, uq.size());
            for (int i = 0; i < uqLimit; i++) {
                ServiceRequest sr = uq.dequeue();
                uqItems.add(sr);
                tempUq.enqueue(sr);
            }
            while (!uq.isEmpty()) {
                tempUq.enqueue(uq.dequeue());
            }
            while (!tempUq.isEmpty()) {
                uq.enqueue(tempUq.dequeue());
            }

            StringBuilder uqJson = new StringBuilder("[");
            for (int i = 0; i < uqItems.size(); i++) {
                if (i > 0) uqJson.append(",");
                ServiceRequest sr = uqItems.get(i);
                Hospital hFrom = hashTable.get(sr.getOriginHospitalId());
                Hospital hTo = hashTable.get(sr.getTargetHospitalId());
                uqJson.append(String.format(Locale.US,
                        "{\"requestId\":%d,\"triage\":%d,\"triageName\":\"%s\",\"serviceType\":\"%s\",\"weightKg\":%.1f,\"from\":\"%s\",\"to\":\"%s\"}",
                        sr.getId(), sr.getTriageLevel(),
                        escapeJson(getTriageName(sr.getTriageLevel(), sr.isPreempted())),
                        escapeJson(getResourceTypeName(sr.getResourceType())),
                        sr.getWeightKg(),
                        (hFrom != null ? escapeJson(hFrom.getName()) : "#" + sr.getOriginHospitalId()),
                        (hTo != null ? escapeJson(hTo.getName()) : "#" + sr.getTargetHospitalId())));
            }
            uqJson.append("]");

            String json = String.format(Locale.US, "{\"undoStack\":%s,\"normalQueue\":%s,\"urgentQueue\":%s}",
                    stackJson.toString(), nqJson.toString(), uqJson.toString());

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchAdmit(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String pStr = params.get("priority");
            String tStr = params.get("type");
            String oStr = params.get("origin");
            String dstStr = params.get("target");

            int priority = (pStr != null) ? Integer.parseInt(pStr) : 1;
            int resType = (tStr != null) ? Integer.parseInt(tStr) : 0;
            int origin = (oStr != null) ? Integer.parseInt(oStr) : 1;
            int target = (dstStr != null) ? Integer.parseInt(dstStr) : 16;

            ServiceRequest newReq = new ServiceRequest(
                    ++requestSeqCounter, origin, target, priority, resType, 70.0,
                    requestSeqCounter, priority == 1
            );

            boolean admitted = dispatchCentre.admitRequest(newReq);
            sendJsonResponse(exchange, 200, String.format(Locale.US, "{\"admitted\":%b,\"requestId\":%d,\"queueType\":\"%s\"}",
                    admitted, newReq.getId(), (priority == 1 ? "Urgent Deque" : (priority == 2 ? "MinHeap PQ" : "Circular Queue"))));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchUndo(HttpExchange exchange) {
        try {
            ActionRecord undone = dispatchCentre.undoLastAction();
            if (undone == null) {
                sendJsonResponse(exchange, 200, "{\"success\":false,\"message\":\"Undo stack is empty\"}");
                return;
            }

            String json = String.format(Locale.US, "{\"success\":true,\"actionType\":\"%s\",\"requestId\":%d,\"resourceId\":%d}",
                    undone.getActionType(), undone.getRequest().getId(),
                    (undone.getResource() != null ? undone.getResource().getId() : 0));

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleKnapsack(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String capStr = params.get("capacity");
            double capacity = (capStr != null) ? Double.parseDouble(capStr) : 35.0;

            long start = System.nanoTime();
            OptimizationResult result = new CapacityOptimizer().optimizeCapacity(SUPPLY_WEIGHTS, SUPPLY_VALUES, capacity);
            long elapsedNs = System.nanoTime() - start;

            DynamicArrayList<Integer> selected = result.getSelectedIndices();
            StringBuilder itemsJson = new StringBuilder("[");
            for (int i = 0; i < selected.size(); i++) {
                if (i > 0) itemsJson.append(",");
                int idx = selected.get(i);
                itemsJson.append(String.format(Locale.US, "{\"name\":\"%s\",\"weight\":%.1f,\"value\":%.1f}",
                        escapeJson(SUPPLY_NAMES[idx]), SUPPLY_WEIGHTS[idx], SUPPLY_VALUES[idx]));
            }
            itemsJson.append("]");

            String json = String.format(Locale.US, "{\"capacity\":%.1f,\"totalValue\":%.1f,\"totalWeight\":%.1f,\"executionTimeNs\":%d,\"selectedItems\":%s}",
                    capacity, result.getTotalValue(), result.getTotalWeight(), elapsedNs, itemsJson.toString());

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private static int[] getScalesForAlgorithm(String algo) {
        if (algo.equals("BinarySearch") || algo.equals("LinearSearch") || algo.contains("Lookup")) {
            // Uniform 500k increments up to 3M: perfectly renders linear vs log vs flat curves
            return new int[]{ 500_000, 1_000_000, 1_500_000, 2_000_000, 2_500_000, 3_000_000 };
        } else if (algo.equals("QuickSort") || algo.equals("MergeSort")) {
            // High-scale sorting up to 3 Million (500k steps) to clearly demonstrate O(N log N) scalability
            return new int[]{ 500_000, 1_000_000, 1_500_000, 2_000_000, 2_500_000, 3_000_000 };
        } else if (algo.equals("InsertionSort") || algo.equals("SelectionSort")) {
            // Quadratic sorting up to 100k (20k steps) to visibly expose steep O(N^2) parabolic growth
            return new int[]{ 20_000, 40_000, 60_000, 80_000, 100_000 };
        } else if (algo.contains("Tree") || algo.contains("HashTable")) {
            // Uniform 20k increments for tree index structures
            return new int[]{ 20_000, 40_000, 60_000, 80_000, 100_000 };
        } else if (algo.contains("Graph") || algo.contains("Dijkstra") || algo.contains("Kruskal") || algo.contains("Prim")) {
            // Network graph nodes V with uniform 500 node increments
            return new int[]{ 500, 1_000, 1_500, 2_000, 2_500 };
        }
        return BenchmarkRunner.DEFAULT_SIZES;
    }

    private void handleBenchmark(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String algo = params.get("algo");
            if (algo == null) algo = "QuickSort";

            BenchmarkRunner runner = new BenchmarkRunner();
            DynamicArrayList<BenchmarkResult> results = new DynamicArrayList<>();

            int[] scales = getScalesForAlgorithm(algo);
            for (int size : scales) {
                for (BenchmarkResult r : runner.runExperiment(algo, size, 3)) {
                    results.add(r);
                }
            }

            StringBuilder rowsJson = new StringBuilder("[");
            for (int i = 0; i < results.size(); i++) {
                if (i > 0) rowsJson.append(",");
                BenchmarkResult r = results.get(i);
                rowsJson.append(String.format(Locale.US, "{\"scale\":%d,\"timeMs\":%.4f,\"memoryKb\":%.2f,\"run\":%d}",
                        r.getInputSize(), r.getExecutionTimeMs(), r.getMemoryUsedKb(), r.getRunNumber()));
            }
            rowsJson.append("]");

            sendJsonResponse(exchange, 200, String.format(Locale.US, "{\"algorithm\":\"%s\",\"runs\":%s}", algo, rowsJson.toString()));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleDispatchRepopulate(HttpExchange exchange) {
        try {
            populateShiftBatch();
            sendJsonResponse(exchange, 200, String.format(Locale.US,
                    "{\"success\":true,\"urgent\":%d,\"normal\":%d,\"preempt\":%d,\"totalPending\":%d}",
                    dispatchCentre.getPendingUrgentCount(),
                    dispatchCentre.getPendingNormalCount(),
                    dispatchCentre.getPendingPreemptionCount(),
                    dispatchCentre.getTotalPendingCount()));
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleStudentCalc(HttpExchange exchange) {
        try {
            CustomMap<String, String> params = parseQueryParams(exchange.getRequestURI());
            String idStr = params.get("id");
            if (idStr == null) idStr = "10987654";
            String applyStr = params.get("apply");

            edu.ug.dcit308.hospital.app.util.StudentParameterCalculator.StudentParameters sp =
                    edu.ug.dcit308.hospital.app.util.StudentParameterCalculator.calculate(idStr);

            if ("true".equalsIgnoreCase(applyStr)) {
                this.urgentShiftLimit = sp.urgentCapacity;
                this.normalShiftLimit = sp.normalCapacity;
                this.preemptionShiftLimit = sp.preemptionCapacity;
                this.dispatchCentre = new HospitalDispatchCentre(sp.normalCapacity, 20, this.activeResourcePool);
                populateShiftBatch();
            }

            String json = String.format(Locale.US,
                    "{\"indexNumber\":\"%s\",\"priorityWeight\":%d,\"hashTableSize\":%d,\"randomSeed\":%d,\"routePenalty\":%.2f,\"urgentCapacity\":%d,\"normalCapacity\":%d,\"preemptionCapacity\":%d}",
                    sp.indexNumber, sp.priorityWeight, sp.hashTableSize, sp.randomSeed, sp.routePenalty, sp.urgentCapacity, sp.normalCapacity, sp.preemptionCapacity);

            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Utilities (Using Custom Data Structures)
    // -------------------------------------------------------------------------

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMsg) {
        try {
            String json = String.format("{\"error\":\"%s\"}", escapeJson(errorMsg));
            sendJsonResponse(exchange, statusCode, json);
        } catch (Exception ignored) {
        }
    }

    private static CustomMap<String, String> parseQueryParams(URI uri) {
        CustomMap<String, String> map = new CustomMap<>();
        String query = uri.getQuery();
        if (query != null) {
            for (String pair : query.split("&")) {
                int eq = pair.indexOf('=');
                if (eq > 0) {
                    String key = URLDecoder.decode(pair.substring(0, eq), StandardCharsets.UTF_8);
                    String val = URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    private static String intArrayToJson(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }

    // -------------------------------------------------------------------------
    // Static Resource File Handler
    // -------------------------------------------------------------------------

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path) || path.isEmpty()) {
                path = "/index.html";
            }

            String resourcePath = "web" + path;
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);

            if (is == null) {
                String notFound = "404 Not Found: " + path;
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes());
                }
                return;
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) contentType = "text/html; charset=UTF-8";
            else if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".png")) contentType = "image/png";
            else if (path.endsWith(".svg")) contentType = "image/svg+xml";

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] temp = new byte[4096];
            int read;
            while ((read = is.read(temp)) != -1) {
                buffer.write(temp, 0, read);
            }
            byte[] bytes = buffer.toByteArray();

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
