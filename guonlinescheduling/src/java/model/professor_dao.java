package model;

import entity.Tlesson;
import entity.Tprofessor;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class professor_dao {

    public professor_dao() {

    }

    public void createProfessor(Tprofessor professor) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(professor);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeProfessor(Tprofessor professor) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(professor);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProfessor(Tprofessor professor) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(professor);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tprofessor> getAllProfessors() {
        List<Tprofessor> professors = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where dto is null order by lastname,firstname asc";
            professors = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (professors.isEmpty()) {
            return null;
        }
        return professors;
    }

    public List<Tprofessor> getProfessorsByGroupID(int groupID) {
        List<Tprofessor> professors = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where groupid='" + groupID + "' and dto is null order by (id) desc";
            professors = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (professors.isEmpty()) {
            return null;
        }
        return professors;
    }

    public List<Tprofessor> getAllProfessorsByGroupID(int groupID) {
        List<Tprofessor> professors = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where groupid='" + groupID + "' and roleid!=2 and dto is null order by (id) desc";
            professors = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (professors.isEmpty()) {
            return null;
        }
        return professors;
    }

    public List<Tprofessor> getProfessorByNameAndLastName(String firstname, String lastname) {
        List<Tprofessor> professors = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tprofessor where firstname='" + firstname + "' and lastname='" + lastname + "'";
            professors = session.createSQLQuery(query).addEntity(Tprofessor.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (professors.isEmpty()) {
            return null;
        }
        return professors;
    }

    public Tprofessor getProfessorByNameLastName(String firstname, String lastname) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Tprofessor professor = (Tprofessor) session.createQuery("from Tprofessor where firstname = :name and lastname = :lastname and dto is null")
                .setParameter("name", firstname)
                .setParameter("lastname", lastname)
                .uniqueResult();

        tx.commit();
        return professor;
    }

    public Tlesson getProfessorByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Tlesson professor = (Tlesson) session.createQuery("from Tprofessor where username = :username and dto is null")
                .setParameter("username", username)
                .uniqueResult();

        tx.commit();
        return professor;
    }
}
