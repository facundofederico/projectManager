package facundofederico.services;

import java.util.Scanner;

public class InputService {
    private final Scanner scanner = new Scanner(System.in);

    public String nextLine() {
        return scanner.nextLine();
    }
}
