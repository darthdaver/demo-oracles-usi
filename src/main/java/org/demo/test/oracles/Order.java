package org.demo.test.oracles;

import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Order {

    // List of items in the order, ordered by price in decreasing order
    PriorityQueue<Item> items;

    /**
     * Creates a new order with a list of items.
     *
     * @param items the list of items to add to the order. The items are ordered by price in decreasing order.
     */
    public Order(PriorityQueue<Item> items) {
        this.items = items;
    }

    /**
     * Add an item to the order.
     *
     * @param item the item to add to the order. It must not be null.
     * @throws IllegalArgumentException if the item is null.
     */
    public void addItem(Item item)  {
        items.add(item);
    }

    /**
     * Returns the list of items in the order.
     *
     * @return the list of items in the order. The list is ordered by price in decreasing order.
     */
    public PriorityQueue<Item> getItems() {
        return items;
    }

    /**
     * Set the list of items in the order.
     *
     * @param items the list of items to add to the order.
     */
    public void setItems(PriorityQueue<Item> items) {
        this.items = items;
    }

    /**
     * Returns the number of distinct items in the order (the items are distinguished by their id).
     *
     * @return the number of distinct items in the order.
     */
    public int getNumberofDistinctItems() {
        return items.size();
    }

    /**
     * Returns the flat list of item ids and names, in the form of array. If a single item has a quantity greater than 1,
     * the item will be repeated in the list as many times as its quantity.
     *
     * @return the flat list of item ids and names. The size of the array is equal to the total number of items in the order,
     * considering the quantity of each item.
     */
    public Pair<Integer,String>[] getAllItemsIdsAndNamesAsArray() {
        Pair<Integer,String>[] itemsIdsArray = (Pair<Integer,String>[]) new Pair<?,?>[this.getNumberofDistinctItems()];
        int itemIndex = 0;
        for (Item item : items) {
            for (int j = 0; j < item.getQuantity(); j++) {
                itemsIdsArray[itemIndex+j] = new Pair(item.getId(), item.getName());
            }
            itemIndex += item.getQuantity();
        }
        return itemsIdsArray;
    }

    /**
     * Returns the total number of items in the order (considering the quantity of each item in the order).
     *
     * @return the total number of items in the order.
     */
    public int getTotalNumberOfItems() {
        int count = 0;
        for (Item item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    /**
     * Returns true if the order is empty.
     *
     * @return true if the order is empty, false otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
