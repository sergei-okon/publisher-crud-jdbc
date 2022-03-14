import ua.com.okonsergei.repository.db.LiquibaseMigration;
import ua.com.okonsergei.view.MainView;

public class Main {

    public static void main(String[] args) {
//        System.out.println("___________________________________________________");
//        PostRepositoryImpl jsonPostRepository = new PostRepositoryImpl();
//
//        System.out.println(jsonPostRepository.findById(1L));
//        System.out.println(jsonPostRepository.findById(2L));
//        System.out.println("___________________________________________________");

//        LabelRepositoryImpl labelRepository=new LabelRepositoryImpl();
//        System.out.println(labelRepository.findById(1L));
//        System.out.println(labelRepository.findById(2L));
//        System.out.println(labelRepository.findById(3L));

//        WriterRepository writerRepository=new JsonWriterRepositoryImpl();
//        writerRepository.findById(2L);

        LiquibaseMigration.runLiquibaseMigration();
        MainView mainView = new MainView();
        mainView.run();
    }
}
