package org.demo.test.oracles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemTest {

    private Item item;

    @Before
    public void setUp() {
        // Arrange: create a fresh Item before each test
        item = new Item(1, "Apple", 2.50, 3);
    }

//    @Test
//    public void testGetId() {
//        assertEquals(1, item.getId());
//    }

//    @Test
//    public void testGetName() {
//        assertEquals("Apple", item.getName());
//    }

//    @Test
//    public void testGetPrice() {
//        assertEquals(2.50, item.getPrice(), 0.001);
//    }

//    @Test
//    public void testGetQuantity() {
//        assertEquals(3, item.getQuantity());
//    }

//    @Test
//    public void testAddQuantity() {
//        item.addQuantity(2);
//        assertEquals(5, item.getQuantity());
//    }

//    @Test
//    public void testGetTotalPrice() {
//        // price=2.50, quantity=3 → loop runs quantity+1=4 times → total = 10.0
//        double expected = item.getPrice() * (item.getQuantity() + 1);
//        assertEquals(expected, item.getTotalPrice(), 0.001);
//    }
}