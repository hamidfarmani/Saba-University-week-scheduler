package model;

import entity.Teducationgroup;
import entity.Tlesson;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class educationGroup_dao {

    public educationGroup_dao() {

    }

    public void createEducationGroup(Teducationgroup educationGroup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(educationGroup);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeEducationGroup(Teducationgroup educationGroup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(educationGroup);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateEducationGroup(Teducationgroup educationGroup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(educationGroup);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Teducationgroup> getAllEducationGroups() {
        List<Teducationgroup> educationGroups = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Teducationgroup where dto is null order by (id) desc";
            educationGroups = session.createSQLQuery(query).addEntity(Teducationgroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return educationGroups;
    }

    public Teducationgroup getEducationGroupByID(int ID) {
        List<Teducationgroup> educationGroup = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Teducationgroup where id='" + ID + "' and dto is null";
            educationGroup = session.createSQLQuery(query).addEntity(Teducationgroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (educationGroup.isEmpty()) {
            return null;
        }
        return educationGroup.get(0);
    }
    
    public Teducationgroup getEducationGroupBySymbol(int symbol) {
        List<Teducationgroup> educationGroup = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Teducationgroup where symbol='" + symbol + "'";
            educationGroup = session.createSQLQuery(query).addEntity(Teducationgroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (educationGroup.isEmpty()) {
            return null;
        }
        return educationGroup.get(0);
    }
    
        public Tlesson getEducationGroupByCode(String code) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Tlesson educationGroup = (Tlesson) session.createQuery("from Teducationgroup where symbol = :symbol and dto is null")
                .setParameter("symbol", code).uniqueResult();

        tx.commit();
        return educationGroup;
    }
}
