package ua.com.okonsergei.view;

import ua.com.okonsergei.controller.LabelController;
import ua.com.okonsergei.controller.PostController;
import ua.com.okonsergei.controller.WriterController;
import ua.com.okonsergei.model.Message;
import ua.com.okonsergei.repository.LabelRepository;
import ua.com.okonsergei.repository.PostRepository;
import ua.com.okonsergei.repository.WriterRepository;
import ua.com.okonsergei.repository.jdbc.JdbcLabelRepositoryImpl;
import ua.com.okonsergei.repository.jdbc.JdbcPostRepositoryImpl;
import ua.com.okonsergei.repository.jdbc.JdbcWriterRepositoryImpl;
import ua.com.okonsergei.service.LabelService;
import ua.com.okonsergei.service.PostService;
import ua.com.okonsergei.service.WriterService;
import ua.com.okonsergei.utils.LiquibaseMigration;

import java.util.Scanner;

public class MainView {

    private final PostRepository postRepository = new JdbcPostRepositoryImpl();
    private final WriterRepository writerRepository = new JdbcWriterRepositoryImpl();
    private final LabelRepository labelRepository = new JdbcLabelRepositoryImpl();

    private final WriterService writerService = new WriterService(writerRepository);
    private final PostService postService = new PostService(postRepository);
    private final LabelService labelService = new LabelService(labelRepository);

    private final WriterController writerController = new WriterController(writerService);
    private final PostController postController = new PostController(postService);
    private final LabelController labelController = new LabelController(labelService);

    private final WriterView writerView = new WriterView(writerController, postController);
    private final LabelView labelView = new LabelView(labelController);
    private final PostView postView = new PostView(postController, labelController);

    private final Scanner scanner = new Scanner(System.in);

    public void run() {

        LiquibaseMigration.runLiquibaseMigration();
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



