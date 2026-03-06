import java.util.*;

// ================= CATEGORY =================
class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// ================= USER =================
class User {
    protected String name;
    protected String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

// ================= SELLER =================
class Seller extends User {
    public Seller(String name, String password) {
        super(name, password);
    }
}

// ================= CUSTOMER =================
class Customer extends User {
    private String email;

    public Customer(String name, String email, String password) {
        super(name, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

// ================= PRODUCT (JEWELRY) =================
class Product {
    private int id;
    private String name;
    private String material;
    private double weight; // grams
    private double price;
    private int stock;
    private Category category;
    private Seller seller;

    public Product(int id, String name, String material, double weight, double price, int stock, Category category,
            Seller seller) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.weight = weight;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.seller = seller;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaterial() {
        return material;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public Category getCategory() {
        return category;
    }

    public Seller getSeller() {
        return seller;
    }

    public void reduceStock(int qty) {
        stock -= qty;
    }

    public void addStock(int qty) {
        stock += qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product))
            return false;
        Product p = (Product) o;
        return id == p.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

// ================= ORDER =================
class Order {
    private Map<Product, Integer> items;
    private double totalAmount;
    private boolean paid;
    private String paymentMode;
    private String qrCode;

    public Order(Map<Product, Integer> items, double totalAmount) {
        this.items = new HashMap<>(items);
        this.totalAmount = totalAmount;
        this.paid = false;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void pay(String mode) {
        this.paymentMode = mode;
        this.paid = true;
        if (mode.equalsIgnoreCase("online"))
            qrCode = "QR-" + UUID.randomUUID().toString().substring(0, 8);
    }
}

// Track customer name
class CustomerOrder extends Order {
    private String customerName;

    public CustomerOrder(String customerName, Map<Product, Integer> items, double totalAmount) {
        super(items, totalAmount);
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}

// ================= MAIN =================
public class JewelryShop {

    static Scanner sc = new Scanner(System.in);

    static List<Category> categories = new ArrayList<>();
    static List<Seller> sellers = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static List<Order> allOrders = new ArrayList<>();

    static int productCounter = 1;
    static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("         WELCOME TO JEWELRY HUB        ");
        System.out.println("=======================================");

        // Categories
        categories.add(new Category(1, "Men"));
        categories.add(new Category(2, "Women"));
        categories.add(new Category(3, "Kids"));

        addDefaultProducts();

        while (true) {
            System.out.println("\n===== JEWELRY SHOP LOGIN MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Seller");
            System.out.println("3. Customer");
            System.out.println("4. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    sellerAccount();
                    break;
                case 3:
                    customerAccount();
                    break;
                case 4:
                    System.out.println("Thank you for visiting Jewelry Hub!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void addDefaultProducts() {
        Seller systemSeller = new Seller("GemMaster", "1234");
        sellers.add(systemSeller);

        // Men Jewelry
        products.add(
                new Product(productCounter++, "Gold Cufflinks", "Gold", 15, 300, 10, categories.get(0), systemSeller));
        products.add(
                new Product(productCounter++, "Silver Tie Pin", "Silver", 10, 80, 15, categories.get(0), systemSeller));

        // Women Jewelry
        products.add(new Product(productCounter++, "Diamond Earrings", "Diamond", 5, 500, 5, categories.get(1),
                systemSeller));
        products.add(
                new Product(productCounter++, "Gold Necklace", "Gold", 20, 1200, 3, categories.get(1), systemSeller));
        products.add(new Product(productCounter++, "Silver Bracelet", "Silver", 12, 200, 7, categories.get(1),
                systemSeller));

        // Kids Jewelry
        products.add(
                new Product(productCounter++, "Kids Gold Ring", "Gold", 3, 100, 8, categories.get(2), systemSeller));
        products.add(new Product(productCounter++, "Beaded Bracelet", "Plastic Beads", 5, 20, 20, categories.get(2),
                systemSeller));
    }

    // ================= ADMIN =================
    static void adminLogin() {
        System.out.print("Enter Admin Password: ");
        String pass = sc.nextLine();

        if (!pass.equals(ADMIN_PASSWORD)) {
            System.out.println("Wrong password!");
            return;
        }

        while (true) {
            System.out.println("\n===== ADMIN PANEL =====");
            System.out.println("1. View Products");
            System.out.println("2. View Sales Report");
            System.out.println("3. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    viewAdminSalesReport();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void viewAdminSalesReport() {

        double totalStoreIncome = 0;
        Map<String, Double> sellerIncome = new HashMap<>();

        for (Order o : allOrders) {
            if (!o.isPaid())
                continue;
            totalStoreIncome += o.getTotalAmount();

            for (Product p : o.getItems().keySet()) {
                int qty = o.getItems().get(p);
                double sellerEarned = p.getPrice() * qty;

                String sellerName = p.getSeller().getName();
                sellerIncome.put(sellerName, sellerIncome.getOrDefault(sellerName, 0.0) + sellerEarned);
            }
        }

        System.out.println("\n===== SALES REPORT =====");
        System.out.println("Total Store Income: $" + totalStoreIncome);
        System.out.println("Total Orders: " + allOrders.size());

        System.out.println("\n---- Seller Earnings ----");
        if (sellerIncome.isEmpty()) {
            System.out.println("No sales yet.");
            return;
        }
        for (String seller : sellerIncome.keySet()) {
            System.out.println("Seller: " + seller + " | Earned: $" + sellerIncome.get(seller));
        }
    }

    // ================= SELLER =================
    static void sellerAccount() {
        while (true) {
            System.out.println("\n===== SELLER MENU =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Name: ");
                String name = sc.nextLine();

                for (Seller s : sellers)
                    if (s.getName().equalsIgnoreCase(name)) {
                        System.out.println("Seller already exists!");
                        return;
                    }

                System.out.print("Password: ");
                String pass = sc.nextLine();
                sellers.add(new Seller(name, pass));
                System.out.println("Registered successfully!");
            } else if (choice == 2) {
                System.out.print("Enter Name: ");
                String name = sc.nextLine();

                Seller found = null;
                for (Seller s : sellers)
                    if (s.getName().equalsIgnoreCase(name))
                        found = s;

                if (found == null) {
                    System.out.println("Seller not found!");
                    return;
                }

                System.out.print("Password: ");
                String pass = sc.nextLine();
                if (!found.getPassword().equals(pass)) {
                    System.out.println("Incorrect password!");
                    return;
                }

                sellerMenu(found);
            } else
                return;
        }
    }

    static void sellerMenu(Seller seller) {
        while (true) {
            System.out.println("\n===== SELLER PANEL =====");
            System.out.println("1. Add Jewelry");
            System.out.println("2. View My Jewelry");
            System.out.println("3. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("Select Category:");
                    for (Category c : categories)
                        System.out.println(c.getId() + ". " + c.getName());

                    int cid = sc.nextInt();
                    sc.nextLine();
                    Category selected = null;
                    for (Category c : categories)
                        if (c.getId() == cid)
                            selected = c;

                    if (selected == null) {
                        System.out.println("Invalid category!");
                        break;
                    }

                    System.out.print("Jewelry Name: ");
                    String name = sc.nextLine();

                    System.out.print("Material: ");
                    String material = sc.nextLine();

                    System.out.print("Weight (grams): ");
                    double weight = sc.nextDouble();

                    System.out.print("Price: ");
                    double price = sc.nextDouble();

                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();

                    products.add(new Product(productCounter++, name, material, weight, price, stock, selected, seller));
                    System.out.println("Jewelry added!");
                    break;

                case 2:
                    System.out.println("Your Jewelry:");
                    for (Product p : products)
                        if (p.getSeller() == seller)
                            System.out.println(p.getId() + ". " + p.getName() +
                                    " | " + p.getMaterial() +
                                    " | " + p.getWeight() + "g" +
                                    " | $" + p.getPrice() +
                                    " | Stock: " + p.getStock());
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // ================= CUSTOMER =================
    static void customerAccount() {
        while (true) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Name: ");
                String name = sc.nextLine();

                for (Customer c : customers)
                    if (c.getName().equalsIgnoreCase(name)) {
                        System.out.println("User already exists!");
                        return;
                    }

                System.out.print("Email: ");
                String email = sc.nextLine();

                System.out.print("Password: ");
                String pass = sc.nextLine();

                customers.add(new Customer(name, email, pass));
                System.out.println("Registered successfully!");
            } else if (choice == 2) {
                System.out.print("Name: ");
                String name = sc.nextLine();

                Customer found = null;
                for (Customer c : customers)
                    if (c.getName().equalsIgnoreCase(name))
                        found = c;

                if (found == null) {
                    System.out.println("User not found!");
                    return;
                }

                System.out.print("Password: ");
                String pass = sc.nextLine();

                if (!found.getPassword().equals(pass)) {
                    System.out.println("Incorrect password!");
                    return;
                }

                customerMenu(found);
            } else
                return;
        }
    }

    static void customerMenu(Customer customer) {

        Map<Product, Integer> cart = new HashMap<>();
        List<Order> myOrders = new ArrayList<>();

        while (true) {
            System.out.println("\n===== CUSTOMER PANEL =====");
            System.out.println("1. View Jewelry by Category");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Refund");
            System.out.println("6. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("Select Category:");
                    for (Category c : categories)
                        System.out.println(c.getId() + ". " + c.getName());

                    int catId = sc.nextInt();
                    sc.nextLine();
                    Category cat = null;
                    for (Category c : categories)
                        if (c.getId() == catId)
                            cat = c;

                    if (cat == null) {
                        System.out.println("Invalid category!");
                        break;
                    }

                    System.out.println("Jewelry in " + cat.getName() + ":");
                    boolean hasProducts = false;
                    for (Product p : products)
                        if (p.getCategory().getId() == cat.getId()) {
                            System.out.println(p.getId() + ". " + p.getName() +
                                    " | " + p.getMaterial() +
                                    " | " + p.getWeight() + "g" +
                                    " | $" + p.getPrice() +
                                    " | Stock: " + p.getStock() +
                                    " | Seller: " + p.getSeller().getName());
                            hasProducts = true;
                        }
                    if (!hasProducts)
                        System.out.println("No jewelry in this category.");
                    break;

                case 2:
                    System.out.print("Enter Jewelry ID: ");
                    int pid = sc.nextInt();

                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine();

                    Product prod = null;
                    for (Product p : products)
                        if (p.getId() == pid)
                            prod = p;

                    if (prod == null || qty > prod.getStock()) {
                        System.out.println("Invalid product or insufficient stock!");
                        break;
                    }

                    cart.put(prod, cart.getOrDefault(prod, 0) + qty);
                    System.out.println("Added to cart!");
                    break;

                case 3:
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty!");
                        break;
                    }
                    double total = 0;
                    for (Product p : cart.keySet()) {
                        int q = cart.get(p);
                        double sub = q * p.getPrice();
                        total += sub;
                        System.out.println(p.getName() + " x" + q + " = $" + sub);
                    }
                    System.out.println("Total: $" + total);
                    break;

                case 4:
                    if (cart.isEmpty()) {
                        System.out.println("Cart is empty!");
                        break;
                    }

                    double totalAmount = 0;
                    for (Product p : cart.keySet())
                        totalAmount += p.getPrice() * cart.get(p);

                    Order order = new CustomerOrder(customer.getName(), cart, totalAmount);

                    System.out.println("Payment Mode (Cash/Online): ");
                    String mode = sc.nextLine();
                    order.pay(mode);

                    if (mode.equalsIgnoreCase("online"))
                        System.out.println("Scan QR: " + order.getQrCode());

                    for (Product p : cart.keySet())
                        p.reduceStock(cart.get(p));

                    myOrders.add(order);
                    allOrders.add(order);
                    cart.clear();

                    System.out.println("Payment successful! Total Paid: $" + totalAmount);
                    break;

                case 5:
                    if (myOrders.isEmpty()) {
                        System.out.println("No orders to refund!");
                        break;
                    }

                    for (int i = 0; i < myOrders.size(); i++)
                        System.out.println(i + ". $" + myOrders.get(i).getTotalAmount() +
                                " | Paid: " + myOrders.get(i).getPaymentMode());

                    System.out.print("Select order to refund: ");
                    int idx = sc.nextInt();
                    sc.nextLine();

                    if (idx < 0 || idx >= myOrders.size()) {
                        System.out.println("Invalid selection!");
                        break;
                    }

                    Order refundOrder = myOrders.get(idx);
                    for (Product p : refundOrder.getItems().keySet())
                        p.addStock(refundOrder.getItems().get(p));

                    allOrders.remove(refundOrder);
                    myOrders.remove(idx);

                    System.out.println("Refund successful! Stock restored.");
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void viewAllProducts() {
        System.out.println("===== JEWELRY SHOP PRODUCT LIST =====");
        for (Product p : products) {
            System.out.println(
                    p.getId() + ". " +
                            p.getName() +
                            " | " + p.getMaterial() +
                            " | " + p.getWeight() + "g" +
                            " | $" + p.getPrice() +
                            " | Stock: " + p.getStock() +
                            " | Seller: " + p.getSeller().getName() +
                            " | Category: " + p.getCategory().getName());
        }
    }
}