package seedu.oneline.testutil;

import seedu.oneline.commons.exceptions.IllegalValueException;
import seedu.oneline.model.TaskBook;
import seedu.oneline.model.task.*;

/**
 *
 */
public class TypicalTestTasks {  

    public static TestTask event1, event2, event3, eventExtra,
                            todo1, todo2, todo3, todoExtra,
                            float1, float2, float3, floatExtra;

    static {
        initTestTasks();
    }
    
    
    private static void initTestTasks() {
        try {
            event1 = new TaskBuilder().withName("Meeting with Harry").withStartTime("Sun Oct 16 21:35:45").withEndTime("Mon Oct 17 21:35:45").withTag("Tag1").build();
            event2 = new TaskBuilder().withName("Appointment with John").withStartTime("Sun Oct 16 21:35:45").withEndTime("Mon Oct 17 21:35:45").withTag("Tag2").build();
            event3 = new TaskBuilder().withName("Date with Girlfriend").withStartTime("Sun Oct 16 21:35:45").withEndTime("Mon Oct 17 21:35:45").withTag("Tag3").build();
            todo1 = new TaskBuilder().withName("Check email").withDeadline("Sun Oct 23 21:35:45").withTag("Tag2").build();
            todo2 = new TaskBuilder().withName("Consolidate EOY reviews").withDeadline("Sun Oct 23 21:35:45").withTag("Tag3").build();
            todo3 = new TaskBuilder().withName("Purchase new stock of cases").withDeadline("Sun Oct 23 21:35:45").withTag("Tag3").build();
            float1 = new TaskBuilder().withName("Consolidate reports").withTag("Tag2").build();
            float2 = new TaskBuilder().withName("Gym").withTag("Tag4").build();
            float3 = new TaskBuilder().withName("Watch Fixing Good").withTag("Tag5").build();
            
            // Extra for manual addition
            eventExtra = new TaskBuilder().withName("Shopping trip").withStartTime("Sun Oct 16 21:35:45").withEndTime("Mon Oct 17 21:35:45").withTag("Tag3").build();
            todoExtra = new TaskBuilder().withName("Name TE").withDeadline("Sun Oct 23 21:35:45").withTag("Tag5").build();
            floatExtra = new TaskBuilder().withName("Name FE").withTag("Tag6").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadTaskBookWithSampleData(TaskBook ab) {

        try {
            ab.addTask(new Task(event1));
            ab.addTask(new Task(event2));
            ab.addTask(new Task(event3));
            ab.addTask(new Task(todo1));
            ab.addTask(new Task(todo2));
            ab.addTask(new Task(todo3));
            ab.addTask(new Task(float1));
            ab.addTask(new Task(float2));
            ab.addTask(new Task(float3));
        } catch (IllegalValueException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        TestTask[] test = new TestTask[]{event1, event2, event3, todo1, todo2, todo3, float1, float2, float3};
        for (int i = 0; i < test.length; i++) {
            try {
                new Task(test[i]);
            } catch (IllegalValueException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                assert false;
            }
        }
        return new TestTask[]{event1, event2, event3, todo1, todo2, todo3, float1, float2, float3};
    }

    public TaskBook getTypicalTaskBook(){
        TaskBook ab = new TaskBook();
        loadTaskBookWithSampleData(ab);
        return ab;
    }
}