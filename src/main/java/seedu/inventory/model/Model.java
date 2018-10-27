package seedu.inventory.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.inventory.commons.events.storage.ItemListUpdateEvent;
import seedu.inventory.model.item.Item;
import seedu.inventory.model.purchaseorder.PurchaseOrder;
import seedu.inventory.model.sale.Sale;
import seedu.inventory.model.staff.Staff;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Item> PREDICATE_SHOW_ALL_ITEMS = unused -> true;

    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<PurchaseOrder> PREDICATE_SHOW_ALL_PURCHASE_ORDER = unused -> true;

    /**
     *
     */
    Predicate<Staff> PREDICATE_SHOW_ALL_STAFFS = unused -> true;

    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlyInventory newData);

    /**
     * Replaces the item list in backing model with the provided new item list.
     */
    void resetItemList(ReadOnlyItemList newItemList);

    /**
     * Returns the Inventory
     */
    ReadOnlyInventory getInventory();

    /**
     * Returns the accessed filtered list
     */
    <T> FilteredList<T> getAccessedList();

    //=========== Reporting API ========================================================

    /**
     * Export the item list to the file path.
     *
     * @param filePath The path to export.
     */
    void exportItemList(Path filePath);

    /**
     * Import the item list from the file path.
     *
     * @param filePath The path to import.
     */
    void importItemList(Path filePath);

    //=========== Item API =============================================================

    /**
     * Returns true if a item with the same identity as {@code item} exists in the inventory.
     */
    boolean hasItem(Item item);

    /**
     * Returns true if a item with the same identity as {@code item} exists in the inventory.
     */
    void viewItem();

    /**
     * Deletes the given item.
     * The item must exist in the inventory.
     */
    void deleteItem(Item target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in the inventory.
     */
    void addItem(Item item);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in the inventory.
     * The item identity of {@code editedItem} must not be the same as another existing item in the inventory.
     */
    void updateItem(Item target, Item editedItem);

    /**
     * Returns an unmodifiable view of the filtered item list
     */
    ObservableList<Item> getFilteredItemList();

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemList(Predicate<Item> predicate);

    //=========== Purchase Order API =============================================================

    /**
     * Returns true if a purchase order with the same identity as {@code po} exists in the inventory.
     */
    boolean hasPurchaseOrder(PurchaseOrder po);

    /**
     * Returns true if a purchase order has a {@code item} in the inventory.
     */
    boolean hasPurchaseOrder(Item item);

    /**
     * Deletes the given purchase order.
     * The purchase order must exist in the inventory.
     */
    void viewPurchaseOrder();

    /**
     * Deletes the given purchase order.
     * The purchase order must exist in the inventory.
     */
    void deletePurchaseOrder(PurchaseOrder target);

    /**
     * Deletes all purchase orders given the item.
     * The item and purchase order must exist in the inventory.
     */
    void deletePurchaseOrder(Item target);

    /**
     * Adds the given purchase order.
     * {@code purchaseorder} must not already exist in the inventory.
     */
    void addPurchaseOrder(PurchaseOrder purchaseOrder);

    /**
     * Replaces the given purchaseorder {@code target} with {@code editedPurchaseOrder}.
     * {@code target} must exist in the inventory.
     */
    void updatePurchaseOrder(PurchaseOrder target, PurchaseOrder editedPurchaseOrder);

    /**
     * Approves the given purchaseorder {@code target} and add the quantity to the item.
     * {@code target} must exist in the inventory.
     */
    void approvePurchaseOrder(PurchaseOrder target);

    /**
     * Rejects the given purchaseorder {@code target}.
     * {@code target} must exist in the inventory.
     */
    void rejectPurchaseOrder(PurchaseOrder target);

    /**
     * Returns an unmodifiable view of the filtered purchase order list
     */
    ObservableList<PurchaseOrder> getFilteredPurchaseOrderList();

    /**
     * Updates the filter of the filtered purchase order list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPurchaseOrderList(Predicate<PurchaseOrder> predicate);


    //=========== Undo/Redo Api =============================================================

    /**
     * Returns true if a staff with the same identity as {@code item} exists in the staff list.
     *
     * @param staff the staff to be checked
     */
    boolean hasStaff(Staff staff);

    /**
     * Deletes the given staff.
     * The staff must exist in the staff list.
     *
     * @param target staff to be deleted must be in the list
     */
    void deleteStaff(Staff target);

    /**
     * Adds the given staff.
     * {@code staff} must not already exist in the inventory.
     *
     * @param staff the staff to be added must not be in the list.
     */
    void addStaff(Staff staff);

    /**
     * Replaces the given staff {@code target} with {@code editedStaff}.
     * {@code target} must exist in the staff list.
     * The staff identity of {@code editedStaff} must not be the same as another existing staff in the staff list.
     *
     * @param target      the staff to be updated
     * @param editedStaff the staff with updated info
     */
    void updateStaff(Staff target, Staff editedStaff);

    /**
     * Returns true if a staff with the same identity as {@code staff} exists in the staff list.
     */
    void viewStaff();

    /**
     * Retrieves the user with staff with partial information.
     */
    Staff retrieveStaff(Staff toRetrieve);

    /**
     * Returns an unmodifiable view of the filtered staff list
     */
    ObservableList<Staff> getFilteredStaffList();

    /**
     * Updates the filter of the filtered staff list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStaffList(Predicate<Staff> predicate);

    /**
     * Authenticates the user.
     */
    void authenticateUser(Staff staff);

    /**
     * Logs the user out from the inventory manager.
     */
    void logoutUser();

    /**
     * Checks if there is any user logged in.
     */
    boolean isUserLoggedIn();

    /**
     * Retrieves the current user in the session.
     */
    Staff getUser();

    /**
     * Returns true if the model has previous inventory states to restore.
     */
    boolean canUndoInventory();

    /**
     * Returns true if the model has undone inventory states to restore.
     */
    boolean canRedoInventory();

    /**
     * Restores the model's inventory to its previous state.
     */
    void undoInventory();

    /**
     * Restores the model's inventory to its previously undone state.
     */
    void redoInventory();

    /**
     * Saves the current inventory state for undo/redo.
     */
    void commitInventory();

    /**
     * Returns the sale list.
     */
    ReadOnlySaleList getSaleList();

    /**
     * Returns the sale list.
     */
    ObservableList<Sale> getObservableSaleList();

    /**
     * Add a new sale.
     */
    void addSale(Sale sale);

    /**
     * Delete a sale.
     */
    void deleteSale(Sale sale);

    /**
     * List sales.
     */
    void listSales();

    /**
     * Handler function of ItemListUpdateEvent.
     */
    void handleItemListUpdateEvent(ItemListUpdateEvent event);

}
