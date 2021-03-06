package seedu.oneline.logic.parser;

import static seedu.oneline.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.oneline.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.oneline.commons.exceptions.IncorrectCommandException;
import seedu.oneline.commons.core.Messages;
import seedu.oneline.commons.exceptions.IllegalCmdArgsException;
import seedu.oneline.commons.exceptions.IllegalValueException;
import seedu.oneline.commons.util.StringUtil;
import seedu.oneline.logic.commands.*;
import seedu.oneline.model.tag.TagField;
import seedu.oneline.model.task.TaskField;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

  //@@author A0140156R
    private static final Pattern EDIT_TASK_COMMAND_ARGS_FORMAT =
            Pattern.compile("(?<index>-?[\\d]+)" // index
                    + " (?<args>.+)"); // the other arguments
    
    private static final Pattern TAG_ARGS_FORMAT =
            Pattern.compile("\\#(?<tag>.*)"); // #<tag>
    
    private static final Pattern EDIT_TAG_COMMAND_ARGS_FORMAT = 
            Pattern.compile("\\#(?<tag>[^\\s]*)" // tag name
                    + " (?<args>.+)"); // the other arguments
  //@@author
    
    public Parser() {}

  //@@author A0140156R
    private static final Map<String, Class<? extends Command>> COMMAND_CLASSES = initCommandClasses();
    
    private static Map<String, Class<? extends Command>> initCommandClasses() {
        Map<String, Class<? extends Command>> commands = new HashMap<String, Class<? extends Command>>();
        commands.put(AddCommand.COMMAND_WORD.toLowerCase(), AddCommand.class);
        commands.put(SelectCommand.COMMAND_WORD.toLowerCase(), SelectCommand.class);
        commands.put(EditCommand.COMMAND_WORD.toLowerCase(), EditCommand.class);
        commands.put(DeleteCommand.COMMAND_WORD.toLowerCase(), DeleteCommand.class);
        commands.put(ClearCommand.COMMAND_WORD.toLowerCase(), ClearCommand.class);
        commands.put(DoneCommand.COMMAND_WORD.toLowerCase(), DoneCommand.class);
        commands.put(FindCommand.COMMAND_WORD.toLowerCase(), FindCommand.class);
        commands.put(ListCommand.COMMAND_WORD.toLowerCase(), ListCommand.class);
        commands.put(ExitCommand.COMMAND_WORD.toLowerCase(), ExitCommand.class);
        commands.put(HelpCommand.COMMAND_WORD.toLowerCase(), HelpCommand.class);
        commands.put(LocationCommand.COMMAND_WORD.toLowerCase(), LocationCommand.class);
        commands.put(UndoneCommand.COMMAND_WORD.toLowerCase(), UndoneCommand.class);
        commands.put(UndoCommand.COMMAND_WORD.toLowerCase(), UndoCommand.class);
        commands.put(RedoCommand.COMMAND_WORD.toLowerCase(), RedoCommand.class);
        return commands;
    }
    
    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        final String commandWord = getGroup(matcher, "commandWord").toLowerCase();
        Class<? extends Command> cmdClass;
        try {
            cmdClass = getCommandClass(commandWord);
        } catch (IncorrectCommandException e) {
            return new IncorrectCommand(e.getMessage());
        }
        final String arguments = getGroup(matcher, "arguments");
        Method method = getCommandCreator(cmdClass);
        Command cmd;
        try {
            cmd = createCommand(method, arguments);
        } catch (InvocationTargetException e) {
            if (e.getCause().getMessage() == null) {
                e.printStackTrace();
                return new IncorrectCommand("Unknown error");
            }
            return new IncorrectCommand(e.getCause().getMessage());
        }
        return cmd;
    }

    /**
     * Returns the group extracted from the string as specified
     * by the regex
     * 
     * @param matcher
     * @param group
     * @return
     */
    private String getGroup(final Matcher matcher, String group) {
        return matcher.group(group);
    }
    
    /**
     * @param commandWord
     * @return class corresponding to the command word
     * @throws IncorrectCommandException 
     */
    private Class<? extends Command> getCommandClass(final String commandWord) throws IncorrectCommandException {
        if (!COMMAND_CLASSES.containsKey(commandWord)) {
            throw new IncorrectCommandException(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
        Class<? extends Command> cmdClass = COMMAND_CLASSES.get(commandWord);
        return cmdClass;
    }

    /**
     * @param cmdClass
     * @return method that takes in command arguments and returns a command 
     */
    private Method getCommandCreator(Class<? extends Command> cmdClass) {
        Method method = null;
        try {
            method = cmdClass.getMethod("createFromArgs", String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            assert false : "Command class should implement \"createFromArgs(String)\".";
        }
        return method;
    }

    /**
     * @param method that takes in command arguments and returns a command
     * @param arguments for the command
     * @return an instance of command class
     * @throws InvocationTargetException 
     */
    private Command createCommand(Method method, final String arguments) throws InvocationTargetException {
        try {
            return (Command) method.invoke(null, arguments);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            assert false : e.getClass().toString() + " : " + e.getMessage();
            assert false : "Command class " + e.getClass().toString() + " should implement \"createFromArgs(String)\".";
        }
        return null;
    }

    /**
     * Parses arguments in the context of CRUD commands for tasks
     *
     * @param args full command args string
     * @return the fields specified in the args
     */
    public static Map<TaskField, String> getTaskFieldsFromArgs(String args) throws IllegalCmdArgsException {
        // Clear extra whitespace characters
        args = args.trim();
        // Get the indexes of all task fields
        String[] splitted = args.split(" ");
        List<Entry<TaskField, Integer>> fieldIndexes = extractFieldIndexes(splitted);
        // Extract the respective task fields into results map
        if (fieldIndexes.size() == 0) {
            return extractFieldName(splitted);
        }
        return extractAllFields(splitted, fieldIndexes);
    }

    /**
     * Returns a list of indexes of the field locations.
     * 
     * E.g.
     * argsArr = ["My", "Task", "Name", ".from", "3pm", "today", ".to", "tomorrow", "8am", "#camp"]
     * returns a list with the following elements:
     * [[TaskField.START_TIME, 3], [TaskField.END_TIME, 6], [TaskField.TAG, 9]]
     * 
     * @param argsArr
     * @return index of the fields in the argsArr
     * @throws IllegalCmdArgsException
     */
    private static List<Entry<TaskField, Integer>> extractFieldIndexes(String[] argsArr)
            throws IllegalCmdArgsException {
        List<Entry<TaskField, Integer>> fieldIndexes = new ArrayList<>();
        TaskField[] fields = new TaskField[] { TaskField.START_TIME, TaskField.END_TIME,
                                               TaskField.DEADLINE, TaskField.IS_DONE };
        for (TaskField tf : fields) {
            Integer index = getIndexesOfKeyword(argsArr, tf.getKeyword());
            if (index != null) {
                fieldIndexes.add(new SimpleEntry<TaskField, Integer>(tf, index));
            }
        }
        Optional<Integer> tagIndex = extractTagIndex(argsArr);
        if (tagIndex.isPresent()) {
            fieldIndexes.add(new SimpleEntry<TaskField, Integer>(TaskField.TAG, tagIndex.get()));
        }
        // Arrange the indexes of task fields in sorted order
        Collections.sort(fieldIndexes, new Comparator<Entry<TaskField, Integer>>() {
            @Override
            public int compare(Entry<TaskField, Integer> a, Entry<TaskField, Integer> b) {
                return a.getValue().compareTo(b.getValue());
            } });
        return fieldIndexes;
    }

    /**
     * Finds the index of the tag field if it exists
     * 
     * @param argsArr
     * @param fieldIndexes
     * @throws IllegalCmdArgsException
     */
    private static Optional<Integer> extractTagIndex(String[] argsArr) throws IllegalCmdArgsException {
        Optional<Integer> tagIndex = Optional.empty();
        for (int i = 0; i < argsArr.length; i++) {
            if (!argsArr[i].toLowerCase().startsWith(CommandConstants.TAG_PREFIX)) {
                continue;
            }
            for (int j = i; j < argsArr.length; j++) {
                if (!argsArr[j].startsWith(CommandConstants.TAG_PREFIX)) {
                    throw new IllegalCmdArgsException("Categories should be the last fields in command");
                }
            }
            if (tagIndex.isPresent()) {
                throw new IllegalCmdArgsException("There should only be one category specified");
            }
            tagIndex = Optional.of(i);
        }
        return tagIndex;
    }
    
    /**
     * 
     * Extracts argument segments into a map of fields to values.
     * 
     * Eg.
     * argsArr = ["My", "Task", "Name", ".from", "3pm", "today", ".to", "tomorrow", "8am", "#camp"]
     * fieldIndexes = [[TaskField.START_TIME, 3], [TaskField.END_TIME, 6], [TaskField.TAG, 9]]
     * returns a map with the following entries:
     * [[TaskField.NAME, "My Task Name"], [TaskField.START_TIME, "3pm today"],
     * [TaskField.END_TIME, "tomorrow 8am"] [TaskField.TAG, "camp"]]
     * 
     * 
     * @param argsArr - array of arguments
     * @param fieldIndexes - indexes of their indexes
     * @return map of the task fields and values
     * @throws IllegalCmdArgsException
     */
    private static Map<TaskField, String> extractAllFields(String[] argsArr,
            List<Entry<TaskField, Integer>> fieldIndexes) throws IllegalCmdArgsException {
        Map<TaskField, String> result = new HashMap<TaskField, String>();
        Integer firstIndex = fieldIndexes.get(0).getValue();
        if (firstIndex > 0) {
            String[] subArr = Arrays.copyOfRange(argsArr, 0, firstIndex);
            result.put(TaskField.NAME, String.join(" ", subArr));
        }
        for (int i = 0; i < fieldIndexes.size(); i++) {
            if (fieldIndexes.get(i).getKey() == TaskField.TAG && i != fieldIndexes.size() - 1) {
                throw new IllegalCmdArgsException("Hashtags should be the last fields in command.");
            }
            int startIndex = (fieldIndexes.get(i).getKey() == TaskField.TAG) ?
                    fieldIndexes.get(i).getValue() :
                    fieldIndexes.get(i).getValue() + 1;
            int endIndex = (i == fieldIndexes.size() - 1) ?
                    argsArr.length : fieldIndexes.get(i + 1).getValue();
            String[] subArr = Arrays.copyOfRange(argsArr, startIndex, endIndex);
            if (fieldIndexes.get(i).getKey() == TaskField.TAG) {
                for (int j = 0; j < subArr.length; j++) {
                    subArr[j] = getTagFromArgs(subArr[j]);
                }
            }
            result.put(fieldIndexes.get(i).getKey(), String.join(" ", subArr));
        }
        return result;
    }

    /**
     * Returns a map containing the entry [TaskField.NAME, name],
     * where name the concatenated fields in argFields
     * 
     * @param argFields
     * @param result
     * @return
     * @throws IllegalCmdArgsException
     */
    private static Map<TaskField, String> extractFieldName(String[] argFields) throws IllegalCmdArgsException {
        Map<TaskField, String> result = new HashMap<TaskField, String>();
        if (argFields[0].equals("")) { 
            throw new IllegalCmdArgsException("No fields specified.");
        }
        result.put(TaskField.NAME, String.join(" ", argFields));
        return result;
    }
    
    /**
     * Parses arguments in the context of CRUD commands for tags
     *
     * @param args full command args string
     * @return the fields specified in the args
     * @throws IllegalCmdArgsException 
     */
    public static Map<TagField, String> getTagFieldsFromArgs(String args) throws IllegalCmdArgsException {
        Map<TagField, String> fields = new HashMap<TagField, String>();
        String[] splitted = args.split(" ");
        for (int i = 0; i < splitted.length; i++) {
            TagField fieldType = null;
            String field = null;
            if (splitted[i].startsWith(CommandConstants.TAG_PREFIX)) {
                fieldType = TagField.NAME;
                field = getTagFromArgs(splitted[i]);
            } else {
                fieldType = TagField.COLOR;
                field = splitted[i];
            }
            if (fields.containsKey(fieldType)) {
                throw new IllegalCmdArgsException("There are more than 1 instances of " +
                        fieldType.toString() + " in the command.");
            }
            fields.put(fieldType, field);
        }
        return fields;
    }
    
    /**
     * Finds the location of the specified keyword in the array of args
     *
     * @param args fields to be checked
     * @return index of where the keyword is found
     * @throws IllegalCmdArgsException if command is not found
     */
    private static Integer getIndexesOfKeyword(String[] args, String keyword) throws IllegalCmdArgsException {
        keyword = keyword.toLowerCase();
        String curKeyword = CommandConstants.KEYWORD_PREFIX + keyword.toLowerCase();
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals(curKeyword)) {
                indexes.add(i);
            }
        }
        if (indexes.size() > 1) {
            throw new IllegalCmdArgsException("There are more than 1 instances of " +
                                CommandConstants.KEYWORD_PREFIX + keyword + " in the command.");
        } else if (indexes.size() < 1) {
            return null;
        }
        // We allow multiple interpretations of the command if no clear keywords are used
        return indexes.get(0);
    }

    /**
     * Parses arguments to get an integer index
     *
     * @param args full command args string
     * @return the value of the index, null if invalid
     * @throws IllegalValueException 
     */
    public static Integer getIndexFromArgs(String args) throws IllegalValueException {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            throw new IllegalValueException("Index does not parse to integer.");
        }
        return index.get();
    }

    /**
     * Parses a tag field [#<tag>] to return the tag [<tag>]
     *
     * @param args full command args string
     * @return the tag
     */
    public static String getTagFromArgs(String args) {
        final Matcher matcher = TAG_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            assert false;
        }
        return matcher.group("tag");
    }
    
    public static Entry<Integer, Map<TaskField, String>> getIndexAndTaskFieldsFromArgs(String args) throws IllegalValueException, IllegalCmdArgsException {
        final Matcher matcher = EDIT_TASK_COMMAND_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new IllegalCmdArgsException(Messages.MESSAGE_EDIT_TAG_ARGS_INVALID_FORMAT);
        }
        Integer index = Parser.getIndexFromArgs(matcher.group("index"));
        Map<TaskField, String> fields = Parser.getTaskFieldsFromArgs(matcher.group("args"));
        return new SimpleEntry<Integer, Map<TaskField, String>>(index, fields);
    }
    
    public static Entry<String, Map<TagField, String>> getTagAndTagFieldsFromArgs(String args) throws IllegalValueException, IllegalCmdArgsException {
        final Matcher matcher = EDIT_TAG_COMMAND_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new IllegalCmdArgsException(Messages.MESSAGE_EDIT_TAG_ARGS_INVALID_FORMAT);
        }
        String tag = matcher.group("tag");
        Map<TagField, String> fields = Parser.getTagFieldsFromArgs(matcher.group("args"));
        return new SimpleEntry<String, Map<TagField, String>>(tag, fields);
    }
    

  //@@author
    
    /**
     * Parses arguments to get search keywords
     *
     * @param args full command args string
     * @return set of keywords
     */
    public static Set<String> getKeywordsFromArgs(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return null; // TODO: THROW ERROR
        }
        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return keywordSet;
    }
    
    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private static Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }
    
}