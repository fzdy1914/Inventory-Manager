package seedu.inventory.model.item.exceptions;

/**
 * Signals that the operation will result in duplicate Items (Items are considered duplicates if they have the same
 * SKUs).
 */
public class DuplicateItemException extends RuntimeException {
    public DuplicateItemException() {
        super("Operation would result in duplicate items");
    }
}
