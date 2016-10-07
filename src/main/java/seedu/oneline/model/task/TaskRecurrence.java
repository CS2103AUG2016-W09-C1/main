package seedu.oneline.model.task;

import seedu.oneline.commons.exceptions.IllegalValueException;

public class TaskRecurrence {

    public static final String MESSAGE_TASK_RECURRENCE_CONSTRAINTS =
            "Task recurrence should ...";

    public final String value;

    /**
     * Validates given email.
     *
     * @throws IllegalValueException if given email address string is invalid.
     */
    public TaskRecurrence(String email) throws IllegalValueException {
        assert email != null;
        email = email.trim();
        if (!isValidTaskRecurrence(email)) {
            throw new IllegalValueException(MESSAGE_TASK_RECURRENCE_CONSTRAINTS);
        }
        this.value = email;
    }

    /**
     * Returns if a given string is a valid task time.
     */
    public static boolean isValidTaskRecurrence(String test) {
        return true; // TODO
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskRecurrence // instanceof handles nulls
                && true); // state check TODO
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
}