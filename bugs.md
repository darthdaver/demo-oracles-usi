# Bugs
The report describes the list of bugs injected in the program

## Item.java

### Bug 1 - Off-by-one in `getTotalPrice()`
```java
// BUGGY
for (int i = 0; i <= quantity; i++) { 
    total += price; 
}

// FIXED
for (int i = 0; i < quantity; i++) { 
    total += price; 
}
```
The loop uses `<=` instead of `<`, so it iterates `quantity + 1 times`, computing one extra price in the total. 
Alternatively, the whole loop can simply be replaced with `return price * quantity`;

### Bug 2 - `Constructor` missing input validation
The Javadoc states the constructor should throw `IllegalArgumentException` if the name is empty or the `price is ≤ 0`, but 
the constructor body contains no validation at all — it just assigns fields unconditionally. Items with blank names, 
zero prices, or negative prices are silently accepted.

## ShoppingCart

### Bug 3 - Validation for k < 0 is commented out in `constructor`
```java
// BUGGY — entire guard is commented out
/*if (k < 0) {
    throw new IllegalArgumentException("Number of products to discount is negative.");
}*/
```
The Javadoc explicitly promises an IllegalArgumentException for negative k, but the guard was intentionally disabled. 
Any negative value of k is accepted silently, which will corrupt the discount logic downstream.

### Bug 4 - Aggregation uses name instead of id in `createOrder()`
```java
// BUGGY
.filter(i -> i.getName().equals(item.getName()))

// FIXED
.filter(i -> i.getId() == item.getId())
```
The `README` specifies that the item `id` is the key for merging duplicates. Matching on `name` is semantically wrong — two 
distinct products could share the same name but have different IDs.

### Bug 5 - Off-by-one in `createOrder()`: last aggregated item is never added to the order
```java
// BUGGY — stops one short
for (int i = 0; i < aggregatedItems.size() - 1; i++) { ... }

// FIXED
for (int i = 0; i < aggregatedItems.size(); i++) { ... }
```
The loop condition `size - 1` means the very last element of `aggregatedItems` is never inserted into the order.

### Bug 6 - Loop reads from the wrong list in `createOrder()`
```java
// BUGGY — reads from the raw (un-aggregated) input list
Item item = items.get(i);

// FIXED — should read from the aggregated list
Item item = aggregatedItems.get(i);
```
Even when the index is correct, the loop adds items from items (the original input) rather than aggregatedItems. 
All the aggregation work done above is therefore ignored, and duplicate items are never merged.

### Bug 7 - Missing `quantity` multiplier in the "all-items" shortcut in `applyDiscountAndCalculateTotalPrice()`
```java
// BUGGY — ignores quantity
return order.getItems().stream()
    .map(i -> i.getPrice() * (100 - this.discount) / 100)
    .reduce(0.0, Double::sum);

// FIXED
return order.getItems().stream()
    .map(i -> i.getPrice() * i.getQuantity() * (100 - this.discount) / 100)
    .reduce(0.0, Double::sum);
```
When `k == getTotalNumberOfItems()` the code takes a fast path that streams over distinct items but multiplies only by price, 
completely ignoring each item's quantity. For any item with `quantity > 1` the total will be severely underestimated.

### Bug 8 - Discount may be over-applied when an item straddles the k boundary in `applyDiscountAndCalculateTotalPrice()`
```java
// BUGGY — applies discount to the full quantity even if count + quantity > k
total += discountedPrice * item.getQuantity();
count += item.getQuantity();
```
If `count = 0`, `k = 2`, and an `item has quantity = 3`, the loop applies the discounted price to all 3 units, but only 
2 should receive the discount. The count variable correctly stops the loop after this iteration, but the arithmetic inside 
is wrong. The fix is to split the item's units:

```java
int unitsToDiscount = Math.min(item.getQuantity(), this.k - count);
int unitsFullPrice  = item.getQuantity() - unitsToDiscount;
total += item.getPrice() * (100 - this.discount) / 100 * unitsToDiscount;
total += item.getPrice() * unitsFullPrice;
count += item.getQuantity();
```

## Order

### Bug 9 - Array is under-sized in `getAllItemsIdsAndNamesAsArray()`
```java
// BUGGY — array sized by number of distinct items
Pair[] itemsIdsArray = (Pair[]) new Pair<?,?>[this.getNumberofDistinctItems()];

// FIXED — array must be sized by total items (summing quantities)
Pair[] itemsIdsArray = (Pair[]) new Pair<?,?>[this.getTotalNumberOfItems()];
```
The method's own Javadoc states the array size should equal the total number of items counting all quantities. Using 
`getNumberofDistinctItems()` gives an array that is too small whenever any item has `quantity > 1`, causing an 
`ArrayIndexOutOfBoundsException` at runtime.

## Main

### Bug 10 - IndexOutOfBoundsException in the second gift block in `makeShopping()`
```java
// BUGGY — bounds come from order's total item count (sum of quantities)
int randomIndex = random.nextInt(order.getTotalNumberOfItems());
Item randomItem = items.get(randomIndex);  // indexes into the raw input list!
```
`order.getTotalNumberOfItems()` sums up all quantities across all items, while items is the raw input list whose size is 
the number of distinct input entries. If the order contains, say, 9 total items across 3 distinct entries, randomIndex can 
be up to 8, but `items.get(5)` (for example) throws `IndexOutOfBoundsException`. The fix is `random.nextInt(items.size())`.

### Bug 11 - Unintended double-gifting due to sequential if blocks in `makeShopping()`
```java
if (/* condition 1: all qty==1 AND total > 3 */) {
    order.addItem(giftItem);  // mutates the order's total count
}
if (order.getTotalNumberOfItems() > 8) { // reads the *updated* count!
    order.addItem(giftItem);
}
```
Both conditions share the same live order object. After the first if adds a gift, `getTotalNumberOfItems()` increases by 1. 
A cart with exactly 8 distinct items (all `qty = 1`) would: satisfy condition 1 → add gift → total becomes 9 → satisfy 
condition 2 → add a second gift. This double-gifting is almost certainly unintentional. The second check should capture 
the count before the first block, or use else if.

### Bug 12 - Gift item price 0.0 violates Item's documented contract (latent) in `makeShopping()`
```java
Item giftItem = new Item(randomItemPair.a, randomItemPair.b + " [GIFT]", 0.0, 1);
```
The Item Javadoc states the price "must be positive" and promises an `IllegalArgumentException` for zero. Currently nothing 
throws because the constructor validation is entirely missing (Bug #2). If that missing guard were ever restored, both 
gift item creations would throw `IllegalArgumentException`.