package prep.self_managed_tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class MainOrchestrator {


    public static void main(String[] args) {

        MessagePrinter messagePrinter = new MessagePrinter();
        try {
            List<String> tasks = Files.readAllLines(Path.of(""));


            for (String task : tasks) {

                String[] taskStructure = task.split(",");
                PrintMessagesTask printMessagesTask = new PrintMessagesTask(taskStructure[0], Integer.valueOf(taskStructure[1]), Arrays.stream(taskStructure[2].split(";")).toList(), messagePrinter);
                printMessagesTask.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
