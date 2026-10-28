
package com.codegnan.E_Commerce;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ecommerce.entity.Address;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.util.HibernateUtil;

public class MainApp {

    public static void main(String[] args) {

        try (SessionFactory factory =
                 HibernateUtil.getSessionFactory()) {

            Long customerId;
            Long orderId;
            Long productId;

            // 1. INSERT SAMPLE DATA
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Address address = new Address(
                        "MG Road", "Bengaluru", "560001"
                    );

                    Customer customer = new Customer(
                        "Kushal", "kushal@example.com"
                    );

                    customer.setAddress(address);

                    Product laptop = new Product(
                        "Gaming Laptop", 65000.00
                    );

                    Product mouse = new Product(
                        "Wireless Mouse", 1200.00
                    );

                    Product keyboard = new Product(
                        "Mechanical Keyboard", 2500.00
                    );

                    session.persist(laptop);
                    session.persist(mouse);
                    session.persist(keyboard);

                    Order order1 = new Order(
                        "2026-10-28", "PLACED"
                    );

                    order1.addProduct(laptop);
                    order1.addProduct(mouse);

                    Order order2 = new Order(
                        "2026-10-28", "PROCESSING"
                    );

                    order2.addProduct(mouse);
                    order2.addProduct(keyboard);

                    customer.addOrder(order1);
                    customer.addOrder(order2);

                    session.persist(customer);

                    tx.commit();

                    customerId = customer.getCustomerId();
                    orderId = order1.getOrderId();
                    productId = laptop.getProductId();

                    System.out.println("\nDATA INSERTED");
                    System.out.println("Customer ID: " + customerId);

                } catch (RuntimeException e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

            // 2. FETCH CUSTOMER, ADDRESS, ORDERS, PRODUCTS
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Customer customer =
                        session.get(Customer.class, customerId);

                    if (customer != null) {
                        System.out.println("\nCUSTOMER DETAILS");
                        System.out.println(customer.getName());
                        System.out.println(customer.getEmail());

                        Address address = customer.getAddress();

                        if (address != null) {
                            System.out.println("\nADDRESS");
                            System.out.println(address.getStreet());
                            System.out.println(address.getCity());
                            System.out.println(address.getPincode());
                        }

                        System.out.println("\nORDERS");

                        for (Order order : customer.getOrders()) {
                            System.out.println(
                                "Order ID: " + order.getOrderId()
                            );

                            System.out.println(
                                "Status: " + order.getStatus()
                            );

                            for (Product product : order.getProducts()) {
                                System.out.println(
                                    product.getProductName()
                                    + " - Rs. " + product.getPrice()
                                );
                            }
                        }
                    }

                    tx.commit();

                } catch (RuntimeException e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

            // 3. UPDATE ORDER
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Order order = session.get(Order.class, orderId);

                    if (order != null) {
                        order.setStatus("SHIPPED");
                        tx.commit();

                        System.out.println("\nORDER UPDATED");
                    } else {
                        tx.rollback();
                        System.out.println("Order not found.");
                    }

                } catch (RuntimeException e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

            // 4. DELETE PRODUCT
            try (Session session = factory.openSession()) {

                Transaction tx = session.beginTransaction();

                try {
                    Product product =
                        session.get(Product.class, productId);

                    if (product != null) {

                        for (Order order : product.getOrders()) {
                            order.getProducts().remove(product);
                        }

                        product.getOrders().clear();

                        session.remove(product);
                        tx.commit();

                        System.out.println("PRODUCT DELETED");

                    } else {
                        tx.rollback();
                        System.out.println("Product not found.");
                    }

                } catch (RuntimeException e) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }

        } catch (RuntimeException e) {
            System.err.println("Application failed:");
            e.printStackTrace();
        }
    }
}
