package guitests;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import seedu.oneline.commons.core.Messages;
import seedu.oneline.testutil.TestTask;
import seedu.oneline.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.oneline.logic.commands.DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS;

public class DeleteCommandTest extends TaskBookGuiTest {

    @Test
    public void deleteCommand_deleteTaskAtStart_success() {
        TestTask[] currentList = td.getTypicalTasks();
        Arrays.sort(currentList);
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);
    }
    
    @Test
    public void deleteCommand_deleteTaskAtEnd_success() {
        //delete the last in the list
        TestTask[] currentList = td.getTypicalTasks();
        Arrays.sort(currentList);
        int targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);
    }
    
    @Test
    public void deleteCommand_deleteTaskInMiddle_success() {
        TestTask[] currentList = td.getTypicalTasks();
        Arrays.sort(currentList);
        int targetIndex = currentList.length / 2;
        assertDeleteSuccess(targetIndex, currentList);
    }
    
    @Test
    public void deleteCommand_invalidIndex_errorMessage() {
        TestTask[] currentList = td.getTypicalTasks();
        Arrays.sort(currentList);
        commandBox.runCommand("del " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    /**
     * Runs the delete command to delete the task at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        Arrays.sort(currentList);
        TestTask taskToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        TestTask[] expectedRemainder = TestUtil.removeTaskFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("del " + targetIndexOneIndexed);

        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskPane.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

}
