package seedu.inventory.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.inventory.commons.events.model.InventoryChangedEvent;
import seedu.inventory.commons.events.storage.DataSavingExceptionEvent;
import seedu.inventory.commons.exceptions.DataConversionException;
import seedu.inventory.model.ReadOnlyInventory;
import seedu.inventory.model.ReadOnlySaleList;
import seedu.inventory.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends InventoryStorage, SaleListStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getInventoryFilePath();

    @Override
    Optional<ReadOnlyInventory> readInventory() throws DataConversionException, IOException;

    @Override
    void saveInventory(ReadOnlyInventory inventory) throws IOException;

    // Sale List Storage
    @Override
    Path getSaleListFilePath();

    @Override
    Optional<ReadOnlySaleList> readSaleList(ReadOnlyInventory inventory) throws DataConversionException, IOException;

    @Override
    void saveSaleList(ReadOnlySaleList saleList) throws IOException;

    /**
     * Saves the current version of the Inventory List to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleInventoryChangedEvent(InventoryChangedEvent abce);
}