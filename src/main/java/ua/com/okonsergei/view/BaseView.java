package ua.com.okonsergei.view;

import ua.com.okonsergei.model.Message;

import java.util.Scanner;

public abstract class BaseView {

    private final Scanner scanner = new Scanner(System.in);

    abstract void create();

    abstract void edit();

    abstract void delete();

    abstract void findAll();

    void showSecondMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println(Message.LINE.getMessage());
            System.out.println(Message.SECOND_MENU.getMessage());
            System.out.println(Message.LINE.getMessage());
            String input = scanner.next();

            switch (input) {
                case "1" -> create();
                case "2" -> edit();
                case "3" -> delete();
                case "4" -> findAll();
                case "0" -> exit = true;
                default -> System.out.println(Message.ERROR_INPUT.getMessage());
            }
        }
    }
}
