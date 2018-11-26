package model;

import entity.Teducationgroup;
import entity.Tweekschedule;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class weekschedule_dao {

    public weekschedule_dao() {

    }

    public void createWeekSchedule(Tweekschedule weekschedule) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(weekschedule);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeWeekSchedule(Tweekschedule weekschedule) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(weekschedule);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void updateWeekSchedule(Tweekschedule weekschedule) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(weekschedule);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Tweekschedule> getAllNonCommonWeekSchedules() {
        List<Tweekschedule> weekschedules = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where lessontype is null and dto is null order by (daynum) ASC";
            weekschedules = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return weekschedules;
    }
    
    public List<Tweekschedule> getAllWeekSchedules() {
        List<Tweekschedule> weekschedules = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where dto is null order by (daynum) ASC";
            weekschedules = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return weekschedules;
    }

    public List<Tweekschedule> getWeekScheduleByRoomID(int roomID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where roomid='" + roomID + "' and dto is null order by (daynum) ASC";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public List<Tweekschedule> getWeekScheduleByProfessorID(int professorID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where proflessonid in (select id from tprofLesson where profid='" + professorID + "') and dto is null order by (daynum) ASC";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public List<Tweekschedule> getWeekScheduleByLessonID(int lessonID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where proflessonid in (select id from tprofLesson where lessonid='" + lessonID + "') and dto is null order by (daynum) ASC";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public List<Tweekschedule> getWeekScheduleByCollegianID(int collegianID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where collegianid='" + collegianID + "' and dto is null order by (daynum) ASC";
            System.out.println("query getWeekScheduleByCollegianID: " + query);
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public Tweekschedule checkWeekScheduleByRoomIDandDayAndTimeAndWeekType(int roomID, int day, int timeslot, String weektype) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where roomid='" + roomID + "' and daynum='" + day + "' and timeslotnum='" + timeslot + "' and dto is null ";
            if (weektype.equals("B")) {
                query = query + " and (weektype='O' or weektype='E' or weektype='B')";
            } else {
                query = query + " and (weektype='" + weektype + "' or weektype='B')";
            }
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule == null || weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule.get(0);
    }

    public Tweekschedule checkProfessorIDByTimeAndWeekType(int professorID, int day, int timeslot, String weektype) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where proflessonid in (select id from Tproflesson where profid='" + professorID + "') and daynum='" + day + "' and timeslotnum='" + timeslot + "' and dto is null ";
            if (weektype.equals("B")) {
                query = query + " and (weektype='O' or weektype='E' or weektype='B')";
            } else {
                query = query + " and (weektype='" + weektype + "' or weektype='B')";
            }
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule == null || weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule.get(0);
    }

    public Tweekschedule checkCollegianIDByTimeAndWeekType(int collegianID, int day, int timeslot, String weektype) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where collegianid='" + collegianID + "' and daynum='" + day + "' and timeslotnum='" + timeslot + "' and dto is null ";
            if (weektype.equals("B")) {
                query = query + " and (weektype='O' or weektype='E' or weektype='B')";
            } else {
                query = query + " and (weektype='" + weektype + "' or weektype='B')";
            }
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule == null || weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule.get(0);
    }

    public List<Tweekschedule> getAllWeekScheduleByEducationGroupID(int groupID) {
        List<Tweekschedule> weekschedules = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where collegianid in (select id from tcollegian where groupid='" + groupID + "') and dto is null order by (daynum) ASC";
            weekschedules = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return weekschedules;
    }

    public Tweekschedule getExamConfliction(int collegianID, int examDay, int examTime) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where collegianid ='" + collegianID + "' and examday='" + examDay + "' and examtime='" + examTime + "' and dto is null";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule == null || weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule.get(0);
    }

    public List<Tweekschedule> getExamByCollegianID(int collegianID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where collegianid='" + collegianID + "' and weektype='B' and examday is not null and dto is null order by (examday) asc";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }
    
    public List<Tweekschedule> getAllExams() {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where weektype='B' and dto is null order by examday,examtime asc";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public List<Tweekschedule> getCommonWeekSchedule() {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where lessontype='C' and dto is null";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }
    
    public List<Tweekschedule> getWeekScheduleByDate(Date date, boolean created, boolean edited, boolean deleted) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String simpledDate = formatter.format(date);
            String query = "select * from Tweekschedule where 1=1";
            if (created) {
                query = query + " and creationdate>='" + simpledDate + "'";
            }
            if (edited) {
                if(created){
                    query = query + " or";
                }else{
                    query = query + " and";
                }
                query = query + " editedin>='" + simpledDate + "'";
            }
            if (deleted) {
                if(created || edited){
                    query = query + " or";
                }else{
                    query = query + " and";
                }
                query = query + " dto>='" + simpledDate + "'";
            }
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

    public List<Tweekschedule> getExamByProffessorID(int professorID) {
        List<Tweekschedule> weekSchedule = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where proflessonid in (select id from tproflesson where profid='" + professorID + "') and weektype='B' and examday is not null and dto is null order by (examday) asc";
            weekSchedule = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (weekSchedule.isEmpty()) {
            return null;
        }
        return weekSchedule;
    }

}
