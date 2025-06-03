package com.davidalberici.cm_challenge.cmadapter;

import com.davidalberici.cm_challenge.Megaverse;
import com.davidalberici.cm_challenge.element.Cometh;
import com.davidalberici.cm_challenge.element.Polyanet;
import com.davidalberici.cm_challenge.element.Soloon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void getCurrentMegaverse_shouldMapPolyanets() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getCurrentMegaverseHttpMockResponse());

        // act
        Megaverse m = repository.getCurrentMegaverse();

        // assert
        assertTrue(m.getElements()[0][0] instanceof Polyanet);
        assertTrue(m.getElements()[2][1] instanceof Polyanet);
    }

    @Test
    void getCurrentMegaverse_shouldMapSoloons() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getCurrentMegaverseHttpMockResponse());

        // act
        Megaverse m = repository.getCurrentMegaverse();

        // assert
        assertTrue(m.getElements()[3][0] instanceof Soloon);
        assertEquals(Soloon.Color.RED, ((Soloon) m.getElements()[3][0]).getColor());
        assertTrue(m.getElements()[3][1] instanceof Soloon);
        assertEquals(Soloon.Color.BLUE, ((Soloon) m.getElements()[3][1]).getColor());
    }

    @Test
    void getCurrentMegaverse_shouldMapComeths() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getCurrentMegaverseHttpMockResponse());

        // act
        Megaverse m = repository.getCurrentMegaverse();

        // assert
        assertTrue(m.getElements()[1][3] instanceof Cometh);
        assertEquals(Cometh.Direction.UP, ((Cometh) m.getElements()[1][3]).getDirection());
        assertTrue(m.getElements()[1][4] instanceof Cometh);
        assertEquals(Cometh.Direction.LEFT, ((Cometh) m.getElements()[1][4]).getDirection());
    }

    @Test
    void getCurrentMegaverse_shouldThrowErrorWhenArrayIsNotRegular() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getInvalidCurrentMegaverseMockHttpResponse());

        // act & assert
        Exception e = assertThrows(Exception.class, () -> repository.getCurrentMegaverse());

        assertEquals("Inconsistent column count at row 1", e.getMessage());
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