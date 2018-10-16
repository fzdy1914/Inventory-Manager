package seedu.inventory.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.inventory.logic.CommandHistory;
import seedu.inventory.logic.commands.exceptions.CommandException;
import seedu.inventory.model.Inventory;
import seedu.inventory.model.Model;
import seedu.inventory.model.ReadOnlyInventory;
import seedu.inventory.model.item.Item;
import seedu.inventory.model.item.Quantity;
import seedu.inventory.testutil.ItemBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullItem_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_itemAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingItemAdded modelStub = new ModelStubAcceptingItemAdded();
        Item validItem = new ItemBuilder().build();

        CommandResult commandResult = new AddCommand(validItem).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validItem), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validItem), modelStub.itemsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateItem_throwsCommandException() throws Exception {
        Item validItem = new ItemBuilder().build();
        AddCommand addCommand = new AddCommand(validItem);
        ModelStub modelStub = new ModelStubWithItem(validItem);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_ITEM);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Item iPhone = new ItemBuilder().withName("iPhone XR").build();
        Item googlePixel = new ItemBuilder().withName("Google Pixel XL").build();
        AddCommand addIphoneCommand = new AddCommand(iPhone);
        AddCommand addGoogleCommand = new AddCommand(googlePixel);

        // same object -> returns true
        assertTrue(addIphoneCommand.equals(addIphoneCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(iPhone);
        assertTrue(addIphoneCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addIphoneCommand.equals(1));

        // null -> returns false
        assertFalse(addIphoneCommand.equals(null));

        // different item -> returns false
        assertFalse(addIphoneCommand.equals(addGoogleCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addItem(Item item) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyInventory newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyInventory getInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasItem(Item item) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteItem(Item target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateItem(Item target, Item editedItem) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Item> getFilteredItemList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredItemList(Predicate<Item> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitInventory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void createSale(Item item, Quantity quantity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteSale(String id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void listSales(String records) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single item.
     */
    private class ModelStubWithItem extends ModelStub {
        private final Item item;

        ModelStubWithItem(Item item) {
            requireNonNull(item);
            this.item = item;
        }

        @Override
        public boolean hasItem(Item item) {
            requireNonNull(item);
            return this.item.isSameItem(item);
        }
    }

    /**
     * A Model stub that always accept the item being added.
     */
    private class ModelStubAcceptingItemAdded extends ModelStub {
        private final ArrayList<Item> itemsAdded = new ArrayList<>();

        @Override
        public boolean hasItem(Item item) {
            requireNonNull(item);
            return itemsAdded.stream().anyMatch(item::isSameItem);
        }

        @Override
        public void addItem(Item item) {
            requireNonNull(item);
            itemsAdded.add(item);
        }

        @Override
        public void commitInventory() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyInventory getInventory() {
            return new Inventory();
        }
    }

}