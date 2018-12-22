package tech.wienner;

public class ConsoleView implements View {
    @Override
    public void display(Message message) {
        System.out.println(message.toString());
    }
}
