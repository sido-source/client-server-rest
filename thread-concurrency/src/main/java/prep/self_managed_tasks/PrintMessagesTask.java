package prep.self_managed_tasks;

import java.util.List;

public class PrintMessagesTask extends Thread {

    MessagePrinter printer;
    List<String> messages;
    int freq;
    String name;

    public PrintMessagesTask(String name, int frequency, List<String> messages, MessagePrinter printer) {
        this.messages = messages;
        this.printer = printer;
        freq = frequency;
        this.name = name;
    }

    @Override
    void run() {

        while (!messages.isEmpty()) {
            String message = messages.get(0);
            try {
                printer.printMessage(message);
                messages.removeFirst();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
