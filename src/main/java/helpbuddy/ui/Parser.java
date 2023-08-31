package helpbuddy.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import helpbuddy.command.AddCommand;
import helpbuddy.command.Command;
import helpbuddy.command.DeleteCommand;
import helpbuddy.command.ExitCommand;
import helpbuddy.command.ListCommand;
import helpbuddy.command.MarkCommand;
import helpbuddy.command.UnmarkCommand;
import helpbuddy.exception.HelpBuddyException;
import helpbuddy.task.Deadline;
import helpbuddy.task.Event;
import helpbuddy.task.ToDo;

/**
 * A Parser class interprets the user input and produces a corresponding Command.
 */
public class Parser {
    /**
     * Interprets the userCommand and produces a corresponding Command.
     * @param userCommand the String that user keys into HelpBuddy.
     * @return the Command to be executed by HelpBuddy.
     * @throws HelpBuddyException if userCommand is invalid.
     * @throws DateTimeParseException if the time keyed in for Deadline and Event Task is not in the
     * format of dd/MM/yy HH:mm.
     */
    public static Command parseCommand(String userCommand) throws HelpBuddyException, DateTimeParseException {
        String[] userInput = userCommand.split(" ", 2);
        String command = userInput[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

        switch (command) {
        case ("list"):
            return new ListCommand();
        case ("mark"):
            return new MarkCommand(Integer.parseInt(userInput[1]));
        case ("unmark"):
            return new UnmarkCommand(Integer.parseInt(userInput[1]));
        case ("todo"):
            if (userInput.length == 1) {
                return new AddCommand(new ToDo(""));
            }
            return new AddCommand(new ToDo(userInput[1]));
        case ("deadline"): {
            if (userInput.length == 1) {
                return new AddCommand(new Deadline("", null));
            }
            String[] taskDetails = userInput[1].split("/by", 2);
            String taskName = taskDetails[0].trim();
            if (taskDetails.length == 1) {
                return new AddCommand(new Deadline(taskName, null));
            }
            String deadlineTime = taskDetails[1].trim();
            if (deadlineTime.isBlank()) {
                return new AddCommand(new Deadline(taskName, null));
            }
            return new AddCommand(new Deadline(taskName, LocalDateTime.parse(deadlineTime, formatter)));
        }
        case ("event"): {
            if (userInput.length == 1) {
                return new AddCommand(new Event("", null, null));
            }
            String[] taskDetails = userInput[1].split("/from", 2);
            String taskName = taskDetails[0].trim();
            if (taskDetails.length == 1) {
                return new AddCommand(new Event(taskName, null, null));
            }
            String[] taskDateDetails = taskDetails[1].split("/to", 2);
            String startTime = taskDateDetails[0].trim();
            if (startTime.isBlank()) {
                return new AddCommand(new Event(taskName, null, null));
            }
            if (taskDateDetails.length == 1) {
                return new AddCommand(new Event(taskName,
                        LocalDateTime.parse(startTime, formatter),
                        null
                ));
            }
            String endTime = taskDateDetails[1].trim();
            if (startTime.isBlank()) {
                return new AddCommand(new Event(
                        taskName,
                        LocalDateTime.parse(startTime, formatter),
                        null
                ));
            }
            return new AddCommand(new Event(
                    taskName,
                    LocalDateTime.parse(startTime, formatter),
                    LocalDateTime.parse(endTime, formatter)
            ));
        }
        case("find"):
            if (userInput.length == 1) {
                return new FindCommand("");
            }
            return new FindCommand(userInput[1].trim());
        case ("delete"):
            return new DeleteCommand(Integer.parseInt(userInput[1]));
        case ("bye"):
            return new ExitCommand();
        default:
            throw new HelpBuddyException("I'm sorry, but I don't know what that means.\n");
        }
    }
}
