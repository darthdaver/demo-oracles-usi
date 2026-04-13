package org.demo.test.oracles;

import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    /**
     * Main method to run the shopping program. The method defines:
     * <ol>
     *     <li>a list of cart items. Each item is referenced by an identifier, a name and a price. The cart item contains
     *     also the quantity of the product to add in the cart.</li>
     *     <li>a discount percentage (defined as an int)</li>
     *     <li>the number of items to discount. The value takes into account the quantity, for each item in the cart.</li>
     * </ol>
     *
     * Play with the values to see how the program behaves.
     *
     * @param args the command line arguments (not used in this program).
     */
    public static void main(String[] args) {

        // Add initial list of items
        List<Item> cartItems = new ArrayList<>();
        cartItems.add(new Item(1,"Apple", 0.99, 2));
        cartItems.add(new Item(2, "Banana", 0.49, 4));
        cartItems.add(new Item(1, "apple", 0.99, 2));
        cartItems.add(new Item(3, "Orange", 1.49, 1));
        cartItems.add(new Item(4, "Mango", 1.99, 2));
        cartItems.add(new Item(1, "banana", 0.49, 1));
        cartItems.add(new Item(5, "Pear", 0.89, 1));

        // Discount percentage
        int discount = 10;
        // Number of items to discount (the value takes into account the quantity of each item).
        // For example, suppose that:
        //    - K is 5 and the cart contains 4 Mango items, and 3 Apples.
        //    - The Mango item is the item with higher price per unit (the order is sorted by price, in decreasing order).
        // The Mango item will be the first item in the order and the discount is applied to the first 5 items. Therefore,
        // the discount will be applied to the all the 4 Mango items, plus 1 Apple item. The last two items (2 Apples) will
        // not be discounted.
        int k = 2;
        // Run the shopping program with the defined items, discount and k
        makeShopping(cartItems, discount, k);
    }

    /**
     * Method to run the shopping program with the defined items, discount and k.
     *
     * @param items the list of items to add to the order.
     * @param discount the discount percentage to apply.
     * @param k the number of items to discount.
     */
    public static void makeShopping(List<Item> items, int discount, int k) {
        // Create a shopping cart with the discount and k items to discount as threshold
        ShoppingCart shoppingCart = new ShoppingCart(discount, k);
        // Generate an order with the given items
        Order order = shoppingCart.createOrder(items);
        // Add a gift item to the order if the total number of items in the cart is greater than 3 and for each
        // the quantity of each distinct item type (identified by the id) in the cart is 1.
        if (order.getTotalNumberOfItems() == order.getNumberofDistinctItems() && order.getTotalNumberOfItems() > 3) {
            // Select a random item from the order and add an additional item as a gift (price 0.0)
            Pair<Integer,String>[] itemsIdsNamesArray = order.getAllItemsIdsAndNamesAsArray();
            // Select a random item from the list of items
            Random random = new Random();
            int randomIndex = random.nextInt(itemsIdsNamesArray.length);
            Pair<Integer,String> randomItemPair = itemsIdsNamesArray[randomIndex];
            // Generate the gift item
            Item giftItem = new Item(randomItemPair.a, randomItemPair.b + " [GIFT]", 0.0, 1);
            // Add the gift item to the order
            order.addItem(giftItem);
        }
        // Add a gift item to the order if the number of items in the order is greater than 8
        // (independently of the number of item types)
        if (order.getTotalNumberOfItems() > 8) {
            // Select a random item from the list of original items in the cart
            Random random = new Random();
            int randomIndex = random.nextInt(order.getTotalNumberOfItems());
            Item randomItem = items.get(randomIndex);
            // Generate the gift item
            Item giftItem = new Item(randomItem.getId(), randomItem.getName() + " [GIFT]", 0.0, 1);
            // Add the gift item to the order
            order.addItem(giftItem);
        }
        // Apply the discounts to the order and count the final price
        double total = shoppingCart.applyDiscountAndCalculateTotalPrice(order);
        // Print the invoice
        System.out.printf("Total invoice (with discount applied): $%.2f\n", total);
        // Print list of items
        System.out.println("Items to be added to the shelves (with discounts):");
        for (Item item : order.getItems()) {
            System.out.println("Item: " + item.getName() + ", Quantity: " + item.getQuantity() + ", Price per unit: " + item.getPrice());
        }
        // Check the price of the order using an equivalent algorithm
        shoppingCart.checkPrice(order, total);
    }
}
