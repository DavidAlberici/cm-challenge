package com.davidalberici.cm_challenge.adapter.cmmegaversereader;

import com.davidalberici.cm_challenge.port.HttpClient;
import com.davidalberici.cm_challenge.hexagon.Megaverse;
import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class CmMegaverseReaderRepository implements MegaverseReaderRepository {
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
        Element[][] matrix = mapCurrentMegaverseResponseToElementArray(rawResponse);
        return new Megaverse(matrix);
    }

    @Override
    public Megaverse getGoalMegaverse() {
        String rawResponse = httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId + "/goal");
        Element[][] matrix = mapGoalMegaverseResponseToElementArray(rawResponse);
        return new Megaverse(matrix);
    }

    private Element[][] mapGoalMegaverseResponseToElementArray(String rawResponse) {
        JsonNode root = null;   // { "goal": [[],[],...] }
        try {
            root = objectMapper.readTree(rawResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode goal = root.path("goal");
        checkGoalMegaverseGoalNodeIsValid(goal);

        try {
            CmGoalElement[][] rawElementArray = objectMapper.treeToValue(goal, CmGoalElement[][].class);
            return mapCmGoalElementArrayToElementArray(rawElementArray);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkGoalMegaverseGoalNodeIsValid(JsonNode goal) {
        if (!goal.isArray()) {
            throw new IllegalArgumentException("'goal' is not an array");
        }

        int rows = goal.size();
        if (rows == 0) {
            throw new IllegalArgumentException("'goal' array is empty");
        }

        int cols = goal.get(0).size();
        if (cols == 0) {
            throw new IllegalArgumentException("'goal' array has rows, but they are empty");
        }
        for (int i = 1; i < rows; i++) {
            if (!goal.get(i).isArray()) {
                throw new IllegalArgumentException("Row " + i + " in 'goal' is not an array");
            }
            if (goal.get(i).size() != cols) {
                throw new IllegalArgumentException("Inconsistent column count at row " + i);
            }
        }
    }

    private Element[][] mapCmGoalElementArrayToElementArray(CmGoalElement[][] rawElementArray) {
        int rows = rawElementArray.length;
        int cols = rawElementArray[0].length;
        Element[][] elements = new Element[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CmGoalElement cell = rawElementArray[i][j];
                if (cell != null) {
                    elements[i][j] = mapCmGoalElementToElement(cell, i, j);
                }
            }
        }

        return elements;
    }

    private Element mapCmGoalElementToElement(CmGoalElement cell, int i, int j) {
        return switch (cell) {
            case SPACE -> null;
            case POLYANET -> new Polyanet();
            case UP_COMETH -> new Cometh(Cometh.Direction.UP);
            case DOWN_COMETH ->  new Cometh(Cometh.Direction.DOWN);
            case LEFT_COMETH ->  new Cometh(Cometh.Direction.LEFT);
            case RIGHT_COMETH ->  new Cometh(Cometh.Direction.RIGHT);
            case WHITE_SOLOON -> new Soloon(Soloon.Color.WHITE);
            case BLUE_SOLOON -> new Soloon(Soloon.Color.BLUE);
            case RED_SOLOON -> new Soloon(Soloon.Color.RED);
            case PURPLE_SOLOON -> new Soloon(Soloon.Color.PURPLE);
        };
    }

    private Element[][] mapCurrentMegaverseResponseToElementArray(String rawResponse) {
        JsonNode root = null;   // { "map": { "content": [...], ...} }
        try {
            root = objectMapper.readTree(rawResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode content = root.path("map").path("content");
        checkCurrentMegaverseContentNodeIsValid(content);

        try {
            CmCurrentElement[][] rawElementArray = objectMapper.treeToValue(content, CmCurrentElement[][].class);
            return mapCmCurrentElementArrayToElementArray(rawElementArray);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Element[][] mapCmCurrentElementArrayToElementArray(CmCurrentElement[][] rawElementArray) {
        int rows = rawElementArray.length;
        int cols = rawElementArray[0].length;
        Element[][] elements = new Element[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CmCurrentElement cell = rawElementArray[i][j];
                if (cell != null) {
                    elements[i][j] = mapCmCurrentElementToElement(cell, i, j);
                }
            }
        }

        return elements;
    }

    private static Element mapCmCurrentElementToElement(CmCurrentElement cell, int i, int j) {
        int type = cell.type();
        return switch (type) {
            case 0 -> new Polyanet();
            case 1 -> mapRawArrayCellToSoloon(cell, i, j);
            case 2 -> mapRawArrayCellToCometh(cell, i, j);
            default -> throw new IllegalArgumentException("Unknown type '" + type + "' at (" + i + "," + j + ")");
        };
    }

    private static Soloon mapRawArrayCellToSoloon(CmCurrentElement cell, int i, int j) {
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

    private static Cometh mapRawArrayCellToCometh(CmCurrentElement cell, int i, int j) {
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

    private void checkCurrentMegaverseContentNodeIsValid(JsonNode content) {
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
