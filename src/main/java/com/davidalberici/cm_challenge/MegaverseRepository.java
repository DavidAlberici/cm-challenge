package com.davidalberici.cm_challenge;

public interface MegaverseRepository {

//    // Polyanets
//    void addPolyanet(int row, int column);
//    void deletePolyanet(int row, int column);
//
//    // Soloons
//    void addSoloon(int row, int column, SoloonColor color);
//    void deleteSoloon(int row, int column);
//
//    // Comeths
//    void addCometh(int row, int column, Direction direction);
//    void deleteCometh(int row, int column);

    // Megaverse
    Megaverse getCurrentMegaverse();
    Megaverse getGoalMegaverse();

//    // helper enums
//    enum SoloonColor { BLUE, RED, PURPLE, WHITE }
//    enum Direction   { UP, DOWN, LEFT, RIGHT }
}
