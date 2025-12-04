package prep.self_managed_tasks;

public class MessagePrinter {

    synchronized void printMessage(String message) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "sends this message: " + message);
        Thread.sleep(1000);
    }
}
