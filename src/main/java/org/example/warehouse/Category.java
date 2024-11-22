package org.example.warehouse;

import java.util.*;


public class Category {
    //Lagrar namnet på kategorin
    private final String name;
    //Statisk karta som lagrar olika instanser av kategorier.
    private static final Map<String, Category> categories = new HashMap<>(); // Karta för att lagra unika kategorier


    //Tar en string som representerar namnet på kategorin.
    private Category(String name) {
        //Namnet sparas i instansvariabeln namn så man kan använda den i andra metoder.
        this.name = name;
    }

    // tar en string som parameter som representerar namnet på kategorin.
    public static Category of(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name can't be null");
        }

        //Skrivs ut med stor förstabokstav
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        //Försöker hämta en kategori, om kategori inte finns skapas en ny.
        return categories.computeIfAbsent(capitalized, Category::new);
    }

    //Hämtar namnet på kategorin
    public String getName() {
        return name;
    }
}