package com.example.myapplication.Models;

public class PackingItems {
    private String item;

    public PackingItems() {
    }

    public String getItem() {
        return item;
    }

    public PackingItems setItem(String item) {
        this.item = item;
        return this;
    }

    @Override
    public String toString() {
        return "packingItems{" +
                "item='" + item + '\'' +
                '}';
    }
}
