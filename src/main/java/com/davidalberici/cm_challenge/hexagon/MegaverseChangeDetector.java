package com.davidalberici.cm_challenge.hexagon;

import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class MegaverseChangeDetector {
    private final MegaverseWriterRepository megaverseWriterRepository;
    private final MegaverseReaderRepository megaverseReaderRepository;

    public List<Runnable> detectRequiredChanges(Megaverse newMg) {
        Megaverse oldMg = megaverseReaderRepository.getCurrentMegaverse();
        checkBothMegaversesHaveTheSameSize(newMg, oldMg);

        List<Runnable> changes = new ArrayList<>();

        for (int row = 0; row < newMg.getElements().length; row++) {
            for (int col = 0; col < newMg.getElements()[row].length; col++) {
                Element newEl = newMg.getElements()[row][col];
                Element oldEl = oldMg.getElements()[row][col];
                if (!Objects.equals(newEl, oldEl)) {
                    addRequiredDeleteAndAddElementChangesToList(
                            changes, newEl, oldEl, row, col);
                }
            }
        }

        return changes;
    }

    private void checkBothMegaversesHaveTheSameSize(@NonNull Megaverse newMg, @NonNull Megaverse oldMg) {
        if (newMg.getElements().length != oldMg.getElements().length) {
            throw new IllegalArgumentException("Both megaverses must have the same number of rows.");
        }
        if (newMg.getElements()[0].length != oldMg.getElements()[0].length) {
            throw new IllegalArgumentException("Both megaverses must have the same number of columns.");
        }
    }

    private void addRequiredDeleteAndAddElementChangesToList(List<Runnable> changes, Element newEl, Element oldEl, int row, int col) {
        addOrderToDeleteElement(changes, oldEl, row, col);
        addOrderToWriteElement(changes, newEl, row, col);
    }

    private void addOrderToDeleteElement(List<Runnable> changes, Element oldEl, int row, int col) {
        if (oldEl == null) {
            return;
        }

        if (oldEl instanceof Polyanet) {
            changes.add(() -> megaverseWriterRepository.deletePolyanet(row, col));
        } else if (oldEl instanceof Soloon) {
            changes.add(() -> megaverseWriterRepository.deleteSoloon(row, col));
        } else if (oldEl instanceof Cometh) {
            changes.add(() -> megaverseWriterRepository.deleteCometh(row, col));
        } else {
            throw new RuntimeException("Error, the required instance of Element is not recognized: " + oldEl.getClass().getSimpleName());
        }
    }

    private void addOrderToWriteElement(List<Runnable> changes, Element newEl, int row, int col) {
        if (newEl == null) {
            return;
        }

        if (newEl instanceof Polyanet) {
            changes.add(() -> megaverseWriterRepository.addPolyanet(row, col));
        } else if (newEl instanceof Soloon soloon) {
            changes.add(() -> megaverseWriterRepository.addSoloon(row, col, soloon.getColor()));
        } else if (newEl instanceof Cometh cometh) {
            changes.add(() -> megaverseWriterRepository.addCometh(row, col, cometh.getDirection()));
        } else {
            throw new RuntimeException("Error, the required instance of Element is not recognized: " + newEl.getClass().getSimpleName());
        }
    }
}
