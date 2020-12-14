package agh.cs.lab2;

import java.awt.*;

class Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Simulation().startSimulation();

            }
        });
    }
}