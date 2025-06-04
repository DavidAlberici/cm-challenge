package com.davidalberici.cm_challenge;

import com.davidalberici.cm_challenge.hexagon.Megaverse;
import com.davidalberici.cm_challenge.hexagon.element.Cometh;
import com.davidalberici.cm_challenge.hexagon.element.Element;
import com.davidalberici.cm_challenge.hexagon.element.Polyanet;
import com.davidalberici.cm_challenge.hexagon.element.Soloon;
import lombok.NonNull;

/**
 * Utility class for printing in Console the contents of a Megaverse. Each element is 2 characters wide, and there
 * is one space between them.
 */
public class MegaversePrinter {
    public static void printMegaverse(@NonNull Megaverse megaverse) {
        if (megaverse.getElements() == null || megaverse.getElements().length == 0 || megaverse.getElements()[0].length == 0) {
            System.out.println("The megaverse is empty.");
            return;
        }

        System.out.println(megaverse + " contents:");
        for (int row = 0; row < megaverse.getElements().length; row++) {
            System.out.print("\t");
            for (int col = 0; col < megaverse.getElements()[row].length; col++) {
                Element e = megaverse.getElements()[row][col];
                if (e == null) {
                    System.out.print("o  ");
                } else if (e instanceof Polyanet){
                    System.out.print("PO ");
                } else if (e instanceof Soloon soloon) {
                    System.out.print("S" + soloon.getColor().toString().charAt(0) + " ");
                } else if (e instanceof Cometh cometh) {
                    System.out.print("C" + cometh.getDirection().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }
}
