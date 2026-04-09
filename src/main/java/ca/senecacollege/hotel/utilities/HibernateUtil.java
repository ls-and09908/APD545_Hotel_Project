package ca.senecacollege.hotel.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    private HibernateUtil() {}
    public static synchronized SessionFactory buildSessionFactory() {

        if (sessionFactory != null) {
            return sessionFactory;
        }

        StandardServiceRegistry registry = null;
        try{
            Properties settings = new Properties();
            try(InputStream in = HibernateUtil.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties")){
                if(in!=null){
                    settings.load(in);
                    logger.info("Loaded Hibernate from Application Properties");
                }
                else{
                    logger.info("No application.properties found on the class path");
                }
            }catch (Exception e){
                logger.warn("Failed, to load application properties, continuing with defaults",e.getMessage());
            }

            settings.putIfAbsent("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver");
            settings.putIfAbsent("hibernate.connection.url","jdbc:mysql://localhost:3306/hotel_test");
            settings.putIfAbsent("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
            settings.putIfAbsent("hibernate.hbm2ddl.auto", "create");
            settings.putIfAbsent("hibernate.show_sql", "true");
            settings.putIfAbsent("hibernate.format_sql", "true");
            settings.putIfAbsent("hibernate.current_session_context_class", "thread");
            settings.putIfAbsent("hibernate.connection.pool_size", "5");
            settings.putIfAbsent("hibernate.temp.use_jdbc_metadata_defaults", "false");
            settings.putIfAbsent("org.jboss.logging.provider", "slf4j");
            settings.putIfAbsent("hibernate.jndi.class", "org.hibernate.naming.internal.NoJndiService");
            settings.putIfAbsent("jakarta.persistence.jdbc.user","user");
            settings.putIfAbsent("jakarta.persistence.jdbc.password","123Password");

            //build registry
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder.applySettings(settings);
            registry = builder.build();

            //Register annotate entity classes
            MetadataSources metadataSources = new MetadataSources(registry);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Feedback.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Guest.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Reservation.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.AdminUser.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.AuditLog.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Billing.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Charge.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Payment.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.AddOn.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Role.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Room.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.Waitlist.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.RoomSet.class);
            metadataSources.addAnnotatedClass(
                    ca.senecacollege.hotel.models.LoyaltyTransaction.class);


            //Build metadata and sessionfactory
            Metadata metadata = metadataSources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();

            logger.info("Created Hibernate SessionFactory/ build Successfully");
            return sessionFactory;
        }catch (Exception e){
            try{
                StandardServiceRegistryBuilder.destroy(registry);

            }catch (Exception e2){
                logger.warn("Failed to destroy Hibernate SessionFactory/ build Failure",e2.getMessage());
            }
            throw new ExceptionInInitializerError(e);
        }

    }

    public static SessionFactory getSessionFactory() {
        return buildSessionFactory();
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            try{
                sessionFactory.close();
                logger.info("Closed Hibernate SessionFactory/ shutdown Successfully");
            }catch (Exception e){
                logger.warn("Failed to close Hibernate SessionFactory/ shutdown Failure",e.getMessage());
            }
            finally {
                sessionFactory = null;
            }
        }
    }
}
