package controller;

import entity.Teducationgroup;
import entity.Troom;
import entity.TroomGroup;
import entity.Tweekschedule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.educationGroup_dao;
import model.room_dao;
import model.weekschedule_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "roomControl")
@SessionScoped
public class roomController implements Serializable {

    private List<Troom> roomList = new ArrayList();
    private List<TroomGroup> roomGroupList = new ArrayList();
    private Troom room = new Troom();
    private int roomID;
    private String passCheck;
    private TroomGroup roomGroup = new TroomGroup();

    private List<TroomGroup> assignedRoom = new ArrayList();
    private List<Tweekschedule> emptyRoom = new ArrayList();
    private String[][] assignedClass = new String[7][6];
    private String[][] emptyClass = new String[7][6];

    private int eduID;

    @ManagedProperty(value = "#{loginGUControl}")
    private loginGUController loginController;

    public loginGUController getLoginController() {
        return loginController;
    }

    public void setLoginController(loginGUController loginController) {
        this.loginController = loginController;
    }

    public List<TroomGroup> getRoomGroupList() {
        room_dao dao = new room_dao();
        roomGroupList = dao.getAllRoomGroups();
        return roomGroupList;
    }

    public String[][] getAssignedClass() {
        return assignedClass;
    }

    public void setAssignedClass(String[][] assignedClass) {
        this.assignedClass = assignedClass;
    }

    public String[][] getEmptyClass() {
        return emptyClass;
    }

    public void setEmptyClass(String[][] emptyClass) {
        this.emptyClass = emptyClass;
    }

    public void setRoomGroupList(List<TroomGroup> roomGroupList) {
        this.roomGroupList = roomGroupList;
    }

    public int getEduID() {
        return eduID;
    }

    public void setEduID(int eduID) {
        this.eduID = eduID;
    }

    public TroomGroup getRoomGroup() {
        return roomGroup;
    }

    public void setRoomGroup(TroomGroup roomGroup) {
        this.roomGroup = roomGroup;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }

    public Troom getRoom() {
        return room;
    }

    public void setRoom(Troom room) {
        this.room = room;
    }

    public List<SelectItem> getAllRoomList() {
        room_dao dao = new room_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Troom> rooms = dao.getAllRooms();
        items.add(new SelectItem(-1, ""));
        for (Troom r : rooms) {
            r.setName(r.getName() + " - '"+  r.getCapacity() + "'");
            if (loginController.getUserObj().getRoleid() != 2) {
                if (r.getName().contains("خاص")) {
                    continue;
                }
            }
            items.add(new SelectItem(r.getId(), r.getName()));
        }
        return items;
    }

    public List<Troom> getRoomList() {
        room_dao dao = new room_dao();
        roomList = dao.getAllRooms();
        return roomList;
    }

    public void setRoomList(List roomList) {
        this.roomList = roomList;
    }

    public void viewNewRoomDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createRoom", options, null);
    }

    public void viewAssignRoomDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        roomGroup.setType("B");
        RequestContext.getCurrentInstance().openDialog("assignRoomToGroup", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditRoomDialog(Troom room) {
        this.room = room;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editRoom", options, null);
    }

    public void viewClassSchedule() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", "500");
        RequestContext.getCurrentInstance().openDialog("viewAssignedAndEmptyClasses", options, null);

    }

    public void onRoomChange() {
        room_dao dao = new room_dao();
        assignedRoom = dao.getRoomGroupByRoomID(roomID);
        fillTheRooms(assignedRoom);
        emptyRoom = dao.getWeekScheduleByRoomID(roomID);
        fillTheEmptyRooms(emptyRoom);
    }

    public String edit(Troom room) {
        this.room = room;

        return "editRoom";
    }

    public String update() {
        room_dao dao = new room_dao();
        room.setName(room.getName().trim());
        if (!room.getName().equals("")) {
            if (room.getName().length() <= 50) {
                Troom roomByName = dao.getRoomByName(room.getName());
                if (roomByName == null || roomByName.getId() == room.getId()) {
                    dao.updateRoom(room);
                    closeDialog("editRoom");
                    resetInputs();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس وارد شده تکراری می باشد."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس باید کمتر از 50 کاراکتر باشد."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس را وارد کنید."));
        }

        return "";
    }

    public void removeRoom(Troom room) {
        room_dao dao = new room_dao();
        weekschedule_dao weekDAO = new weekschedule_dao();
        List<Tweekschedule> weekScheduleByRoomID = weekDAO.getWeekScheduleByRoomID(room.getId());
        if (weekScheduleByRoomID == null) {
            room.setDto(new Date());
            dao.updateRoom(room);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "از این کلاس در برنامه هفتگی استفاده شده است."));
        }

    }

    public void removeRoomGroup(TroomGroup roomGroup) {
        room_dao dao = new room_dao();
        if (roomGroup.getType().equals("B")) {
            dao.deleteAllEOB(roomGroup.getTroom().getId(), roomGroup.getTeducationgroup().getId(), roomGroup.getTimeslot(), roomGroup.getDaynum());
        } else {
            dao.removeRoomGroup(roomGroup);
        }

    }

    public void removeAllRoomGroup() {
        room_dao dao = new room_dao();
        dao.deleteAllRoomGroups();
    }

    public String insertRoom() {
        room.setName(room.getName().trim());
        room_dao dao = new room_dao();
        if (!room.getName().equals("")) {
            if (room.getName().length() <= 50) {
                Troom roomByName = dao.getRoomByName(room.getName());
                if (roomByName == null) {
                    dao.createRoom(room);
                    closeDialog("createRoom");
                    resetInputs();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس وارد شده تکراری می باشد."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس باید کمتر از 50 کاراکتر باشد."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس را وارد کنید."));
        }

        return "";
    }

    public String assignRoom() {
        room_dao dao = new room_dao();
        educationGroup_dao eduDAO = new educationGroup_dao();
        Troom roomByID = dao.getRoomByID(roomID);
        Teducationgroup educationGroupByID = eduDAO.getEducationGroupByID(eduID);
        roomGroup.setTeducationgroup(educationGroupByID);
        roomGroup.setTroom(roomByID);
        TroomGroup checkRoomForAssigning = dao.checkRoomForAssigning(roomID, roomGroup.getDaynum(), roomGroup.getTimeslot(), roomGroup.getType());
        if (checkRoomForAssigning == null) {
            if (roomGroup.getType().equals("B")) {
                dao.createRoomGroup(roomGroup);
                roomGroup.setType("O");
                dao.createRoomGroup(roomGroup);
                roomGroup.setType("E");
                dao.createRoomGroup(roomGroup);
            } else {
                dao.createRoomGroup(roomGroup);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "کلاس انتخاب شده در این زمان پر می باشد."));
        }
        /* if (roomID) {
         if (room.getName().length() <= 50) {
         Troom roomByName = dao.getRoomByName(room.getName());
         if (roomByName == null) {

         } else {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس وارد شده تکراری می باشد."));
         }
         } else {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس باید کمتر از 50 کاراکتر باشد."));
         }
         } else {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کلاس را وارد کنید."));
         }
         */
        return "";
    }

    public void resetInputs() {
        room.setName("");
    }

    public void fillTheRooms(List<TroomGroup> roomGroup) {
        assignedRoom = roomGroup;
        String days[] = new String[]{
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
        };
        int timeSlots[] = new int[]{
            0, 8, 10, 13, 15, 17
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                assignedClass[i][j] = "";
            }
        }
        if (roomGroup != null) {

            assignedClass[0][0] = "";
            assignedClass[0][1] = "8-10";
            assignedClass[0][2] = "10-12";
            assignedClass[0][3] = "13-15";
            assignedClass[0][4] = "15-17";
            assignedClass[0][5] = "17-19";

            for (int i = 1; i < 7; i++) {
                assignedClass[i][0] = days[i - 1];
                for (int j = 1; j < 6; j++) {
                    for (TroomGroup r : assignedRoom) {
                        if (r.getDaynum() == Arrays.asList(days).indexOf(days[i - 1]) && r.getTimeslot() == timeSlots[j]) {
                            String weekType = r.getType();
                            if (weekType.equals("B")) {
                                weekType = "ثابت";
                            } else if (weekType.equals("E")) {
                                weekType = "زوج";
                            } else {
                                weekType = "فرد";
                            }
                            if (weekType.equals("زوج") || weekType.equals("فرد")) {
                                if (!assignedClass[i][j].contains("ثابت")) {
                                    if (assignedClass[i][j].trim().equals("")) {
                                        assignedClass[i][j] = r.getTeducationgroup().getName() + " (" + weekType + ")";
                                    } else {
                                        assignedClass[i][j] = assignedClass[i][j] + r.getTeducationgroup().getName() + " (" + weekType + ")";
                                    }
                                }
                            } else {
                                assignedClass[i][j] = r.getTeducationgroup().getName() + " (" + weekType + ")";
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillTheEmptyRooms(List<Tweekschedule> roomGroup) {
        emptyRoom = roomGroup;
        String days[] = new String[]{
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
        };
        int timeSlots[] = new int[]{
            0, 8, 10, 13, 15, 17
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                emptyClass[i][j] = "";
            }
        }
        if (roomGroup != null) {

            emptyClass[0][0] = "";
            emptyClass[0][1] = "8-10";
            emptyClass[0][2] = "10-12";
            emptyClass[0][3] = "13-15";
            emptyClass[0][4] = "15-17";
            emptyClass[0][5] = "17-19";

            for (int i = 1; i < 7; i++) {
                emptyClass[i][0] = days[i - 1];
                for (int j = 1; j < 6; j++) {
                    for (Tweekschedule r : emptyRoom) {
                        if (r.getDaynum() == Arrays.asList(days).indexOf(days[i - 1]) && r.getTimeslotnum() == timeSlots[j]) {
                            String weekType = r.getWeektype();
                            if (weekType.equals("B")) {
                                weekType = "ثابت";
                            } else if (weekType.equals("E")) {
                                weekType = "زوج";
                            } else {
                                weekType = "فرد";
                            }
                            if (weekType.equals("زوج") || weekType.equals("فرد")) {
                                if (emptyClass[i][j].trim().equals("")) {
                                    emptyClass[i][j] = r.getTcollegian().getName() + " (" + weekType + ")";
                                } else {
                                    emptyClass[i][j] = emptyClass[i][j] + r.getTcollegian().getName() + " (" + weekType + ")";
                                }
                            } else {
                                emptyClass[i][j] = r.getTcollegian().getName() + " (" + weekType + ")";
                            }
                        }
                    }
                }
            }

        }
    }
}
