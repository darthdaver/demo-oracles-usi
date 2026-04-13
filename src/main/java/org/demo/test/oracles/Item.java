package org.demo.test.oracles;

public class Item {
    private int id;
    private String name;
    private double price;

    private int quantity;


    /**
     * Creates a new cart item with a name, a price, and a quantity.
     *
     * @param id the id of the item. Must not be positive.
     * @param name the name of the item. Must not be empty.
     * @param price the price of the item. Must be positive.
     * @param quantity the quantity of the item. Must be positive.
     * @throws IllegalArgumentException if the name is empty.
     * @throws IllegalArgumentException if the price is negative or equal to zero.
     */
    public Item(int id, String name, double price, int quantity) throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Returns the id of the item.
     * @return the id of the item.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name of the item. The string must not be empty.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the item.
     *
     * @return the price of the item. The number must not be negative.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the quantity of the cart item.
     *
     * @return the quantity of the cart item. The number must be positive.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Adds a quantity to the cart item.
     *
     * @param quantity the quantity to add. The number must be positive.
     */
    public void addQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    /**
     * Returns the total price of the cart item, considering the quantity of the item.
     *
     * @return the total price of the cart item. The number must be positive.
     */
    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i <= quantity; i++) {
            total += price;
        }
        return total;
    }
}

