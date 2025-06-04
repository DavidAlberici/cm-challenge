package com.davidalberici.cm_challenge.adapter.cmmegaversewriter;

import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import com.davidalberici.cm_challenge.port.HttpClient;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class is simple, just converts the calls to HTTP requests, following the API docs
 */
@RequiredArgsConstructor
public class CmMegaverseWriterRepository implements MegaverseWriterRepository {

    private final HttpClient httpClient;
    private final String candidateId;

    @Override
    public void addPolyanet(int row, int column) {
        String url = "https://challenge.crossmint.io/api/polyanets";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.post(url, jsonBody);
    }

    @Override
    public void deletePolyanet(int row, int column) {
        String url = "https://challenge.crossmint.io/api/polyanets";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.delete(url, jsonBody);
    }

    @Override
    public void addSoloon(int row, int column, Soloon.Color color) {
        String url = "https://challenge.crossmint.io/api/soloons";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.post(url, jsonBody);
    }

    @Override
    public void deleteSoloon(int row, int column) {
        String url = "https://challenge.crossmint.io/api/soloons";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.delete(url, jsonBody);
    }

    @Override
    public void addCometh(int row, int column, Cometh.Direction direction) {
        String url = "https://challenge.crossmint.io/api/comeths";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.post(url, jsonBody);
    }

    @Override
    public void deleteCometh(int row, int column) {
        String url = "https://challenge.crossmint.io/api/comeths";

        String jsonBody = String.format(
                "{\"row\":%d,\"column\":%d,\"candidateId\":\"%s\"}",
                row, column, candidateId
        );

        httpClient.delete(url, jsonBody);
    }
}
