package ua.com.okonsergei;

import ua.com.okonsergei.controller.LabelController;
import ua.com.okonsergei.controller.PostController;
import ua.com.okonsergei.controller.WriterController;
import ua.com.okonsergei.repository.LabelRepository;
import ua.com.okonsergei.repository.PostRepository;
import ua.com.okonsergei.repository.WriterRepository;
import ua.com.okonsergei.repository.db.LabelRepositoryImpl;
import ua.com.okonsergei.repository.db.LiquibaseMigration;
import ua.com.okonsergei.repository.db.PostRepositoryImpl;
import ua.com.okonsergei.repository.db.WriterRepositoryImpl;
import ua.com.okonsergei.service.LabelService;
import ua.com.okonsergei.service.PostService;
import ua.com.okonsergei.service.WriterService;
import ua.com.okonsergei.view.LabelView;
import ua.com.okonsergei.view.MainView;
import ua.com.okonsergei.view.PostView;
import ua.com.okonsergei.view.WriterView;

public class PublisherRunner {

    public static void main(String[] args) {
        PublisherRunner runner = new PublisherRunner();
        runner.run();
    }

    public void run() {
        PostRepository postRepository = new PostRepositoryImpl();
        WriterRepository writerRepository = new WriterRepositoryImpl();
        LabelRepository labelRepository = new LabelRepositoryImpl();

        WriterService writerService = new WriterService(writerRepository);
        PostService postService = new PostService(postRepository);
        LabelService labelService = new LabelService(labelRepository);

        WriterController writerController = new WriterController(writerService);
        PostController postController = new PostController(postService);
        LabelController labelController = new LabelController(labelService);

        WriterView writerView = new WriterView(writerController, postController);
        LabelView labelView = new LabelView(labelController);
        PostView postView = new PostView(postController, labelController);

        LiquibaseMigration.runLiquibaseMigration();
        MainView mainView = new MainView(writerView, labelView, postView);
        mainView.run();
    }
}
