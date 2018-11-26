package model;

import entity.Tlesson;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class lesson_dao {

    public lesson_dao() {

    }

    public void createLesson(Tlesson lesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(lesson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeLesson(Tlesson lesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(lesson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateLesson(Tlesson lesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(lesson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tlesson> getAllLessons() {
        List<Tlesson> lesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where dto is null order by (id) desc";
            lesson = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lesson.isEmpty()) {
            return null;
        }
        return lesson;
    }

    public List<Tlesson> getAllLessonsByEducationGroupID(int groupID) {
        List<Tlesson> lesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where (groupid='" + groupID + "' or groupid=9) and dto is null order by (id) desc";
            lesson = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lesson.isEmpty()) {
            return null;
        }
        return lesson;
    }

    public Tlesson getLessonByID(int ID) {
        List<Tlesson> lesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where id='" + ID + "' and dto is null";
            lesson = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lesson.isEmpty()) {
            return null;
        }
        return lesson.get(0);
    }

    public List<Tlesson> getLessonByProfID(int profID) {
        List<Tlesson> lesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where id in (select lessonid from Tproflesson where profid='" + profID + "') and dto is null";
            lesson = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lesson.isEmpty()) {
            return null;
        }
        return lesson;
    }

    public List<Tlesson> getLessonByGroupID(int groupID) {
        List<Tlesson> lesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where groupid='" + groupID + "' and dto is null";
            lesson = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lesson.isEmpty()) {
            return null;
        }
        return lesson;
    }

    public Tlesson getLessonByCode(String code) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Tlesson lesson = (Tlesson) session.createQuery("from Tlesson where symbol = :symbol and dto is null")
                .setParameter("symbol", code).uniqueResult();

        tx.commit();
        return lesson;
    }
}
