package com.ardent.epicreads.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(Book book) {
        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        CartItem newItem = new CartItem();
        newItem.setBook(book);
        newItem.setQuantity(1);
        items.add(newItem);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void updateItemQuantity(Long bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBook().getId().equals(bookId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }


    public void removeItem(Long bookId) {
        items.removeIf(item -> item.getBook().getId().equals(bookId));
    }

    // Additional methods like clearCart, getTotalPrice, etc.
}
