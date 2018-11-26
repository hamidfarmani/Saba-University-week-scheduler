package model;

import entity.Tcollegian;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class collegian_dao {

    public collegian_dao() {

    }

    public void createCollegian(Tcollegian collegian) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(collegian);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeCollegian(Tcollegian collegian) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(collegian);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateCollegian(Tcollegian collegian) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(collegian);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tcollegian> getAllCollegians() {
        List<Tcollegian> collegians = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            collegians = session.createCriteria(Tcollegian.class).addOrder(Order.asc("name")).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return collegians;
    }

    public Tcollegian getCollegianByID(int ID) {
        List<Tcollegian> collegian = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tcollegian where id='" + ID + "' and dto is null";
            collegian = session.createSQLQuery(query).addEntity(Tcollegian.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (collegian.isEmpty()) {
            return null;
        }
        return collegian.get(0);
    }

    public List<Tcollegian> getCollegianByGroupID(int groupID) {
        List<Tcollegian> collegian = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tcollegian where (groupid='" + groupID + "' or name like '%(%') and dto is null";
            System.out.println("q: " + query);
            collegian = session.createSQLQuery(query).addEntity(Tcollegian.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (collegian.isEmpty()) {
            return null;
        }
        return collegian;
    }
}
