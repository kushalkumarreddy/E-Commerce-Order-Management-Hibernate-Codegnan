
package com.ecommerce.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;

public class HibernateUtil {

    private static final SessionFactory factory =
            buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        Configuration configuration =
                new Configuration().configure();

        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Product.class);

        return configuration.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }
}
