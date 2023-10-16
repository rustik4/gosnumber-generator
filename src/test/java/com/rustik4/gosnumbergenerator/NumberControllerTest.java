package com.rustik4.gosnumbergenerator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NumberControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testNextPattern() {
        String result = makeNextRequest();
        Assertions.assertTrue(result.matches(GosNumber.PATTERN.pattern()));
    }

    @Test
    public void testNextOrder() throws GosNumberGeneratorException {
        String current = makeNextRequest();
        GosNumber currentGosNumber = new GosNumber(current);
        currentGosNumber = currentGosNumber.next();
        current = makeNextRequest();
        Assertions.assertEquals(currentGosNumber, new GosNumber(current));
        currentGosNumber = currentGosNumber.next();
        current = makeNextRequest();
        Assertions.assertEquals(currentGosNumber, new GosNumber(current));
    }

    @Test
    public void testFixedOrder1() {
        String testValue = "С399ВА 116 RUS";
        makeSetCurrentRequest(testValue);
        String current = makeNextRequest();
        Assertions.assertEquals(current, testValue);
        String next = makeNextRequest();
        Assertions.assertEquals(next, "С400ВА 116 RUS");
    }

    @Test
    public void testFixedOrder2() {
        String testValue = "С999ВА 116 RUS";
        makeSetCurrentRequest(testValue);
        String current = makeNextRequest();
        Assertions.assertEquals(current, testValue);
        String next = makeNextRequest();
        Assertions.assertEquals(next, "С000ВВ 116 RUS");
    }

    @Test
    public void testRandomPattern() {
        String result = makeRandomRequest();
        Assertions.assertTrue(result.matches(GosNumber.PATTERN.pattern()));
        makeClearRequest();
    }

    @Test
    public void testRandomFullRepository() {
        makeSetCurrentRequest("Х996ХХ 116 RUS");
        String result = makeRandomRequest();
        System.out.println(result);
        result = makeRandomRequest();
        System.out.println(result);
        result = makeRandomRequest();
        System.out.println(result);
        boolean hasNext = makeHasNextRequest();
        Assertions.assertTrue(hasNext);
        result = makeRandomRequest();
        System.out.println(result);
        hasNext = makeHasNextRequest();
        Assertions.assertFalse(hasNext);
        makeClearRequest();
    }

    private String makeNextRequest() {
        return makeRequest("next");
    }

    private String makeRandomRequest() {
        return makeRequest("random");
    }

    private void makeSetCurrentRequest(String current) {
        restTemplate.postForObject(
                String.format("http://localhost:%d/number/setCurrent", port), current, String.class);
    }

    private boolean makeHasNextRequest() {
        String hasNext = makeRequest("hasNext");
        return Boolean.parseBoolean(hasNext);
    }

    private void makeClearRequest() {
        makeRequest("clear");
    }

    private String makeRequest(String requestType) {
        return restTemplate.getForObject(
                String.format("http://localhost:%d/number/%s", port, requestType), String.class);
    }
}
