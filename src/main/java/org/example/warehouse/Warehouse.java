package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    //Lagrar unika instanser av warehouse med namn som nycklar.
    private static final Map<String, Warehouse> instances = new HashMap<>();
    //Representerar namnet på lagret
    private final String name;
    //Map som kopplar produktens uuid till productrecord.
    private final Map<UUID, ProductRecord> products;
    //Lista som håller koll på produkter vars pris har ändrats.
    private final List<ProductRecord> changedProducts;

    /*
    En privat konstruktor som sätter upp ett lager med standardinställningar
    för att alla nya instanser av warehouse ska börja i ett väldefinierat tillstånd.
     */
    private Warehouse() {
        this.name = "Warehouse";
        this.products = new LinkedHashMap<>();
        this.changedProducts = new LinkedList<>();
    }

    private Warehouse(String name) {
        this.name = name; //Sätter lagrets namn
        this.products = new LinkedHashMap<>(); // Lagrar produkterna i lagret,
        //och håller koll på produkterna via linked hashmap.
        this.changedProducts = new LinkedList<>(); // Lista som håller koll på produkter som ändrats t.ex pris
    }

    //Hämtar en ny instans av warehouse
    public static Warehouse getInstance() {
        return new Warehouse();
    }

    //Kontrollerar om Instansnamnet redan finns! Om ja kastas en exeption, om ne görs en ny.
    public static Warehouse getInstance(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Warehouse name can't be null or empty");
        }
        return instances.computeIfAbsent(name, Warehouse::new);
    }

    //Returnerar lagrets namn
    public String getName() {
        return name;
    }


    //Kontrollerar om lagret är tomt
    public boolean isEmpty() {
        return products.isEmpty();
    }

    //Returnerar en lista som ej går att förändra på produktlagret
    public List<ProductRecord> getProducts() {
        return List.copyOf(products.values());
    }


    //Metod som tar emot parametrar för att skapa en ny produkt (om alla delar går igenom).
    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {

        //Om ett namn inte givits kastas en exeption.
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }

        //Om UUID redan finns kastas exeption
        if (products.containsKey(uuid)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        //Om kategori inte finns kastas exeption (Produkten måste ha en giltig kategori)
        if (category == null) { // Kolla om kategorin är null
            throw new IllegalArgumentException("Category can't be null."); // Här ska undantaget kastas
        }

        //Om inget pris givits sätts det till 0
        if (price == null) {
            price = BigDecimal.ZERO;
        }

        //Om UUID inte finns skapas ett nytt
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        //När allt gått igenom skapas en ny instans av klassen productrecord, och läggs till i products
        //mapen med UUID som nyckel.
        ProductRecord newProduct = new ProductRecord(uuid, name, category, price);
        products.put(uuid, newProduct);
        return newProduct;
    }

    // tar emot uuid som parameter för söka efter produkten.
    //Resultatet omsluts av en optional för att hantera resultatet.
    public Optional<ProductRecord> getProductById(UUID uuid) {
        return Optional.ofNullable(products.get(uuid));
    }

    //Tar uuid och BigDecimal som parameter. Om null kastas exeption annars uppdateras
    //Läggs den i changedproducts och ersätter den gamla i productsmappen
    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {

        ProductRecord existingProduct = products.get(uuid);


        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }


        ProductRecord updatedProduct = new ProductRecord(existingProduct.uuid(),
                existingProduct.name(), existingProduct.category(), newPrice);
        changedProducts.add(existingProduct);
        products.put(uuid, updatedProduct);
    }


    /*
    Hämtar alla värden
    Gör om det till en stream för att bearbeta
    4 raden, grupperar productrecords efter cateogry
    innan den returneras görs listan oföränderlig.
     */
    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return Collections.unmodifiableMap(
                products.values().stream()
                        .collect(Collectors.groupingBy(ProductRecord::category)));
    }

    //tar ut en unmodifiablelist av produkterna som ändrats.
    public List<ProductRecord> getChangedProducts() {
        return Collections.unmodifiableList(changedProducts);
    }


    /*
    Tar emot category som parameter, hämtar alla produkter och returnerar en samling,
    omvandlar till en stream för enkel bearbetning
    filtrerar för att välja ut de produkter som fyller de krav som skickas in
    gör en lista och skriver ut det som en unmodefiable list.
     */
    public List<ProductRecord> getProductsBy(Category category) {
        return Collections.unmodifiableList(
                products.values().stream()
                        .filter(product -> product.category().equals(category))
                        .collect(Collectors.toList()));
    }
}