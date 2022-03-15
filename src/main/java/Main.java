import ua.com.okonsergei.repository.db.LiquibaseMigration;
import ua.com.okonsergei.view.MainView;

public class Main {

    public static void main(String[] args) {
        LiquibaseMigration.runLiquibaseMigration();
        MainView mainView = new MainView();
        mainView.run();
    }
}
