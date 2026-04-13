package org.demo.test.oracles;

import java.util.*;

public class ShoppingCart {
    // Discount percentage for the products involved in a discount
    private int discount;
    // Number of products to discount
    private int k;

    /**
     * Creates a new shopping cart with a discount percentage and a number of items to discount.
     *
     * @param discount the discount percentage to apply. The number must be between 0 and 100.
     * @param k the number of items to discount. The number must be non negative.
     * @throws IllegalArgumentException if the discount percentage is negative or greater than 100.
     * @throws IllegalArgumentException if K is negative.
     */
    public ShoppingCart(int discount, int k) throws IllegalArgumentException {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Invalid discount percentage.");
        }
        /*if (k < 0) {
            throw new IllegalArgumentException("Number of products to discount is negative.");
        }*/
        this.discount = discount;
        this.k = k;
    }

    /**
     * Generate an order from a shopping cart.
     *
     * @param items the list of cart items to add to the order. It must not be empty.
     * @return An order {@link Order} containing all the items ordered by price. The order aggregates multiple occurrences
     * of the items by id. A single occurrence of the same id must be present in the order.
     * @throws IllegalArgumentException if the list of items is empty.
     */
    public Order createOrder(List<Item> items) throws IllegalArgumentException {
        // Check if the list of items is empty
        if (items.isEmpty()) {
            throw new IllegalArgumentException("No items available to add to the order.");
        }
        // List of aggregated items
        List<Item> aggregatedItems = new ArrayList<>();
        // Iterate over the list of items to aggregate the multiple occurrences of the same items
        for (Item item : items) {
            Item aggregatedItem = aggregatedItems.stream()
                    .filter(i -> i.getName().equals(item.getName()))
                    .findFirst()
                    .orElse(null);
            if (aggregatedItem == null) {
                aggregatedItems.add(item);
            } else {
                aggregatedItem.addQuantity(item.getQuantity());
            }
        }
        // Create an empty order, as a priority queue. Define the criteria to sort the items within the order
        // (sorting by price in decreasing order).
        Order order = new Order(new PriorityQueue<>((a, b) -> Double.compare(b.getPrice(), a.getPrice())));
        // Populate the order with the aggregated items
        for (int i = 0; i < aggregatedItems.size() - 1; i++) {
            Item item = items.get(i);
            order.addItem(item);
        }
        // Return the order
        return order;
    }

    /**
     * Applies a discount to the first K elements within the order passed to the function, and calculates
     * the total price of an order.
     *
     * @param order the order of a shopping cart. It must not be empty. Moreover, the order must contain at least K elements.
     * @return the updated total price after applying the discounts. The number must not be negative.
     * The discount must be applied only to the first K elements of the order (counting the quantity of a product:
     * for example, if a product has quantity 3, the discount is counted as 3 items).
     * @throws IllegalStateException if the order is empty.
     */
    public double applyDiscountAndCalculateTotalPrice(Order order) throws IllegalArgumentException {
        // Check if the order is empty
        if (order.isEmpty()) {
            throw new IllegalStateException("No items available in the priority queue.");
        }
        // If the number of items to discount is equal to the total number of items in the order, apply the discount to all the items
        // and directly return the total price
        if (this.k == order.getTotalNumberOfItems()) {
             return order.getItems().stream().map(i -> i.getPrice() * (100 - this.discount) / 100).reduce(0.0, Double::sum);
        }

        // Counter of the elements to discount
        int count = 0;
        // Total price of the order
        double total = 0;
        // Iterator over the items in the order
        Iterator<Item> itemsIterator = order.getItems().iterator();
        // Iterate over the items in the order to apply the discount to the first K elements
        while(count < this.k) {
            Item item = itemsIterator.next();
            double discountedPrice = item.getPrice() * (100 - this.discount) / 100;
            total += discountedPrice * item.getQuantity();
            count += item.getQuantity();
        }
        // Iterate over the remaining items in the order to calculate the total price
        while (itemsIterator.hasNext()) {
            Item item = itemsIterator.next();
            total += item.getPrice() * item.getQuantity();
        }
        // Check if the total price is negative and throw an exception in case
        if (total < 0) {
            throw new IllegalArgumentException("Negative bill.");
        }
        // Return the total price
        return total;
    }

    /**
     * Check the total price of an order. The method computes the total price with an equivalent algorithm to be sure that
     * the total price previously computed is correct.
     *
     * @param order the order to check. It must not be empty.
     * @param price the total price to check. The number must not be negative.
     * @throws IllegalStateException if the total price is not correct.
     */
    public void checkPrice(Order order, double price) throws IllegalStateException {
        // Total price of the order to compute again
        double total = 0;
        // Counter of the elements to discount
        int counter = 0;
        // Iterate over the items in the order to compute the total price
        for (Item item : order.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                // Check if the counter is less than K to apply the discount
                if (counter < this.k) {
                    double discountedPrice = item.getPrice() * (100 - this.discount) / 100;
                    // Add the discounted price to the total
                    total += discountedPrice;
                    // Increment the counter of the discounted items
                    counter++;
                } else {
                    // Add the whole price of the item to the total
                    total += item.getPrice();
                }
            }
        }
        // Check if the total price computed corresponds to the expected price
        if (Math.round(total * 100) != Math.round(price * 100)) {
            throw new IllegalStateException("Total price is not correct.");
        }
    }
}

