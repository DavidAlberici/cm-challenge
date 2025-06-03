package com.davidalberici.cm_challenge.cmadapter;

import com.davidalberici.cm_challenge.Megaverse;
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
    void getCurrentMegaverse() {
        // arrange
        when(httpClient.get("https://challenge.crossmint.com/api/map/" + candidateId)).thenReturn(getMockCurrentMegaverseHttpResponse());

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

    private String getMockCurrentMegaverseHttpResponse() {
        return """
                {
                    "map":{"_id":"68272737eaa8c821866f2a9e",
                    "content":[
                        [null,null,null,null,null,null,null,null],
                        [null,{"type":1,"color":"red"},{"type":1,"color":"blue"},{"type":2,"direction":"up"},{"type":2,"direction":"left"},{"type":2,"direction":"down"},{"type":2,"direction":"right"},null],
                        [null,null,null,null,null,null,null,null],
                        [null,null,null,null,null,null,null,null],
                        [null,null,null,null,null,null,null,null],
                        [null,null,null,null,null,null,null,null]
                    ],
                    "candidateId":"b2f8b8be-5953-4d4a-a43d-6752db2b7088","phase":2,"__v":0}
                }
                """;
    }
}