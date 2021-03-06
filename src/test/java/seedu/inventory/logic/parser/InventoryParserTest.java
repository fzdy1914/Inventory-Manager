package seedu.inventory.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.inventory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.inventory.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.inventory.logic.commands.CommandTestUtil.NAME_DESC_ZUL;
import static seedu.inventory.logic.commands.CommandTestUtil.PASSWORD_DESC_ZUL;
import static seedu.inventory.logic.commands.CommandTestUtil.ROLE_DESC_ADMIN;
import static seedu.inventory.logic.commands.CommandTestUtil.ROLE_DESC_USER;
import static seedu.inventory.logic.commands.CommandTestUtil.USERNAME_DESC_ZUL;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_NAME_ZUL;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_PASSWORD_ZUL;
import static seedu.inventory.logic.commands.CommandTestUtil.VALID_USERNAME_ZUL;
import static seedu.inventory.testutil.TypicalIndexes.INDEX_FIRST_ITEM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.inventory.logic.commands.ClearCommand;
import seedu.inventory.logic.commands.ExitCommand;
import seedu.inventory.logic.commands.HelpCommand;
import seedu.inventory.logic.commands.HistoryCommand;
import seedu.inventory.logic.commands.RedoCommand;
import seedu.inventory.logic.commands.SelectCommand;
import seedu.inventory.logic.commands.UndoCommand;
import seedu.inventory.logic.commands.authentication.ChangePasswordCommand;
import seedu.inventory.logic.commands.authentication.LoginCommand;
import seedu.inventory.logic.commands.csv.ExportCsvCommand;
import seedu.inventory.logic.commands.csv.ImportCsvCommand;
import seedu.inventory.logic.commands.item.AddItemCommand;
import seedu.inventory.logic.commands.item.DeleteItemCommand;
import seedu.inventory.logic.commands.item.EditItemCommand;
import seedu.inventory.logic.commands.item.EditItemCommand.EditItemDescriptor;
import seedu.inventory.logic.commands.item.FindItemCommand;
import seedu.inventory.logic.commands.item.FindItemSkuCommand;
import seedu.inventory.logic.commands.item.ListItemCommand;
import seedu.inventory.logic.commands.item.ListLowQuantityCommand;
import seedu.inventory.logic.commands.purchaseorder.AddPurchaseOrderCommand;
import seedu.inventory.logic.commands.purchaseorder.ApprovePurchaseOrderCommand;
import seedu.inventory.logic.commands.purchaseorder.DeletePurchaseOrderCommand;
import seedu.inventory.logic.commands.purchaseorder.ListPurchaseOrderCommand;
import seedu.inventory.logic.commands.purchaseorder.RejectPurchaseOrderCommand;
import seedu.inventory.logic.commands.staff.AddStaffCommand;
import seedu.inventory.logic.commands.staff.DeleteStaffCommand;
import seedu.inventory.logic.commands.staff.EditStaffCommand;
import seedu.inventory.logic.commands.staff.ListStaffCommand;
import seedu.inventory.logic.parser.exceptions.ParseException;
import seedu.inventory.model.item.Item;
import seedu.inventory.model.item.NameContainsKeywordsPredicate;
import seedu.inventory.model.item.SkuContainsKeywordsPredicate;
import seedu.inventory.model.purchaseorder.PurchaseOrder;
import seedu.inventory.model.staff.Password;
import seedu.inventory.model.staff.Staff;
import seedu.inventory.testutil.EditItemDescriptorBuilder;
import seedu.inventory.testutil.ItemBuilder;
import seedu.inventory.testutil.ItemUtil;
import seedu.inventory.testutil.purchaseorder.PurchaseOrderBuilder;
import seedu.inventory.testutil.purchaseorder.PurchaseOrderUtil;
import seedu.inventory.testutil.staff.EditStaffDescriptorBuilder;
import seedu.inventory.testutil.staff.StaffBuilder;

public class InventoryParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final InventoryParser parser = new InventoryParser();

    @Test
    public void parseCommand_login() throws Exception {
        Staff staff = new StaffBuilder().withUsername(VALID_USERNAME_ZUL)
                .withPassword(VALID_PASSWORD_ZUL).withName("dummy").withRole(Staff.Role.user).build();
        LoginCommand command = (LoginCommand) parser.parseCommand(LoginCommand.COMMAND_WORD
                + USERNAME_DESC_ZUL + PASSWORD_DESC_ZUL);
        assertEquals(new LoginCommand(staff), command);
    }

    @Test
    public void parseCommand_changePassword() throws Exception {
        Password password = new Password(Password.hash(VALID_PASSWORD_ZUL));
        ChangePasswordCommand command = (ChangePasswordCommand)
                parser.parseCommand(ChangePasswordCommand.COMMAND_WORD + PASSWORD_DESC_ZUL);
        assertEquals(new ChangePasswordCommand(password), command);
    }

    @Test
    public void parseCommand_add() throws Exception {
        Item item = new ItemBuilder().build();
        AddItemCommand command = (AddItemCommand) parser.parseCommand(ItemUtil.getAddCommand(item));
        assertEquals(new AddItemCommand(item), command);
    }

    @Test
    public void parseCommand_add_po() throws Exception {
        PurchaseOrder po = new PurchaseOrderBuilder().build();
        AddPurchaseOrderCommand command =
                (AddPurchaseOrderCommand) parser.parseCommand(PurchaseOrderUtil.getAddPoCommand(po));
        assertEquals(new AddPurchaseOrderCommand(po), command);
    }

    @Test
    public void parseCommand_delete_po() throws Exception {
        DeletePurchaseOrderCommand command = (DeletePurchaseOrderCommand) parser.parseCommand(
                DeletePurchaseOrderCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new DeletePurchaseOrderCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_approve_po() throws Exception {
        ApprovePurchaseOrderCommand command = (ApprovePurchaseOrderCommand) parser.parseCommand(
                ApprovePurchaseOrderCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new ApprovePurchaseOrderCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_reject_po() throws Exception {
        RejectPurchaseOrderCommand command = (RejectPurchaseOrderCommand) parser.parseCommand(
                RejectPurchaseOrderCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new RejectPurchaseOrderCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteItemCommand command = (DeleteItemCommand) parser.parseCommand(
                DeleteItemCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new DeleteItemCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Item item = new ItemBuilder().build();
        EditItemDescriptor descriptor = new EditItemDescriptorBuilder(item).build();
        EditItemCommand command = (EditItemCommand) parser.parseCommand(EditItemCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ITEM.getOneBased() + " " + ItemUtil.getEditItemDescriptorDetails(descriptor));
        assertEquals(new EditItemCommand(INDEX_FIRST_ITEM, descriptor), command);
    }

    @Test
    public void parseCommand_addStaff() throws Exception {
        Staff staff = new StaffBuilder().withUsername(VALID_USERNAME_ZUL)
                .withPassword(VALID_PASSWORD_ZUL)
                .withName(VALID_NAME_ZUL).withRole(Staff.Role.admin).build();
        AddStaffCommand command = (AddStaffCommand) parser.parseCommand(AddStaffCommand.COMMAND_WORD
                + USERNAME_DESC_ZUL + PASSWORD_DESC_ZUL + NAME_DESC_ZUL + ROLE_DESC_ADMIN);
        assertEquals(new AddStaffCommand(staff), command);
    }

    @Test
    public void parseCommand_deleteStaff() throws Exception {
        DeleteStaffCommand command = (DeleteStaffCommand) parser.parseCommand(
                DeleteStaffCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new DeleteStaffCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_editStaff() throws Exception {
        Staff staff = new StaffBuilder().withUsername(VALID_USERNAME_ZUL)
                .withPassword(VALID_PASSWORD_ZUL)
                .withName(VALID_NAME_ZUL)
                .withRole(Staff.Role.user).build();
        EditStaffCommand.EditStaffDescriptor descriptor = new EditStaffDescriptorBuilder(staff).build();
        EditStaffCommand command = (EditStaffCommand) parser.parseCommand(EditStaffCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ITEM.getOneBased() + " " + USERNAME_DESC_ZUL + PASSWORD_DESC_ZUL + NAME_DESC_ZUL
                + ROLE_DESC_USER);
        assertEquals(new EditStaffCommand(INDEX_FIRST_ITEM, descriptor), command);
    }

    @Test
    public void parseCommand_listStaff() throws Exception {
        assertTrue(parser.parseCommand(ListStaffCommand.COMMAND_WORD) instanceof ListStaffCommand);
        assertTrue(parser.parseCommand(ListStaffCommand.COMMAND_WORD + " 3") instanceof ListStaffCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindItemCommand command = (FindItemCommand) parser.parseCommand(
                FindItemCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindItemCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findSku() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar-test", "baz_test");
        FindItemSkuCommand command = (FindItemSkuCommand) parser.parseCommand(
                FindItemSkuCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindItemSkuCommand(new SkuContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListItemCommand.COMMAND_WORD) instanceof ListItemCommand);
        assertTrue(parser.parseCommand(ListItemCommand.COMMAND_WORD + " 3") instanceof ListItemCommand);
    }

    @Test
    public void parseCommand_listLowQuantity() throws Exception {
        assertTrue(parser.parseCommand(ListLowQuantityCommand.COMMAND_WORD) instanceof ListLowQuantityCommand);
        assertTrue(parser.parseCommand(ListLowQuantityCommand.COMMAND_WORD + " 3") instanceof ListLowQuantityCommand);
    }

    @Test
    public void parseCommand_list_po() throws Exception {
        assertTrue(parser.parseCommand(ListPurchaseOrderCommand.COMMAND_WORD) instanceof ListPurchaseOrderCommand);
        assertTrue(parser.parseCommand(ListPurchaseOrderCommand.COMMAND_WORD + " 3")
                instanceof ListPurchaseOrderCommand);
    }

    @Test
    public void parseCommand_exportCsv() throws Exception {
        assertTrue(parser.parseCommand(ExportCsvCommand.COMMAND_WORD_ITEMS + " f/.csv")
                instanceof ExportCsvCommand);
        assertTrue(parser.parseCommand(ExportCsvCommand.COMMAND_WORD_SALES + " f/.csv")
                instanceof ExportCsvCommand);
        assertTrue(parser.parseCommand(ExportCsvCommand.COMMAND_WORD_STAFFS + " f/.csv")
                instanceof ExportCsvCommand);
        assertTrue(parser.parseCommand(ExportCsvCommand.COMMAND_WORD_PURCHASE_ORDERS + " f/.csv")
                instanceof ExportCsvCommand);

    }

    @Test
    public void parseCommand_importCsvItems() throws Exception {
        assertTrue(parser.parseCommand(ImportCsvCommand.COMMAND_WORD_ITEMS + " f/.csv")
                instanceof ImportCsvCommand);
        assertTrue(parser.parseCommand(ImportCsvCommand.COMMAND_WORD_SALES + " f/.csv")
                instanceof ImportCsvCommand);
        assertTrue(parser.parseCommand(ImportCsvCommand.COMMAND_WORD_STAFFS + " f/.csv")
                instanceof ImportCsvCommand);
        assertTrue(parser.parseCommand(ImportCsvCommand.COMMAND_WORD_PURCHASE_ORDERS + " f/.csv")
                instanceof ImportCsvCommand);

    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ITEM.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ITEM), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
