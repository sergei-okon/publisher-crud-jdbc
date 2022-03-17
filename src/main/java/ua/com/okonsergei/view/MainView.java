package ua.com.okonsergei.view;

import ua.com.okonsergei.model.Message;

import java.util.Scanner;

public class MainView {

    private final WriterView writerView;
    private final LabelView labelView;
    private final PostView postView;

    public MainView(WriterView writerView, LabelView labelView, PostView postView) {
        this.writerView = writerView;
        this.labelView = labelView;
        this.postView = postView;
    }

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        String mainMenu = """
                MAIN MENU
                Make your choice:
                1 Writer
                2 Label
                3 Post
                ____________________________________________
                0 Exit""";

        boolean exit = false;
        while (!exit) {
            System.out.println(Message.LINE.getMessage() + "\n" + mainMenu);
            String input = scanner.next();

            switch (input) {
                case "1" -> writerView.showSecondMenu();
                case "2" -> labelView.showSecondMenu();
                case "3" -> postView.showSecondMenu();
                case "0" -> exit = true;
                default -> System.out.println(Message.ERROR_INPUT.getMessage());
            }
        }
    }
}



