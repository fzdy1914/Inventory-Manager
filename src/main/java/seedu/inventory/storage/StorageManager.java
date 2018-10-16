package seedu.inventory.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.inventory.commons.core.ComponentManager;
import seedu.inventory.commons.core.LogsCenter;
import seedu.inventory.commons.events.model.InventoryChangedEvent;
import seedu.inventory.commons.events.storage.DataSavingExceptionEvent;
import seedu.inventory.commons.exceptions.DataConversionException;
import seedu.inventory.model.ReadOnlyInventory;
import seedu.inventory.model.ReadOnlySaleList;
import seedu.inventory.model.UserPrefs;

/**
 * Manages storage of Inventory data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private InventoryStorage inventoryStorage;
    private UserPrefsStorage userPrefsStorage;
    private SaleListStorage saleListStorage;


    public StorageManager(InventoryStorage inventoryStorage, UserPrefsStorage userPrefsStorage,
                          SaleListStorage saleListStorage) {
        super();
        this.inventoryStorage = inventoryStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.saleListStorage = saleListStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Inventory methods ==============================

    @Override
    public Path getInventoryFilePath() {
        return inventoryStorage.getInventoryFilePath();
    }

    @Override
    public Optional<ReadOnlyInventory> readInventory() throws DataConversionException, IOException {
        return readInventory(inventoryStorage.getInventoryFilePath());
    }

    @Override
    public Optional<ReadOnlyInventory> readInventory(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return inventoryStorage.readInventory(filePath);
    }

    @Override
    public void saveInventory(ReadOnlyInventory inventory) throws IOException {
        saveInventory(inventory, inventoryStorage.getInventoryFilePath());
    }

    @Override
    public void saveInventory(ReadOnlyInventory inventory, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        inventoryStorage.saveInventory(inventory, filePath);
    }

    // ================ Sale List methods ==============================

    @Override
    public Path getSaleListFilePath() {
        return saleListStorage.getSaleListFilePath();
    }

    @Override
    public Optional<ReadOnlySaleList> readSaleList(ReadOnlyInventory inventory) throws DataConversionException,
            IOException {
        return readSaleList(saleListStorage.getSaleListFilePath(), inventory);
    }

    @Override
    public Optional<ReadOnlySaleList> readSaleList(Path filePath, ReadOnlyInventory inventory)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return saleListStorage.readSaleList(filePath, inventory);
    }

    @Override
    public void saveSaleList(ReadOnlySaleList saleList) throws IOException {
        saveSaleList(saleList, saleListStorage.getSaleListFilePath());
    }

    @Override
    public void saveSaleList(ReadOnlySaleList saleList, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        saleListStorage.saveSaleList(saleList, filePath);
    }


    @Override
    @Subscribe
    public void handleInventoryChangedEvent(InventoryChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveInventory(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}