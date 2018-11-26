package model;

import entity.Tprofessor;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class logingu_dao {

    public logingu_dao() {

    }

    public Tprofessor getUserByUsername(String username) {
        List<Tprofessor> accounts = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where username='" + username + "'";
            accounts = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (accounts.isEmpty()) {
            return null;
        }
        return accounts.get(0);
    }

    public Tprofessor getProfessorByUsernameAndPassword(String username, String password) {
        List<Tprofessor> professor = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where username='" + username + "' and password='" + password + "' and roleid in (1,2) and dto is null";
            professor = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (professor.isEmpty()) {
            return null;
        }
        return professor.get(0);
    }

}
