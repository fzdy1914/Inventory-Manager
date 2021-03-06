package seedu.inventory.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.inventory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.inventory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.inventory.testutil.TypicalItems.getTypicalInventory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.inventory.logic.CommandHistory;
import seedu.inventory.logic.commands.csv.ExportCsvCommand;
import seedu.inventory.model.Model;
import seedu.inventory.model.ModelManager;
import seedu.inventory.model.SaleList;
import seedu.inventory.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditItemCommand.
 */
//TODO
public class ExportCsvCommandTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private Model model = new ModelManager(getTypicalInventory(), new UserPrefs(), new SaleList());
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * Helper method to provide temporary file paths
     */
    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + File.separator + fileName;
    }

    @Test
    public void execute_validFilePath_success() {
        Path filePath = Paths.get(getTempFilePath("validExport.csv"));
        ExportCsvCommand command = new ExportCsvCommand(filePath).setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS);
        String expectedMessage = String.format(ExportCsvCommand.MESSAGE_SUCCESS_ITEMS, filePath);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, model);

        command = new ExportCsvCommand(filePath).setCommandWord(ExportCsvCommand.COMMAND_WORD_SALES);
        expectedMessage = String.format(ExportCsvCommand.MESSAGE_SUCCESS_SALES, filePath);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, model);

        command = new ExportCsvCommand(filePath).setCommandWord(ExportCsvCommand.COMMAND_WORD_STAFFS);
        expectedMessage = String.format(ExportCsvCommand.MESSAGE_SUCCESS_STAFFS, filePath);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, model);

        command = new ExportCsvCommand(filePath).setCommandWord(ExportCsvCommand.COMMAND_WORD_PURCHASE_ORDERS);
        expectedMessage = String.format(ExportCsvCommand.MESSAGE_SUCCESS_PURCHASE_ORDERS, filePath);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, model);
    }

    @Test
    public void execute_invalidFileExtension_throwsCommandException() {
        Path filePath = Paths.get(getTempFilePath("invalidExport.notcsv"));
        ExportCsvCommand command = new ExportCsvCommand(filePath).setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS);
        String expectedMessage = String.format(ExportCsvCommand.MESSAGE_INVALID_CSV_FILEPATH, filePath);

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_invalidCommandWord_throwsCommandException() {
        Path filePath = Paths.get(getTempFilePath("validExport.csv"));
        ExportCsvCommand command = new ExportCsvCommand(filePath).setCommandWord("Dummy");
        String expectedMessage = ExportCsvCommand.MESSAGE_INVALID_COMMAND_WORD;

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }


    @Test
    public void equals() {
        final Path tempPath = Paths.get(getTempFilePath("validExport.csv"));
        final ExportCsvCommand standardCommand = new ExportCsvCommand(tempPath);

        // same values -> returns true
        ExportCsvCommand commandWithSameValues = new ExportCsvCommand(tempPath);

        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // same commandWord -> returns true
        assertTrue(standardCommand.setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS)
                .equals(commandWithSameValues.setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS)));

        // null -> returns false
        assertFalse(standardCommand.equals((ExportCsvCommand) null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different filepath -> returns false
        Path differentPath = Paths.get(getTempFilePath("invalidExport.csv"));
        assertFalse(standardCommand.equals(new ExportCsvCommand(differentPath)));

        // different commandWord -> returns false
        assertFalse(standardCommand.setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS)
                .equals(commandWithSameValues.setCommandWord(ExportCsvCommand.COMMAND_WORD_PURCHASE_ORDERS)));

        // different commandWord -> returns false
        assertFalse(standardCommand.setCommandWord(ExportCsvCommand.COMMAND_WORD_ITEMS)
                .equals(commandWithSameValues.setCommandWord((String) null)));
    }

}
