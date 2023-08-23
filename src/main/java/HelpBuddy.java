import java.util.ArrayList;
import java.util.Scanner;

/**
 * A HelpBuddy class that produces messages to user.
 */

public class HelpBuddy {
    /** A string that produces a line to segment messages produced. */
    private final static String horizontal = "    ____________________________________________________________";
    /** An ArrayList that holds everything the user has typed into the chatbot. */
    private ArrayList<Task> userInput = new ArrayList<>(100);

    /**
     * @return hello message.
     */
    public static String printHelloMessage() {
        return "Hello! I'm HelpBuddy.\n" + "    What can I do for you?\n";
    }

    /**
     * @return bye message.
     */
    public static String printByeMessage() {
        return "Bye. Hope to see you again soon!\n";
    }

    /**
     * A method that segments replies from HelpBuddy.
     *
     * @param s The string that HelpBuddy replies.
     */
    private void printMessageBlock(String s) {
         System.out.println(horizontal + "\n" + "    " + s + horizontal + "\n");
    }

    /**
     * A method that prints the response for command mark.
     * @param i The value keyed in by user.
     * @throws HelpBuddyException If the input value by user is invalid.
     */
    public void printMarkOutput(int i) throws HelpBuddyException {
        int numOfTasks = userInput.size();
        if (numOfTasks == 0 || i < 0 || i > numOfTasks - 1) {
            throw new HelpBuddyException("Invalid task number.\n");
        } else {
            Task t = this.userInput.get(i);
            String status = t.getStatusIcon();
            if (status == "X") {
                throw new HelpBuddyException("Task is already marked as done.\n");
            } else {
                t.updateDone();
                printMessageBlock("Nice! I've marked this task as done:\n      " + t + "\n");
            }
        }
    }

    /**
     * A method that prints the response for command unmark.
     * @param i The value keyed in by user.
     * @throws HelpBuddyException If the input value by user is invalid.
     */
    public void printUnmarkOutput(int i) throws HelpBuddyException {
        int numOfTasks = userInput.size();
        if (numOfTasks == 0 || i < 0 || i > numOfTasks - 1) {
            throw new HelpBuddyException("Invalid task number.\n");
        } else {
            Task t = this.userInput.get(i);
            String status = t.getStatusIcon();
            if (status == " ") {
                throw new HelpBuddyException("Task is not marked as done.\n");
            } else {
                t.updateDone();
                printMessageBlock("OK, I've marked this task as not done yet:\n      " + t + "\n");
            }
        }
    }

    /**
     * A method that displays error messages from HelpBuddy.
     *
     * @param error A String that contains error details.
     */
    private void printErrorMessage(String error) {
        printMessageBlock(error);
    }

    /**
     * A method that displays to users HelpBuddy's response to their input.
     *
     * @param sc A scanner to read user inputs
     * @throws HelpBuddyException If the command entered by user is invalid
     */
    public void outputMessage(Scanner sc) throws HelpBuddyException {
        printMessageBlock(HelpBuddy.printHelloMessage());

        String markPattern = "^mark.*";
        String unmarkPattern = "^unmark.*";
        String toDoPattern = "^todo.*";
        String deadlinePattern = "^deadline.*";
        String eventPattern = "^event.*";
        String inputMessage = sc.nextLine();

        while(inputMessage != "") {
            try {
                if (inputMessage.matches(markPattern)) {
                    if (inputMessage.matches("mark")) {
                        throw new HelpBuddyException("Please enter a task number.\n");
                    }
                    String taskIndex = inputMessage.replaceAll("mark\\s+", "");
                    if (!taskIndex.isBlank()) {
                        int intTaskValue = Integer.valueOf(taskIndex) - 1;
                        printMarkOutput(intTaskValue);
                    } else {
                        throw new HelpBuddyException("Please enter a task number.\n");
                    }
                } else if (inputMessage.matches(unmarkPattern)) {
                    if (inputMessage.matches("unmark")) {
                        throw new HelpBuddyException("Please enter a task number.\n");
                    }
                    String taskIndex = inputMessage.replaceAll("unmark\\s+", "");
                    if (!taskIndex.isBlank()) {
                        int intTaskValue = Integer.valueOf(taskIndex) - 1;
                        printUnmarkOutput(intTaskValue);
                    } else {
                        throw new HelpBuddyException("Please enter a task number.\n");
                    }
                } else if (inputMessage.equalsIgnoreCase("list")){
                    Object[] inputArray = this.userInput.toArray();
                    String outputMessage = "Here are the tasks in your list:\n";
                    for (int i = 0; i < inputArray.length; i++) {
                        int index = i + 1;
                        Task curr = (Task) inputArray[i];
                        outputMessage +=  "    " + index + "." + curr + "\n";
                    }
                    printMessageBlock(outputMessage);
                } else if (inputMessage.equalsIgnoreCase("bye")) {
                    printMessageBlock(HelpBuddy.printByeMessage());
                    break;
                } else {
                    Task t = null;
                    if (inputMessage.matches(deadlinePattern)) {
                        String taskDetails = inputMessage.replaceAll("deadline", "");
                        if (taskDetails.isBlank()) {
                            t = new Deadline("", "");
                        }
                        String[] taskDetailArray = taskDetails.split("/by");
                        if (taskDetailArray.length == 1) {
                            String[] taskDetailErrorArray = taskDetailArray[0].split("/");
                            if (taskDetailErrorArray.length == 1) {
                                t = new Deadline(taskDetails, "");
                            } else {
                                throw new HelpBuddyException("Please use /by to indicate deadline.\n");
                            }
                        }
                        t = new Deadline(taskDetailArray[0], taskDetailArray[1]);
                    } else if (inputMessage.matches(toDoPattern)) {
                        String taskDetails = inputMessage.replaceAll("todo", "");
                        t = new ToDo(taskDetails);
                    } else if (inputMessage.matches(eventPattern)) {
                        String taskDetails = inputMessage.replaceAll("event", "");
                        if (taskDetails.isBlank()) {
                            t = new Event("", "", "");
                        }
                        String[] taskDetailArray = taskDetails.split("/from");
                        String[] taskDetailErrorArray = taskDetails.split("/");
                        if (taskDetailArray.length == 1) {
                            if (taskDetailErrorArray.length == 1) {
                                t = new Event(taskDetails, "", "");
                            } else {
                                throw new HelpBuddyException("Please enter and use /from for start time first then\n    /to for end time for event.\n");
                            }
                        }
                        String[] timeFromTo = taskDetailArray[1].split("/to");
                        if (timeFromTo.length == 1) {
                            t = new Event(taskDetails, timeFromTo[0], "");
                        }
                        t = new Event(taskDetailArray[0], timeFromTo[0], timeFromTo[1]);
                    } else {
                        throw new HelpBuddyException("I'm sorry, but I don't know what that means.\n");
                    }
                    String output = "Got it. I've added this task:\n      ";
                    int numOfTasks = userInput.size() + 1;
                    this.userInput.add(t);
                    output += t + "\n    Now you have " + numOfTasks + " tasks in this list.\n";
                    printMessageBlock(output);
                }
            }
            catch (HelpBuddyException e) {
                printErrorMessage(e.getMessage());
            }
            inputMessage = sc.nextLine();
        }
    }
}
