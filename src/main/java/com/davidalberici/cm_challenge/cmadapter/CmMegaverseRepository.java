package com.davidalberici.cm_challenge.cmadapter;

import com.davidalberici.cm_challenge.Megaverse;
import com.davidalberici.cm_challenge.MegaverseRepository;
import com.davidalberici.cm_challenge.element.Cometh;
import com.davidalberici.cm_challenge.element.Element;
import com.davidalberici.cm_challenge.element.Polyanet;
import com.davidalberici.cm_challenge.element.Soloon;
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
        Element[][] matrix = mapResponseToElementArray(rawResponse);
        return new Megaverse(matrix);
    }

    @Override
    public Megaverse getGoalMegaverse() {
        return null;
    }

    private Element[][] mapResponseToElementArray(String rawResponse) {
        JsonNode root = null;   // { "map": { "content": [...], ...} }
        try {
            root = objectMapper.readTree(rawResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode content = root.path("map").path("content");
        checkContentNodeIsValid(content);

        try {
            CmElement[][] rawElementArray = objectMapper.treeToValue(content, CmElement[][].class);
            return mapRawArrayToElementArray(rawElementArray);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Element[][] mapRawArrayToElementArray(CmElement[][] rawElementArray) {
        int rows = rawElementArray.length;
        int cols = rawElementArray[0].length;
        Element[][] elements = new Element[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CmElement cell = rawElementArray[i][j];
                if (cell != null) {
                    elements[i][j] = mapRawArrrayCellToElement(cell, i, j);
                }
            }
        }

        return elements;
    }

    private static Element mapRawArrrayCellToElement(CmElement cell, int i, int j) {
        int type = cell.type();
        return switch (type) {
            case 0 -> new Polyanet();
            case 1 -> mapRawArrayCellToSoloon(cell, i, j);
            case 2 -> mapRawArrayCellToCometh(cell, i, j);
            default -> throw new IllegalArgumentException("Unknown type '" + type + "' at (" + i + "," + j + ")");
        };
    }

    private static Soloon mapRawArrayCellToSoloon(CmElement cell, int i, int j) {
        Object rawColor = cell.color();
        if (rawColor == null) {
            throw new IllegalArgumentException("Missing 'color' for Soloon at (" + i + "," + j + ")");
        }
        String colorStr = rawColor.toString().toUpperCase();
        try {
            Soloon.Color color = Soloon.Color.valueOf(colorStr);
            return new Soloon(color);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Soloon color '" + colorStr + "' at (" + i + "," + j + ")");
        }
    }

    private static Cometh mapRawArrayCellToCometh(CmElement cell, int i, int j) {
        Object rawDir = cell.direction();
        if (rawDir == null) {
            throw new IllegalArgumentException("Missing 'direction' for Cometh at (" + i + "," + j + ")");
        }
        String dirString = rawDir.toString().toUpperCase();
        try {
            Cometh.Direction dir = Cometh.Direction.valueOf(dirString);
            return new Cometh(dir);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Cometh direction '" + dirString + "' at (" + i + "," + j + ")");
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
        if (cols == 0) {
            throw new IllegalArgumentException("'content' array has rows, but they are empty");
        }
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
