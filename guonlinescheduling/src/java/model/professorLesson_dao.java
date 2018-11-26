package model;

import entity.Tlesson;
import entity.TprofLesson;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class professorLesson_dao {

    public professorLesson_dao() {

    }

    public void createProfLesson(TprofLesson profLesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(profLesson);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeProfLesson(TprofLesson profLesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(profLesson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateProfLesson(TprofLesson profLesson) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(profLesson);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<TprofLesson> getAllProfLessons() {
        List<TprofLesson> profLessons = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            profLessons = session.createCriteria(TprofLesson.class).addOrder(Order.desc("id")).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return profLessons;
    }

    public TprofLesson getProfLessonByProfANDLessonID(int profID, int lessonID) {
        List<TprofLesson> profLesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tproflesson where profid='" + profID + "' and lessonid='" + lessonID + "'";
            profLesson = session.createSQLQuery(query).addEntity(TprofLesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (profLesson.isEmpty()) {
            return null;
        }
        return profLesson.get(0);
    }

    public List<TprofLesson> getProfLessonByProfID(int profID) {
        List<TprofLesson> profLesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tproflesson where profid='" + profID + "'";
            profLesson = session.createSQLQuery(query).addEntity(TprofLesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (profLesson.isEmpty()) {
            return null;
        }
        return profLesson;
    }

    public List<Tlesson> getLessonByProfID(int profID) {
        List<Tlesson> lessons = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tlesson where id in (select lessonid from Tproflesson where profid='" + profID + "') and dto is null";
            lessons = session.createSQLQuery(query).addEntity(Tlesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (lessons.isEmpty()) {
            return null;
        }
        return lessons;
    }
    
    public void deleteByProfID(int profID) {
        Transaction tx = null;
        try {
            List<TprofLesson> profLessonList = getProfLessonByProfID(profID);
            if (profLessonList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < profLessonList.size(); i++) {
                    TprofLesson profLesson = profLessonList.get(i);
                    session.delete(profLesson);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<TprofLesson> getProfLessonByLessonID(int lessonID) {
        List<TprofLesson> profLesson = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tproflesson where lessonid='" + lessonID + "'";
            profLesson = session.createSQLQuery(query).addEntity(TprofLesson.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (profLesson.isEmpty()) {
            return null;
        }
        return profLesson;
    }

    public void deleteByProfessorID(int profID) {
        Transaction tx = null;
        try {
            List<TprofLesson> profLessonList = getProfLessonByProfID(profID);
            if (profLessonList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < profLessonList.size(); i++) {
                    TprofLesson profLesson = profLessonList.get(i);
                    session.delete(profLesson);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteByLessonID(int lessonID) {
        Transaction tx = null;
        try {
            List<TprofLesson> profLessonList = getProfLessonByProfID(lessonID);
            if (profLessonList != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < profLessonList.size(); i++) {
                    TprofLesson profLesson = profLessonList.get(i);
                    session.delete(profLesson);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
