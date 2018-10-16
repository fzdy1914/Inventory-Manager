package seedu.inventory.model.purchaseorder;

import static java.util.Objects.requireNonNull;

import static seedu.inventory.commons.util.AppUtil.checkArgument;

/**
 * Represents the quantity of item in the purchase order to be added in the inventory upon approval.
 * Guarantees: immutable; is valid as declared in {@link #isValidQuantity(String)}
 */
public class Quantity {


    public static final String MESSAGE_QUANTITY_CONSTRAINTS =
            "Quantity should only contain numbers, and it should not start with a 0";
    public static final String QUANTITY_VALIDATION_REGEX = "^[1-9]\\d+";
    public final String value;

    /**
     * Constructs a {@code Quantity}.
     *
     * @param quantity A valid quantity amount
     */
    public Quantity(String quantity) {
        requireNonNull(quantity);
        checkArgument(isValidQuantity(quantity), MESSAGE_QUANTITY_CONSTRAINTS);
        value = quantity;
    }

    /**
     * Returns true if a given string is a valid quantity.
     */
    public static boolean isValidQuantity(String test) {
        return test.matches(QUANTITY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Quantity // instanceof handles nulls
                && value.equals(((Quantity) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}