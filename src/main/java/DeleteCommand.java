import java.io.IOException;

public class DeleteCommand extends Command {
    private int taskIndex;

    public DeleteCommand(int taskIndex) throws HelpBuddyException {
        if (taskIndex < 0) {
            throw new HelpBuddyException("Invalid task number.\n");
        }
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws IOException, HelpBuddyException {
        if (this.taskIndex > taskList.getSize()) {
            throw new HelpBuddyException("Invalid task number.\n");
        }
        Task task = taskList.getTask(this.taskIndex - 1);
        taskList.deleteTask(this.taskIndex - 1);
        ui.printDeleteTaskMessage(task, taskList);
    }
}
