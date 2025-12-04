package prep.self_managed_tasks;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MainOrchestrator {


    public static void main(String[] args) {

        MessagePrinter messagePrinter = new MessagePrinter();
        try (InputStream in = MainOrchestrator.class.getResourceAsStream("inputFile.txt")) {
                if (in == null) throw new FileNotFoundException("inputFile.txt not found");
                List<String> tasks = new BufferedReader(new InputStreamReader(in))
                        .lines().collect(Collectors.toList());


            //List<String> tasks = Files.readAllLines(path);
            //List<String> tasks = Files.readAllLines(Path.of(MainOrchestrator.class.getResource("inputFile.txt").toURI()));


            for (String task : tasks) {

                String[] taskStructure = task.split(",");
                PrintMessagesTask printMessagesTask = new PrintMessagesTask(taskStructure[0], Integer.valueOf(taskStructure[1]), new LinkedList<>(Arrays.stream(taskStructure[2].split(";")).toList()), messagePrinter);
                printMessagesTask.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
