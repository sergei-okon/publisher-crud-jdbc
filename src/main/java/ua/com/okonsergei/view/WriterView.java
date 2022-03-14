package ua.com.okonsergei.view;

import ua.com.okonsergei.controller.PostController;
import ua.com.okonsergei.controller.WriterController;
import ua.com.okonsergei.model.Message;
import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.model.Writer;

import java.util.*;
import java.util.stream.Collectors;

public class WriterView extends BaseView {

    private final WriterController writerController = new WriterController();
    private final PostController postController = new PostController();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    void create() {
        Writer writer = new Writer();

        System.out.println("Input Writer firstName");
        String firstName = scanner.next();

        System.out.println("Input Writer lastName");
        String lastName = scanner.next();

        System.out.println("Input Posts id via ' , ' ");
        scanner.nextLine();
        String post = scanner.nextLine();

        List<Post> posts = new ArrayList<>();

        String[] postIds = post.replace(" ", "").split(",");
        Set<String> uniquePostIds = Arrays.stream(postIds).collect(Collectors.toSet());

        for (String id : uniquePostIds) {
            Post byId = postController.findById(Long.valueOf(id));
            if (byId != null) {
                posts.add(byId);
            }
        }
        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        writer.setPosts(posts);

        writerController.save(writer);
    }

    @Override
    void edit() {
        System.out.println("Editing Writer... Input id ");
        Long id;
        while (!scanner.hasNextLong()) {
            System.out.println("That not correct id");
            scanner.next();
        }

        id = scanner.nextLong();
        if (writerController.findById(id).getId() == null) {
            System.out.println("Writer with id " + id + "not found");
            return;
        }
        System.out.println("Input Writer new first name");
        String firstName = scanner.next();

        System.out.println("Input Writer new last Name");
        String lastName = scanner.next();

        System.out.println("Input Posts id via ' , ' ");
        scanner.nextLine();
        String post = scanner.nextLine();
        List<Post> posts = new ArrayList<>();

        String[] postArray = post.split(",");

        for (String element : postArray) {
            posts.add(postController.findById(Long.valueOf(element)));
        }
        Writer updateWriter = new Writer();

        updateWriter.setId(id);
        updateWriter.setFirstName(firstName);
        updateWriter.setLastName(lastName);
        updateWriter.setPosts(posts);

        writerController.update(id, updateWriter);
        System.out.println(Message.SUCCESSFUL_OPERATION.getMessage());
    }

    @Override
    void delete() {
        System.out.println("Deleting Writer. Input Id ...");
        Long id;
        while (!scanner.hasNextLong()) {
            System.out.println("That not correct id");
            scanner.next();
        }
        id = Long.valueOf(scanner.next());
        writerController.deleteById(id);
    }

    @Override
    void findAll() {
        System.out.println("Writers List");
        writerController.findAll().forEach(System.out::println);
    }

    @Override
    void showSecondMenu() {
        System.out.println("WRITER control menu. What do you want to do?");
        super.showSecondMenu();
    }
}
