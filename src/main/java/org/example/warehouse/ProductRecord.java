package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductRecord {
    //Identifierare för varje produkt
    private final UUID uuid;
    //Namn för varje produkt
    private final String name;
    //Kategorin som produkten tillhör
    private final Category category;
    //Priset för produkten. BigDecimal för att hantera priser exakt och undvika avrundningsfel.
    private BigDecimal price;

    //För att skapa en ny instans av productrecord, med värden för varje attribut.
    public ProductRecord(UUID id,String name, Category category,BigDecimal price){
        this.uuid = id;
        this.name = name;
        this.category = category;
        this.price = price;

    }

    // Returnerar värdena för de privata attributen.
    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public void price(BigDecimal price) {
        this.price = price;
    }

    //Används för att kunna uppdatera priset.
    public BigDecimal price() {
        return price;
    }

    //Gör det enkelt att snabbt se objektets innehåll utan att behöva komma åt varje attribut individuellt.
    @Override
    public String toString() {
        return "ProductRecord [id=" + uuid + ", name=" + name +
                ", category=" + category + ", price=" + price + "]";
    }
}