//@@author A0121657H
package seedu.oneline.logic.commands;

import java.util.Iterator;
import java.util.Set;

import seedu.oneline.commons.core.EventsCenter;
import seedu.oneline.commons.core.Messages;
import seedu.oneline.commons.events.ui.ShowHelpRequestEvent;
import seedu.oneline.commons.events.ui.ChangeViewEvent; 
import seedu.oneline.commons.exceptions.IllegalCmdArgsException;
import seedu.oneline.commons.exceptions.IllegalValueException;
import seedu.oneline.logic.parser.Parser;

/**
 * Lists all tasks in the task book to the user.
 */
public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows a list of undone tasks accordingly \n"
            + "Parameters: [<today> / <week> / <float> / <done> / #<category>] \n"
            + "Example: " + COMMAND_WORD
            + "today";

    public static final String MESSAGE_SUCCESS = "Listed %1$s tasks";

    public static final String MESSAGE_INVALID = "Argument given is invalid. \n" +
                                                "Supported formats: list [done/undone/today/week/float/#<Category>]";

    public String listBy;

    public ListCommand() {
        this.listBy = " ";
    }

    public ListCommand(String listBy) throws IllegalCmdArgsException {
        this.listBy = listBy;
    }

    public static ListCommand createFromArgs(String args) throws IllegalValueException, IllegalCmdArgsException {
        args = args.trim();
        if (args.isEmpty()) {
            return new ListCommand(" ");
        }
        if (args.startsWith(CommandConstants.TAG_PREFIX)){
            return ListTagCommand.createFromArgs(args);
        }
        args.toLowerCase();
        Set<String> keywords = Parser.getKeywordsFromArgs(args);
        if (keywords == null) {
            throw new IllegalCmdArgsException(Messages.getInvalidCommandFormatMessage(MESSAGE_INVALID));
        }
        Iterator<String> iter = keywords.iterator();
        String listBy = iter.next();
        return new ListCommand(listBy);
    }
    
    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAllNotDone();
        switch (listBy) {
        case " ":
            model.updateFilteredListToShowAllNotDone();
            break;
        case "done":
            model.updateFilteredListToShowAllDone();
            break;
        case "undone":
            model.updateFilteredListToShowAllNotDone();
            break;
        case "today":
            model.updateFilteredListToShowToday();
            break;
        case "week":
            model.updateFilteredListToShowWeek();
            break;
        case "float":
            model.updateFilteredListToShowFloat();
            break;
        default:
            return new CommandResult(MESSAGE_INVALID);
        }
        EventsCenter.getInstance().post(new ChangeViewEvent(listBy));
        return new CommandResult(String.format(MESSAGE_SUCCESS, listBy.equals(" ") ? "all" : listBy));
    }

    @Override
    public boolean canUndo() {
        return true;
    }
}
