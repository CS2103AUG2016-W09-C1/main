package seedu.oneline.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.oneline.model.Model;
import seedu.oneline.model.TaskBook;
import seedu.oneline.model.tag.Tag;
import seedu.oneline.model.tag.UniqueTagList;
import seedu.oneline.model.task.Task;
import seedu.oneline.model.task.TaskName;
import seedu.oneline.model.task.TaskTime;

/**
 * A utility class to generate test data.
 */
public class TestDataHelper{

//    public static Task myTask() throws Exception {
//        TaskName name = new TaskName("Do seagull stuff");
//        TaskTime startTime = new TaskTime("Sun Oct 16 21:35:45");
//        TaskTime endTime = new TaskTime("Mon Oct 17 21:35:45");
//        TaskTime deadline = new TaskTime("Sun Oct 23 21:35:45");
//        Tag tag = Tag.getTag("tag1");
//        return new Task(name, startTime, endTime, deadline, tag, false);
//    }
//
//    /**
//     * Generates a valid task using the given seed.
//     * Running this function with the same parameter values guarantees the returned task will have the same state.
//     * Each unique seed will generate a unique Task object.
//     *
//     * @param seed used to generate the task data field values
//     */
//    public static Task generateTask(int seed) throws Exception {
//        return new Task(
//                new TaskName("Task " + seed),
//                new TaskTime("" + Math.abs(seed)),
//                new TaskTime("" + seed),
//                new TaskTime("" + seed),
//                Tag.getTag("tag" + Math.abs(seed)),
//                false
//        );
//    }
//
//    /** Generates the correct add command based on the task given */
//    public static String generateAddCommand(Task p) {
//        StringBuffer cmd = new StringBuffer();
//
//        cmd.append("add ");
//
//        cmd.append(p.getName().toString());
//        cmd.append(" .from ").append(p.getStartTime());
//        cmd.append(" .to ").append(p.getEndTime());
//        cmd.append(" .due ").append(p.getDeadline());
//        cmd.append(" #").append(p.getTag().getTagName());
//
//        return cmd.toString();
//    }
//
//    /**
//     * Generates an TaskBook with auto-generated tasks.
//     */
//    public static TaskBook generateTaskBook(int numGenerated) throws Exception {
//        TaskBook taskBook = new TaskBook();
//        addToTaskBook(taskBook, numGenerated);
//        return taskBook;
//    }
//
//    /**
//     * Generates an AddressBook based on the list of Tasks given.
//     */
//    public static TaskBook generateTaskBook(List<Task> tasks) throws Exception {
//        TaskBook taskBook = new TaskBook();
//        addToTaskBook(taskBook, tasks);
//        return taskBook;
//    }
//
//    /**
//     * Adds auto-generated Task objects to the given Task Book
//     * @param taskBook The Task Book to which the Tasks will be added
//     */
//    public static void addToTaskBook(TaskBook taskBook, int numGenerated) throws Exception {
//        addToTaskBook(taskBook, generateTaskList(numGenerated));
//    }
//
//    /**
//     * Adds the given list of Tasks to the given Task Book
//     */
//    public static void addToTaskBook(TaskBook taskBook, List<Task> tasksToAdd) throws Exception {
//        for(Task p: tasksToAdd){
//            taskBook.addTask(p);
//        }
//    }
//
//    /**
//     * Adds auto-generated Task objects to the given model
//     * @param model The model to which the Tasks will be added
//     */
//    public static void addToModel(Model model, int numGenerated) throws Exception {
//        addToModel(model, generateTaskList(numGenerated));
//    }
//
//    /**
//     * Adds the given list of Tasks to the given model
//     */
//    public static void addToModel(Model model, List<Task> tasksToAdd) throws Exception {
//        for(Task p: tasksToAdd){
//            model.addTask(p);
//        }
//    }
//
//    /**
//     * Generates a list of Tasks based on the flags.
//     */
//    public static List<Task> generateTaskList(int numGenerated) throws Exception {
//        List<Task> tasks = new ArrayList<>();
//        for(int i = 1; i <= numGenerated; i++){
//            tasks.add(generateTask(i));
//        }
//        return tasks;
//    }
//
//    public static List<Task> generateTaskList(Task... tasks) {
//        return Arrays.asList(tasks);
//    }

    /**
     * Generates a Task object with given name. Other fields will have some dummy values.
     */
    public static Task generateTaskWithName(String name) throws Exception {
        return new Task(
                new TaskName(name),
                new TaskTime(""),
                new TaskTime(""),
                new TaskTime(""),
                Tag.getTag("tag"),
                false
        );
    }
}