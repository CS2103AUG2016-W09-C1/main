package guitests;

import org.junit.Test;

import seedu.oneline.commons.core.Messages;
import seedu.oneline.commons.exceptions.IllegalValueException;
import seedu.oneline.logic.commands.CommandConstants;
import seedu.oneline.logic.commands.EditCommand;
import seedu.oneline.logic.parser.Parser;
import seedu.oneline.model.tag.Tag;
import seedu.oneline.model.tag.UniqueTagList;
import seedu.oneline.model.task.TaskField;
import seedu.oneline.model.task.TaskName;
import seedu.oneline.model.task.TaskRecurrence;
import seedu.oneline.model.task.TaskTime;
import seedu.oneline.testutil.TestTask;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EditCommandTest extends TaskBookGuiTest {

    @Test
    public void edit() {
        //edit one task
        TestTask[] currentList = td.getTypicalTasks();
        Map<TaskField, String> fields = new HashMap<TaskField, String>();
        fields.put(TaskField.NAME, "New Task");
        fields.put(TaskField.START_TIME, "Mon Oct 17 21:35:45 SGT 2016");
        fields.put(TaskField.END_TIME, "Tue Oct 18 21:35:45 SGT 2016");
        fields.put(TaskField.DEADLINE, "Mon Oct 24 21:35:45 SGT 2016");
        fields.put(TaskField.RECURRENCE, "Monday");
        fields.put(TaskField.TAG_ARGUMENTS, "#Tag3 #Tag4");
        assertEditSuccess(2, fields, currentList);

        //invalid command
        commandBox.runCommand("edits Task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertEditSuccess(int index, Map<TaskField, String> fields, TestTask... currentList) {
        TestTask[] expectedRemainder = currentList.clone();
        assert 0 <= index && index < expectedRemainder.length;
        StringBuilder cmd = new StringBuilder();
        cmd.append("edit ").append(index);
        TestTask newTask = new TestTask(expectedRemainder[index - 1]);
        try {
            if (fields.containsKey(TaskField.NAME)) {
                String newName = fields.get(TaskField.NAME);
                cmd.append(" ").append(newName);
                newTask.setName(new TaskName(newName));
            }
            if (fields.containsKey(TaskField.START_TIME)) {
                String newStartTime = fields.get(TaskField.START_TIME);
                cmd.append(" ")
                    .append(CommandConstants.KEYWORD_PREFIX)
                    .append(CommandConstants.KEYWORD_START_TIME)
                    .append(" ")
                    .append(newStartTime);
                newTask.setStartTime(new TaskTime(newStartTime));
            }
            if (fields.containsKey(TaskField.END_TIME)) {
                String newEndTime = fields.get(TaskField.END_TIME);
                cmd.append(" ")
                    .append(CommandConstants.KEYWORD_PREFIX)
                    .append(CommandConstants.KEYWORD_END_TIME)
                    .append(" ")
                    .append(newEndTime);
                newTask.setEndTime(new TaskTime(newEndTime));
            }
            if (fields.containsKey(TaskField.DEADLINE)) {
                String newDeadline = fields.get(TaskField.DEADLINE);
                cmd.append(" ")
                    .append(CommandConstants.KEYWORD_PREFIX)
                    .append(CommandConstants.KEYWORD_DEADLINE)
                    .append(" ")
                    .append(newDeadline);
                newTask.setDeadline(new TaskTime(newDeadline));
            }
            if (fields.containsKey(TaskField.RECURRENCE)) {
                String newRecurrence = fields.get(TaskField.RECURRENCE);
                cmd.append(" ")
                    .append(CommandConstants.KEYWORD_PREFIX)
                    .append(CommandConstants.KEYWORD_RECURRENCE)
                    .append(" ")
                    .append(newRecurrence);
                newTask.setRecurrence(new TaskRecurrence(newRecurrence));
            }
            if (fields.containsKey(TaskField.TAG_ARGUMENTS)) {
                String newTagsArgs = fields.get(TaskField.TAG_ARGUMENTS);
                Set<String> tagsToAdd = Parser.getTagsFromArgs(newTagsArgs);
                UniqueTagList newTags = new UniqueTagList();
                for (String tag : tagsToAdd) {
                    cmd.append(" ").append(CommandConstants.TAG_PREFIX).append(tag);
                    newTags.add(new Tag(tag));
                }
                newTask.getTags().setTags(newTags);
            }
        } catch (IllegalValueException e) {
            assert false : "Invalid input";
        }
        expectedRemainder[index - 1] = newTask;
        commandBox.runCommand(cmd.toString());

        assertTrue(taskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(EditCommand.MESSAGE_SUCCESS, newTask.toString()));
    }

}
