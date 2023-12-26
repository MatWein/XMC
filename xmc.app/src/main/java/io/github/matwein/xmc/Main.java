package io.github.matwein.xmc;

/**
 * This Main class only calls the next Main2 class. Unfortunately this is neccessary to run a JavaFX application with pre-packaged dependencies by maven.
 */
public class Main {
    public static void main(String[] args) {
        Main2.main(args);
    }
}
