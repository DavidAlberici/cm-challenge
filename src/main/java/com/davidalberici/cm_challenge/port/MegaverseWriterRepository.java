package com.davidalberici.cm_challenge.port;

import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;

public interface MegaverseWriterRepository {
    // Polyanets
    void addPolyanet(int row, int column);
    void deletePolyanet(int row, int column);

    // Soloons
    void addSoloon(int row, int column, Soloon.Color color);
    void deleteSoloon(int row, int column);

    // Comeths
    void addCometh(int row, int column, Cometh.Direction direction);
    void deleteCometh(int row, int column);
}
