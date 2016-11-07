//@@author A0140156R
package seedu.oneline.model.task;

import seedu.oneline.logic.commands.CommandConstants;

public enum TaskField {
    NAME(null),
    START_TIME(CommandConstants.KEYWORD_START_TIME),
    END_TIME(CommandConstants.KEYWORD_END_TIME),
    DEADLINE(CommandConstants.KEYWORD_DEADLINE),
    IS_DONE(CommandConstants.KEYWORD_DONE),
    TAG(CommandConstants.KEYWORD_PREFIX);
    
    String keyword;
    
    private TaskField(String keyword) {
        this.keyword = keyword;
    }
    
    public String getKeyword() {
        return keyword;
    }
}
