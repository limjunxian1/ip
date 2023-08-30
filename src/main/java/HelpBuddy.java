import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * A HelpBuddy class that produces messages to user.
 */

public class HelpBuddy {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public HelpBuddy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadData());
        } catch (HelpBuddyException e) {
            ui.printErrorMessage(e.getMessage());
        } catch (IOException e) {
            ui.printErrorMessage(e.getMessage());
        }
    }

    public void run() {
        ui.printHelloMessage();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readInput();
                Command c = Parser.parseCommand(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (HelpBuddyException e) {
                ui.printErrorMessage(e.getMessage());
            } catch (IOException e) {
                ui.printErrorMessage(e.getMessage());
            } catch (DateTimeParseException e) {
                ui.printDateTimeParseErrorMessage();
            }
        }
    }
}
