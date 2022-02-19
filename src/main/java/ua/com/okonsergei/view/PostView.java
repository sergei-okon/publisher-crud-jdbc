package ua.com.okonsergei.view;

import ua.com.okonsergei.controller.LabelController;
import ua.com.okonsergei.controller.PostController;
import ua.com.okonsergei.model.Label;
import ua.com.okonsergei.model.Message;
import ua.com.okonsergei.model.Post;
import ua.com.okonsergei.utils.ConverterLocalDateTimeToLong;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PostView extends BaseView {

    private final PostController postController = new PostController();
    private final LabelController labelController = new LabelController();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    void create() {
        Post post = new Post();

        System.out.println("Input POST content");
        String content = scanner.nextLine();

        System.out.println("Input Label id via ' , ' ");
        String label = scanner.nextLine();

        List<Label> labels = new ArrayList<>();

        String[] labelIds = label.replace(" ", "").split(",");
        Set<String> uniqueLabelsIds = Arrays.stream(labelIds).collect(Collectors.toSet());

        for (String id : uniqueLabelsIds) {
            Label byId = labelController.findById(Long.valueOf(id));
            if (byId != null) {
                labels.add(byId);
            }
        }

        post.setContent(content);
        post.setCreated(ConverterLocalDateTimeToLong.convertLocalDateTimeToLong(LocalDateTime.now()));
        post.setUpdated(ConverterLocalDateTimeToLong.convertLocalDateTimeToLong(LocalDateTime.now()));
        post.setLabels(labels);

        postController.save(post);
        System.out.println(Message.SUCCESSFUL_OPERATION.getMessage());
    }

    @Override
    void edit() {
        System.out.println("Editing POST... Input id ");
        Long id;
        while (!scanner.hasNextLong()) {
            System.out.println("That not correct id");
            scanner.next();
        }
        id = scanner.nextLong();
        if (postController.findById(id) == null) {
            System.out.println("Post with id " + id + "not found");
            return;
        }

        System.out.println("Input POST new content");
        scanner.nextLine();
        String content = scanner.nextLine();

        System.out.println("Input Label id via ' , ' ");
        String label = scanner.next();

        List<Label> labels = new ArrayList<>();

        String[] postArray = label.split(",");

        for (String s : postArray) {
            labels.add(labelController.findById(Long.valueOf(s)));
        }
        Post updatePost = new Post();
        updatePost.setId(id);
        updatePost.setContent(content);
        updatePost.setUpdated(ConverterLocalDateTimeToLong.convertLocalDateTimeToLong(LocalDateTime.now()));
        updatePost.setLabels(labels);

        postController.update(id, updatePost);
        System.out.println(Message.SUCCESSFUL_OPERATION.getMessage());
    }

    @Override
    void delete() {
        System.out.println("Deleting POST. Input Id");
        Long id = Long.valueOf(scanner.next());
        postController.deleteById(id);
        System.out.println(Message.SUCCESSFUL_OPERATION.getMessage());
    }

    @Override
    void findAll() {
        System.out.println("Post List");
        postController.findAll().forEach(System.out::println);
    }

    public void showSecondMenu() {
        System.out.println("Post control menu. What do you want to do?");
        System.out.println(Message.SECOND_MENU.getMessage());
        super.showSecondMenu();
    }
}
