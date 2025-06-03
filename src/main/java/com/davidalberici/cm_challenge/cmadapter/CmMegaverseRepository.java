package com.davidalberici.cm_challenge.cmadapter;

import com.davidalberici.cm_challenge.Megaverse;
import com.davidalberici.cm_challenge.MegaverseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class CmMegaverseRepository implements MegaverseRepository {
    // I am intentionally assuming objectMapper as part of the implementation, I am prioritizing speed of development over flexibility here.
    // Besides speed, I am doing it because you can already see I know how dependency injection works, by looking at HttpClient injection!
    // In a real project (if it was deemed worth it) I would have injected this dependency as well
    private final ObjectMapper objectMapper = new ObjectMapper();
    // As stated above, I am injecting HttpClient, specially because I do not want to do a real Http request for each test run
    private final HttpClient httpClient;
    private final String candidateId;

    @Override
    public Megaverse getCurrentMegaverse() {
        String rawResponse = httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId);
        Object[][] matrix = mapResponseToElementArray(rawResponse);
//        Element[][] els = convertStringMatrixToElementMatrix(rawMatrix);
        return new Megaverse(matrix);
    }

    @Override
    public Megaverse getGoalMegaverse() {
        return null;
    }

    private Object[][] mapResponseToElementArray(String rawResponse) {
        JsonNode root = null;   // { "map": { "content": [...], ...} }
        try {
            root = objectMapper.readTree(rawResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode content = root.path("map").path("content");
        checkContentNodeIsValid(content);

        try {
            return objectMapper.treeToValue(content, Object[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkContentNodeIsValid(JsonNode content) {
        if (!content.isArray()) {
            throw new IllegalArgumentException("'content' is not an array");
        }

        int rows = content.size();
        if (rows == 0) {
            throw new IllegalArgumentException("'content' array is empty");
        }

        int cols = content.get(0).size();
        for (int i = 1; i < rows; i++) {
            if (!content.get(i).isArray()) {
                throw new IllegalArgumentException("Row " + i + " in 'content' is not an array");
            }
            if (content.get(i).size() != cols) {
                throw new IllegalArgumentException("Inconsistent column count at row " + i);
            }
        }

    }
}
