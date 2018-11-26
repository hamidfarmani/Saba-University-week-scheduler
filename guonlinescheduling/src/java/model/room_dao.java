package model;

import entity.Troom;
import entity.TroomGroup;
import entity.Tweekschedule;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class room_dao {

    public room_dao() {

    }

    public void createRoom(Troom room) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void createRoomGroup(TroomGroup roomGroup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(roomGroup);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("e: " + e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeRoom(Troom room) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(room);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public void removeRoomGroup(TroomGroup roomGroup) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(roomGroup);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<TroomGroup> getRoomGroup(int roomID, int groupID, int timeSlot, int dayNum) {
        List<TroomGroup> roomGroups = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Troomgroup where roomid='" + roomID + "' and groupid='" + groupID + "' and timeslot='" + timeSlot + "' and daynum='" + dayNum + "'";
            roomGroups = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (roomGroups.isEmpty()) {
            return null;
        }
        return roomGroups;
    }

    public void deleteAllEOB(int roomID, int groupID, int timeSlot, int dayNum) {
        Transaction tx = null;
        try {
            List<TroomGroup> roomGroups = getRoomGroup(roomID, groupID, timeSlot, dayNum);
            if (roomGroups != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < roomGroups.size(); i++) {
                    TroomGroup RG = roomGroups.get(i);
                    session.delete(RG);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteAllRoomGroups() {
        Transaction tx = null;
        try {
            List<TroomGroup> roomGroups = getAllRoomGroupEOB();
            if (roomGroups != null) {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                tx = session.beginTransaction();
                for (int i = 0; i < roomGroups.size(); i++) {
                    TroomGroup RG = roomGroups.get(i);
                    session.delete(RG);
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateRoom(Troom room) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(room);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    public List<Troom> getAllRooms() {
        List<Troom> rooms = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Troom where dto is null order by (id) desc";
            rooms = session.createSQLQuery(query).addEntity(Troom.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return rooms;
    }

    public List<TroomGroup> getAllRoomGroups() {
        List<TroomGroup> roomGroups = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup where type='B' order by (id) desc";
            roomGroups = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return roomGroups;
    }

    public List<TroomGroup> getAllRoomGroupEOB() {
        List<TroomGroup> roomGroups = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup";
            roomGroups = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return roomGroups;
    }

    public Troom getRoomByID(int ID) {
        List<Troom> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Troom where id='" + ID + "' and dto is null";
            room = session.createSQLQuery(query).addEntity(Troom.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room.get(0);
    }

    public Troom getRoomByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Troom lesson = (Troom) session.createQuery("from Troom where name = :name and dto is null")
                .setParameter("name", name).uniqueResult();

        tx.commit();
        return lesson;
    }

    public TroomGroup checkRoomForAssigning(int roomID, int dayNum, int timeSlot, String weekType) {
        List<TroomGroup> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup "
                    + "where "
                    + "roomID='" + roomID + "' and daynum='" + dayNum + "' and timeslot='" + timeSlot + "' ";
            if (weekType.equals("B")) {
                query = query + " and (type='O' or type='E' or type='B')";
            } else {
                query = query + " and (type='" + weekType + "' or type='B')";
            }
            room = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room.get(0);
    }

    public TroomGroup checkAssignedRoomByGroupID(int groupID, int roomID, int dayNum, int timeSlot, String weekType) {
        List<TroomGroup> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup "
                    + "where "
                    + "roomID='" + roomID + "' and daynum='" + dayNum + "' and timeslot='" + timeSlot + "' and (groupid='" + groupID + "' or groupid='9') and type='" + weekType + "'";
            room = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room.get(0);
    }

    public TroomGroup checkAssignedRoomIsEmpty(int roomID, int dayNum, int timeSlot, String weekType) {
        List<TroomGroup> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup "
                    + "where "
                    + "roomID='" + roomID + "' and daynum='" + dayNum + "' and timeslot='" + timeSlot + "' and type='" + weekType + "'";
            room = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room.get(0);
    }

    public List<TroomGroup> getRoomGroupByGroupID(int groupID) {
        List<TroomGroup> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup where (groupid='" + groupID + "' or groupid='9') and type='B'";
            room = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room;
    }

    public List<TroomGroup> getRoomGroupByRoomID(int roomID) {
        List<TroomGroup> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from TroomGroup where roomid='" + roomID + "'";
            room = session.createSQLQuery(query).addEntity(TroomGroup.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room;
    }

    public List<Tweekschedule> getWeekScheduleByRoomID(int roomID) {
        List<Tweekschedule> room = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String query = "select * from Tweekschedule where roomid='" + roomID + "' and dto is null";
            room = session.createSQLQuery(query).addEntity(Tweekschedule.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        if (room.isEmpty()) {
            return null;
        }
        return room;
    }
}
