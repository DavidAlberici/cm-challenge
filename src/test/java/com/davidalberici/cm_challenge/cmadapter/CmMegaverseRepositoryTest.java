package com.davidalberici.cm_challenge.cmadapter;

import com.davidalberici.cm_challenge.Megaverse;
import com.davidalberici.cm_challenge.element.Cometh;
import com.davidalberici.cm_challenge.element.Element;
import com.davidalberici.cm_challenge.element.Polyanet;
import com.davidalberici.cm_challenge.element.Soloon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CmMegaverseRepositoryTest {

    private HttpClient httpClient;
    private String candidateId;

    private CmMegaverseRepository repository;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        candidateId = "any-id-will-work-here";
        repository = new CmMegaverseRepository(httpClient, candidateId);
    }

    @Test
    void getCurrentMegaverse_shouldKeepArraySize() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getCurrentMegaverseHttpMockResponse());

        // act
        Megaverse m = repository.getCurrentMegaverse();

        // assert
        int expectedColumns = 6;
        int expectedRows = 8;
        assertEquals(expectedColumns, m.getElements().length);
        for (int i = 0; i < expectedColumns; i++) {
            assertEquals(expectedRows, m.getElements()[i].length);
        }
    }

    @ParameterizedTest
    @MethodSource("provideExpectedElementsInCurrentMetaverse")
    void getCurrentMegaverse_shouldMapAllElemetTypes(int row, int column, Element expectedElement) {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getCurrentMegaverseHttpMockResponse());

        // act
        Megaverse m = repository.getCurrentMegaverse();

        // assert
        assertEquals(expectedElement, m.getElements()[row][column]);
    }

    private static Stream<Arguments> provideExpectedElementsInCurrentMetaverse() {
        return Stream.of(
                Arguments.of(0,0, new Polyanet()),
                Arguments.of(2,1, new Polyanet()),
                Arguments.of(3,0, new Soloon(Soloon.Color.RED)),
                Arguments.of(3,1, new Soloon(Soloon.Color.BLUE)),
                Arguments.of(1,3, new Cometh(Cometh.Direction.UP)),
                Arguments.of(1,4, new Cometh(Cometh.Direction.LEFT)),
                Arguments.of(1,0, null),
                Arguments.of(2,0, null)
        );
    }

    @Test
    void getCurrentMegaverse_shouldThrowErrorWhenArrayIsNotRegular() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getInvalidCurrentMegaverseMockHttpResponse());

        // act & assert
        Exception e = assertThrows(Exception.class, () -> repository.getCurrentMegaverse());

        assertEquals("Inconsistent column count at row 1", e.getMessage());
    }

    @Test
    void getGoalMegaverse_shouldKeepArraySize() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId + "/goal")).thenReturn(getGoalMegaverseMockHttpResponse());

        // act
        Megaverse m = repository.getGoalMegaverse();

        // assert
        int expectedColumns = 3;
        int expectedRows = 3;
        assertEquals(expectedColumns, m.getElements().length);
        for (int i = 0; i < expectedColumns; i++) {
            assertEquals(expectedRows, m.getElements()[i].length);
        }
    }

    @ParameterizedTest
    @MethodSource("provideExpectedElementsInGoalMetaverse")
    void getGoalMegaverse_shouldMapAllElemetTypes(int row, int column, Element expectedElement) {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId + "/goal")).thenReturn(getGoalMegaverseMockHttpResponse());

        // act
        Megaverse m = repository.getGoalMegaverse();

        // assert
        assertEquals(expectedElement, m.getElements()[row][column]);
    }

    private static Stream<Arguments> provideExpectedElementsInGoalMetaverse() {
        return Stream.of(
                Arguments.of(0,0, new Cometh(Cometh.Direction.DOWN)),
                Arguments.of(0,1, null),
                Arguments.of(0,2, new Polyanet()),
                Arguments.of(1,0, new Cometh(Cometh.Direction.UP)),
                Arguments.of(1,1, new Soloon(Soloon.Color.RED)),
                Arguments.of(1,2, new Polyanet()),
                Arguments.of(2,2, new Soloon(Soloon.Color.PURPLE))
        );
    }

    @Test
    void getGoalMegaverse_shouldThrowErrorWhenArrayIsNotRegular() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId + "/goal")).thenReturn(getInvalidGoalMegaverseMockHttpResponse());

        // act & assert
        Exception e = assertThrows(Exception.class, () -> repository.getGoalMegaverse());

        assertEquals("Inconsistent column count at row 2", e.getMessage());
    }

    private String getGoalMegaverseMockHttpResponse() {
        return """
                {
                    "goal": [
                        [
                            "DOWN_COMETH",
                            "SPACE",
                            "POLYANET"
                        ],
                        [
                            "UP_COMETH",
                            "RED_SOLOON",
                            "POLYANET"
                        ],
                        [
                            "SPACE",
                            "SPACE",
                            "PURPLE_SOLOON"
                        ]
                    ]
                }
                """;
    }

    private String getInvalidGoalMegaverseMockHttpResponse() {
        return """
                {
                    "goal": [
                        [
                            "DOWN_COMETH",
                            "SPACE",
                            "POLYANET"
                        ],
                        [
                            "UP_COMETH",
                            "RED_SOLOON",
                            "POLYANET"
                        ],
                        [
                            "SPACE",
                            "SPACE"
                        ]
                    ]
                }
                """;
    }

    private String getCurrentMegaverseHttpMockResponse() {
        return """
                {
                    "map":{"_id":"68272737eaa8c821866f2a9e",
                    "content":[
                        [{"type":0},null,null,null,null,null,null,null],
                        [null,null,null,{"type":2,"direction":"up"},{"type":2,"direction":"left"},{"type":2,"direction":"down"},{"type":2,"direction":"right"},null],
                        [null,{"type":0},null,null,null,null,null,null],
                        [{"type":1,"color":"red"},{"type":1,"color":"blue"},{"type":1,"color":"purple"},{"type":1,"color":"white"},null,null,null,null],
                        [null,null,null,null,null,null,null,null],
                        [null,null,null,null,null,null,null,null]
                    ],
                    "candidateId":"b2f8b8be-5953-4d4a-a43d-6752db2b7088","phase":2,"__v":0}
                }
                """;
    }

    private String getInvalidCurrentMegaverseMockHttpResponse() {
        return """
                {
                    "map":{"_id":"68272737eaa8c821866f2a9e",
                    "content":[
                        [{"type":0},null,null,null,null,null,null],
                        [null,null,null,{"type":2,"direction":"up"},{"type":2,"direction":"left"},{"type":2,"direction":"down"},{"type":2,"direction":"right"},null],
                        [null,{"type":0},null,null,null,null,null,null],
                        [{"type":1,"color":"red"},{"type":1,"color":"blue"},{"type":1,"color":"purple"},{"type":1,"color":"white"},null,null,null,null],
                        [null,null,null,null,null,null,null,null],
                        [null,null,null,null,null,null,null,null]
                    ],
                    "candidateId":"b2f8b8be-5953-4d4a-a43d-6752db2b7088","phase":2,"__v":0}
                }
                """;
    }
}