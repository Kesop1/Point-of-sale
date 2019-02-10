package pl.piotrak.product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository("productDAO")
public class ProductDAOImpl implements ProductDAO {

    public ProductDAOImpl() {
        //for Spring
    }

    /**
     @inheritDoc
     */
    @Override
    public void persist(Product entity) {
        openCurrentSessionwithTransaction();
        getCurrentSession().save(entity);
        closeCurrentSessionwithTransaction();
    }

    /**
     @inheritDoc
     */
    @Override
    public void update(Product entity) {
        openCurrentSessionwithTransaction();
        getCurrentSession().update(entity);
        closeCurrentSessionwithTransaction();
    }

    /**
     @inheritDoc
     */
    @Override
    public Product findByCode(long code) {
        openCurrentSession();
        Product product = getCurrentSession().get(Product.class, code);
        closeCurrentSession();
        return product;
    }

    /**
     @inheritDoc
     */
    @Override
    public void delete(Product entity) {
        openCurrentSessionwithTransaction();
        getCurrentSession().delete(entity);
        closeCurrentSessionwithTransaction();
    }

    private Session currentSession;

    private Transaction currentTransaction;

    public void openCurrentSession() {
        currentSession = getSessionFactory().openSession();
    }

    public void openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
}
