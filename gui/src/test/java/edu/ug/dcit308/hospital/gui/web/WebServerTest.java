package edu.ug.dcit308.hospital.gui.web;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebServerTest {

    private static WebServer server;
    private static final int TEST_PORT = 18081;
    private static HttpClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new WebServer(TEST_PORT);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testGetStaticIndexHtml() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("GHANA HEALTH SERVICE"));
    }

    @Test
    void testGetStaticCssAndJs() throws Exception {
        HttpRequest reqCss = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/css/style.css"))
                .GET()
                .build();
        HttpResponse<String> respCss = client.send(reqCss, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, respCss.statusCode());
        assertTrue(respCss.body().contains("@import"));

        HttpRequest reqJs = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/js/app.js"))
                .GET()
                .build();
        HttpResponse<String> respJs = client.send(reqJs, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, respJs.statusCode());
        assertTrue(respJs.body().contains("import"));
    }

    @Test
    void testGetHospitalsEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/hospitals"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("Korle Bu"));
    }

    @Test
    void testGetRoutesEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/routes"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("distanceKm"));
    }

    @Test
    void testDijkstraRouteEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/route/dijkstra?source=1&dest=16"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("totalDistanceKm"));
    }

    @Test
    void testMstEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/route/mst"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("kruskalTotalKm"));
    }

    @Test
    void testSearchEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/search?id=1"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("hashNs"));
    }

    @Test
    void testDispatchServeAndUndoEndpoint() throws Exception {
        HttpRequest serveReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/dispatch/serve"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> serveResp = client.send(serveReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, serveResp.statusCode());

        HttpRequest undoReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/dispatch/undo"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> undoResp = client.send(undoReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, undoResp.statusCode());
        assertTrue(undoResp.body().contains("success"));
    }

    @Test
    void testDispatchQueuesEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/dispatch/queues"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("undoStack"));
        assertTrue(resp.body().contains("normalQueue"));
        assertTrue(resp.body().contains("urgentQueue"));
    }

    @Test
    void testBatchDispatchEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/dispatch/batch?count=5"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("requests"));
    }

    @Test
    void testKnapsackOptimizerEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/optimizer/knapsack?capacity=35.0"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("totalValue"));
    }

    @Test
    void testStudentCalcEndpoint() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/api/student/calc?id=10987654"))
                .GET()
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("priorityWeight"));
    }
}
