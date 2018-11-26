package controller;

import entity.Tcollegian;
import entity.Tlesson;
import entity.TprofLesson;
import entity.Troom;
import entity.TroomGroup;
import entity.Tweekschedule;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import jdk.jfr.events.FileWriteEvent;
import model.collegian_dao;
import model.lesson_dao;
import model.professorLesson_dao;
import model.room_dao;
import model.weekschedule_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "weekScheduleControl")
@SessionScoped
public class weekscheduleController implements Serializable {

    private List<Tweekschedule> weekScheduleList = new ArrayList();
    private List<TroomGroup> assignedRoomList = new ArrayList();
    private List<Tweekschedule> searchWeekScheduleList = new ArrayList();
    private Tweekschedule weekSchedule = new Tweekschedule();
    private List<Tweekschedule> examsList = new ArrayList();
    private List<SelectItem> lessons = new ArrayList<SelectItem>();
    private String[][] schedule = new String[7][6];
    private String[][] assignedRoom = new String[7][6];
    private String[][] submittedLesson = new String[7][6];

    private int educationGroupID;
    private int collegianID;
    private int proffessorID;
    private int lessonID;
    private int roomID;
    private int assignedRoomID;
    private int gpnum;

    private boolean common;

    private int searchProfessorID;
    private int searchColegianID;
    private int searchRoomID;

    private Date selectedDate;
    private boolean showDeleted;
    private boolean showEdited;
    private boolean showCreated;

    private String typeOfScheduleList;

    private String pathStr;

    @ManagedProperty(value = "#{loginGUControl}")
    private loginGUController loginController;

    public Date getSelectedDate() {
        return selectedDate;
    }

    public boolean isShowDeleted() {
        return showDeleted;
    }

    public void setShowDeleted(boolean showDeleted) {
        this.showDeleted = showDeleted;
    }

    public boolean isShowEdited() {
        return showEdited;
    }

    public void setShowEdited(boolean showEdited) {
        this.showEdited = showEdited;
    }

    public boolean isShowCreated() {
        return showCreated;
    }

    public void setShowCreated(boolean showCreated) {
        this.showCreated = showCreated;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public loginGUController getLoginController() {
        return loginController;
    }

    public void setLoginController(loginGUController loginController) {
        this.loginController = loginController;
    }

    public List<Tweekschedule> getExamsList() {
        weekschedule_dao dao = new weekschedule_dao();
        examsList = dao.getExamByCollegianID(collegianID);
        if (collegianID == -2) {
            examsList = dao.getAllExams();
        }
        if (typeOfScheduleList != null && typeOfScheduleList.equals("C")) {
            examsList = dao.getExamByCollegianID(searchColegianID);
            if (searchColegianID == -2) {
                examsList = dao.getAllExams();
            }
        } else if (typeOfScheduleList != null && typeOfScheduleList.equals("P")) {
            examsList = dao.getExamByProffessorID(searchProfessorID);
        }
        return examsList;
    }

    public void setExamsList(List<Tweekschedule> examsList) {
        this.examsList = examsList;
    }

//    public String[][] getSubmittedLesson() {
//        weekschedule_dao dao = new weekschedule_dao();
//        List<Tweekschedule> commonWeekSchedule = dao.getCommonWeekSchedule();
//        fillTheSubmittedLesson(commonWeekSchedule);
//        return submittedLesson;
//    }
    public String getPathStr() {
        return pathStr;
    }

    public void setPathStr(String pathStr) {
        this.pathStr = pathStr;
    }

    public void setSubmittedLesson(String[][] submittedLesson) {
        this.submittedLesson = submittedLesson;
    }

    public String[][] getAssignedRoom() {

        room_dao dao = new room_dao();
        assignedRoomList = dao.getRoomGroupByGroupID(loginController.getUserObj().getTeducationgroup().getId());
        if (assignedRoomList != null) {
            fillTheAssignedRoom(assignedRoomList);
        }
        return assignedRoom;
    }

    public void setAssignedRoom(String[][] assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public List<TroomGroup> getAssignedRoomList() {
        room_dao dao = new room_dao();
        assignedRoomList = dao.getRoomGroupByGroupID(loginController.getUserObj().getTeducationgroup().getId());
        if (assignedRoomList != null) {
            fillTheAssignedRoom(assignedRoomList);
        }
        return assignedRoomList;
    }

    public int getAssignedRoomID() {
        return assignedRoomID;
    }

    public void setAssignedRoomID(int assignedRoomID) {
        this.assignedRoomID = assignedRoomID;
    }

    public void setAssignedRoomList(List<TroomGroup> assignedRoomList) {
        this.assignedRoomList = assignedRoomList;
    }

    public List<Tweekschedule> getSearchWeekScheduleList() {
        return searchWeekScheduleList;
    }

    public void setSearchWeekScheduleList(List<Tweekschedule> searchWeekScheduleList) {
        this.searchWeekScheduleList = searchWeekScheduleList;
    }

    public int getSearchProfessorID() {
        return searchProfessorID;
    }

    public void setSearchProfessorID(int searchProfessorID) {
        this.searchProfessorID = searchProfessorID;
    }

    public int getGpnum() {
        return gpnum;
    }

    public void setGpnum(int gpnum) {
        this.gpnum = gpnum;
    }

    public int getSearchColegianID() {
        return searchColegianID;
    }

    public void setSearchColegianID(int searchColegianID) {
        this.searchColegianID = searchColegianID;
    }

    public int getSearchRoomID() {
        return searchRoomID;
    }

    public void setSearchRoomID(int searchRoomID) {
        this.searchRoomID = searchRoomID;
    }

    public String getTypeOfScheduleList() {
        return typeOfScheduleList;
    }

    public void setTypeOfScheduleList(String typeOfScheduleList) {
        this.typeOfScheduleList = typeOfScheduleList;
    }

    public String[][] getSchedule() {
        fillTheSchedule(weekScheduleList);
        return schedule;
    }

    public void setSchedule(String[][] schedule) {
        this.schedule = schedule;
    }

    public void fillTheSchedule(List<Tweekschedule> weekschedule) {
        weekScheduleList = weekschedule;
        String days[] = new String[]{
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
        };
        int timeSlots[] = new int[]{
            0, 8, 10, 13, 15, 17
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                schedule[i][j] = "";
            }
        }
        if (weekschedule != null) {

            schedule[0][0] = "";
            schedule[0][1] = "8-10";
            schedule[0][2] = "10-12";
            schedule[0][3] = "13-15";
            schedule[0][4] = "15-17";
            schedule[0][5] = "17-19";

            for (int i = 1; i < 7; i++) {
                schedule[i][0] = days[i - 1];
                for (int j = 1; j < 6; j++) {
                    for (Tweekschedule w : weekScheduleList) {
                        if (w.getDaynum() == Arrays.asList(days).indexOf(days[i - 1]) && w.getTimeslotnum() == timeSlots[j]) {
                            String weektype = w.getWeektype();
                            if (weektype.equals("B")) {
                                weektype = "ثابت";
                            } else if (weektype.equals("O")) {
                                weektype = "فرد";
                            } else {
                                weektype = "زوج";
                            }
                            if (weektype.equals("زوج") || weektype.equals("فرد")) {
                                if (schedule[i][j].trim().equals("")) {
                                    schedule[i][j] = w.getTprofLesson().getTlesson().getName() + "-"
                                            + w.getTprofLesson().getTprofessor().getLastname() + "-"
                                            + w.getTroom().getName() + "-("
                                            + weektype + ")";
                                } else {
                                    schedule[i][j] = schedule[i][j] + "\n" + w.getTprofLesson().getTlesson().getName() + "-"
                                            + w.getTprofLesson().getTprofessor().getLastname() + "-"
                                            + w.getTroom().getName() + "-("
                                            + weektype + ")";
                                }
                            } else {
                                if (schedule[i][j].trim().equals("")) {
                                    schedule[i][j] = w.getTprofLesson().getTlesson().getName() + "-"
                                            + w.getTprofLesson().getTprofessor().getLastname() + "-"
                                            + w.getTroom().getName() + "-("
                                            + weektype + ")";
                                } else {
                                    schedule[i][j] = schedule[i][j] + "\n" + w.getTprofLesson().getTlesson().getName() + "-"
                                            + w.getTprofLesson().getTprofessor().getLastname() + "-"
                                            + w.getTroom().getName() + "-("
                                            + weektype + ")" + " - 'گروه دوم'";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillTheAssignedRoom(List<TroomGroup> roomGroup) {
        assignedRoomList = roomGroup;
        String days[] = new String[]{
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
        };
        int timeSlots[] = new int[]{
            0, 8, 10, 13, 15, 17
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                assignedRoom[i][j] = "";
            }
        }
        if (roomGroup != null) {

            assignedRoom[0][0] = "";
            assignedRoom[0][1] = "8-10";
            assignedRoom[0][2] = "10-12";
            assignedRoom[0][3] = "13-15";
            assignedRoom[0][4] = "15-17";
            assignedRoom[0][5] = "17-19";

            for (int i = 1; i < 7; i++) {
                assignedRoom[i][0] = days[i - 1];
                for (int j = 1; j < 6; j++) {
                    for (TroomGroup r : assignedRoomList) {
                        if (r.getDaynum() == Arrays.asList(days).indexOf(days[i - 1]) && r.getTimeslot() == timeSlots[j]) {

                            if (!r.getTroom().getName().startsWith("آز")) {
                                if (!assignedRoom[i][j].trim().equals("")) {
                                    assignedRoom[i][j] = assignedRoom[i][j] + " -- " + r.getTroom().getName();
                                } else {
                                    assignedRoom[i][j] = r.getTroom().getName();
                                }
                                if (r.getTeducationgroup().getId() == 9) {
                                    assignedRoom[i][j] = assignedRoom[i][j] + "(مشترک)";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void fillTheSubmittedLesson(List<Tweekschedule> weekSch) {
        String days[] = new String[]{
            "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
        };
        int timeSlots[] = new int[]{
            0, 8, 10, 13, 15, 17
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                submittedLesson[i][j] = "";
            }
        }
        if (weekSch != null) {

            submittedLesson[0][0] = "";
            submittedLesson[0][1] = "8-10";
            submittedLesson[0][2] = "10-12";
            submittedLesson[0][3] = "13-15";
            submittedLesson[0][4] = "15-17";
            submittedLesson[0][5] = "17-19";

            for (int i = 1; i < 7; i++) {
                submittedLesson[i][0] = days[i - 1];
                for (int j = 1; j < 6; j++) {
                    for (Tweekschedule w : weekSch) {
                        if (w.getDaynum() == Arrays.asList(days).indexOf(days[i - 1]) && w.getTimeslotnum() == timeSlots[j]) {

                            if (!w.getTroom().getName().startsWith("آز")) {
                                if (!submittedLesson[i][j].trim().equals("")) {
                                    submittedLesson[i][j] = submittedLesson[i][j] + " -- " + w.getTprofLesson().getTlesson().getName() + " (" + w.getTroom().getName() + ")";
                                } else {
                                    submittedLesson[i][j] = w.getTprofLesson().getTlesson().getName() + " (" + w.getTroom().getName() + ")";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*public void fillTheSubmittedLesson() {
     String days[] = new String[]{
     "شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه"
     };
     int timeSlots[] = new int[]{
     0, 8, 10, 13, 15, 17
     };

     for (int i = 0; i < 7; i++) {
     for (int j = 0; j < 6; j++) {
     submittedLesson[i][j] = "";
     }
     }

     submittedLesson[0][0] = "";
     submittedLesson[0][1] = "8-10";
     submittedLesson[0][2] = "10-12";
     submittedLesson[0][3] = "13-15";
     submittedLesson[0][4] = "15-17";
     submittedLesson[0][5] = "17-19";

     for (int i = 1; i < 7; i++) {
     submittedLesson[i][0] = days[i - 1];
     }

     submittedLesson[1][4] = "تاریخ تحلیلی صدر اسلام" + " (خواهران) " + "103";
     submittedLesson[1][5] = "تاریخ تحلیلی صدر اسلام" + " (برادران) " + "103";
     submittedLesson[2][4] = "انقلاب اسلامی" + " (خواهران) " + "103";
     submittedLesson[2][5] = "انقلاب اسلامی" + " (برادران) " + "103";
     submittedLesson[2][4] = submittedLesson[2][4] + " \n " + "اندیشه اسلامی 2" + " (خواهران) " + "107";
     submittedLesson[2][5] = submittedLesson[2][5] + " \n " + "اندیشه اسلامی 2" + " (برادران) " + "107";
     submittedLesson[3][3] = "اندیشه اسلامی 1" + " (خواهران) " + "103";
     submittedLesson[3][4] = "اندیشه اسلامی 1" + " (برادران) " + "103";
     submittedLesson[4][2] = "تفسیر موضوعی قرآن" + " (خواهران) " + "103";
     submittedLesson[4][3] = "تفسیر موضوعی قرآن" + " (برادران) " + "103";
     submittedLesson[5][1] = "اخلاق اسلامی" + " (برادران) " + "103";
     submittedLesson[5][2] = "اخلاق اسلامی" + " (خواهران) " + "103";
     submittedLesson[5][3] = "دانش خانواده" + " (خواهران) " + "103";
     submittedLesson[5][4] = "دانش خانواده" + " (برادران) " + "103";

     submittedLesson[1][3] = "ریاضی 1" + " (ثابت) " + "301 علوم";
     submittedLesson[1][4] = submittedLesson[1][4] + " \n " + "ریاضی 1" + " (زوج) " + "301 علوم";
     submittedLesson[2][3] = "آمار احتمال" + " (فرد) " + "207 علوم";
     submittedLesson[2][4] = submittedLesson[2][4] + " \n " + "آمار احتمال" + " (ثابت) " + "203 علوم";
     submittedLesson[5][3] = submittedLesson[5][3] + " \n " + "فیزیک 1" + " (ثابت) " + "207 علوم";
     submittedLesson[5][4] = submittedLesson[5][4] + " \n " + "فیزیک 1" + " (زوج) " + "207 علوم";

     }*/
    public List<SelectItem> getLessons() {
        return lessons;
    }

    public void setLessons(List<SelectItem> lessons) {
        this.lessons = lessons;
    }

    public int getCollegianID() {
        return collegianID;
    }

    public void setCollegianID(int collegianID) {
        this.collegianID = collegianID;
    }

    public int getProffessorID() {
        return proffessorID;
    }

    public void setProffessorID(int proffessorID) {
        this.proffessorID = proffessorID;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getEducationGroupID() {
        return educationGroupID;
    }

    public void setEducationGroupID(int educationGroupID) {
        this.educationGroupID = educationGroupID;
    }

    public Tweekschedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(Tweekschedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public List<Tweekschedule> getWeekScheduleList() {
        return weekScheduleList;
    }

    public List<Tweekschedule> getALLWeekScheduleList() {
        weekschedule_dao dao = new weekschedule_dao();
        weekScheduleList = dao.getAllWeekSchedules();
        if (selectedDate != null) {
            searchDate();
        }
        return weekScheduleList;
    }

    public void onCollegianChange() {
        weekschedule_dao dao = new weekschedule_dao();
        weekScheduleList = dao.getWeekScheduleByCollegianID(collegianID);
        fillTheSchedule(weekScheduleList);
    }

    public void onAssignedRoomChange() {
        room_dao dao = new room_dao();
        assignedRoomList = dao.getRoomGroupByGroupID(loginController.getUserObj().getTeducationgroup().getId());
        if (assignedRoomList != null) {
            fillTheAssignedRoom(assignedRoomList);
        }
    }

    public void onTypeChange() {
        weekschedule_dao dao = new weekschedule_dao();
        if (typeOfScheduleList.equals("P")) {
            collegianID = -1;
            searchWeekScheduleList = dao.getWeekScheduleByProfessorID(searchProfessorID);
            fillTheSchedule(searchWeekScheduleList);
        } else if (typeOfScheduleList.equals("R")) {
            collegianID = -1;
            searchWeekScheduleList = dao.getWeekScheduleByRoomID(searchRoomID);
            fillTheSchedule(searchWeekScheduleList);
        } else if (typeOfScheduleList.equals("C")) {
            if (searchColegianID != -2) {
                searchWeekScheduleList = dao.getWeekScheduleByCollegianID(searchColegianID);
                collegianID = searchColegianID;
                fillTheSchedule(searchWeekScheduleList);
            } else {
                fillTheSchedule(new ArrayList<Tweekschedule>());
            }
        }

    }

    public void onRoomChange() {
        weekschedule_dao dao = new weekschedule_dao();
        weekScheduleList = dao.getWeekScheduleByRoomID(roomID);
    }

    public void setWeekScheduleList(List weekScheduleList) {
        this.weekScheduleList = weekScheduleList;
    }

    public void viewNewWeekScheduleDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentWidth", 1000);
        RequestContext.getCurrentInstance().openDialog("createWeekSchedule", options, null);
    }

    public void viewSchedulesDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", false);
        options.put("resizable", false);
        //  resetInputs();
        RequestContext.getCurrentInstance().openDialog("schedules", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditWeekScheduleDialog(Tweekschedule weekSchedule) {
        this.weekSchedule = weekSchedule;
        if (weekSchedule.getLessontype() != null) {
            common = true;
        }
        proffessorID = weekSchedule.getTprofLesson().getTprofessor().getId();
        onProfessorChange();
        roomID = weekSchedule.getTroom().getId();
        lessonID = weekSchedule.getTprofLesson().getTlesson().getId();
        collegianID = weekSchedule.getTcollegian().getId();
        onCollegianChange();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentWidth", 800);
        RequestContext.getCurrentInstance().openDialog("editWeekSchedule", options, null);
    }

    public void onProfessorChange() {
        lessons = getLessonListByProfessor();
    }

    public List<SelectItem> getLessonListByProfessor() {
        lesson_dao dao = new lesson_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Tlesson> lessons = dao.getLessonByProfID(proffessorID);
        if (lessons != null) {
            for (Tlesson less : lessons) {
                items.add(new SelectItem(less.getId(), less.getName()));
            }
        }
        return items;
    }

    public String edit(Tweekschedule weekSchedule) {
        this.weekSchedule = weekSchedule;

        return "editWeekSchedule";
    }

    public String update() {
        weekschedule_dao dao = new weekschedule_dao();
        collegian_dao collegianDAO = new collegian_dao();
        professorLesson_dao profDAO = new professorLesson_dao();
        room_dao roomDAO = new room_dao();
        boolean isManager;
        if (common) {
            weekSchedule.setLessontype("C");
        } else {
            weekSchedule.setLessontype(null);
        }
        if (loginController.getUserObj().getRoleid() == 2) {
            isManager = true;
        } else {
            isManager = false;
        }

        if (proffessorID != 0 && proffessorID != -1) {
            if (lessonID != 0 && lessonID != -1) {
                if (roomID != 0 && roomID != -1) {
                    if (weekSchedule.getWeektype() != null) {
                        TroomGroup checkAssignedRoomByGroupID = roomDAO.checkAssignedRoomByGroupID(loginController.getUserObj().getTeducationgroup().getId(), roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                        TroomGroup checkAssignedRoomIsEmpty = roomDAO.checkAssignedRoomIsEmpty(roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                        if (checkAssignedRoomByGroupID != null || checkAssignedRoomIsEmpty == null || isManager) {
                            Tweekschedule checkRoom = dao.checkWeekScheduleByRoomIDandDayAndTimeAndWeekType(roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                            if (checkRoom == null || checkRoom.getId() == weekSchedule.getId() || common) {
                                Tweekschedule checkProfessor = dao.checkProfessorIDByTimeAndWeekType(proffessorID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                                if (checkProfessor == null || checkProfessor.getId() == weekSchedule.getId()) {
                                    Tweekschedule checkCollegian = dao.checkCollegianIDByTimeAndWeekType(collegianID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                                    if (checkCollegian == null || checkCollegian.getId() == weekSchedule.getId() || gpnum == 2) {
                                        TprofLesson profLesson = profDAO.getProfLessonByProfANDLessonID(proffessorID, lessonID);
                                        Tcollegian collegian = collegianDAO.getCollegianByID(collegianID);
                                        Troom room = roomDAO.getRoomByID(roomID);

                                        weekSchedule.setTcollegian(collegian);
                                        weekSchedule.setTprofLesson(profLesson);
                                        weekSchedule.setTroom(room);

                                        if (weekSchedule.getExamday() != null && weekSchedule.getExamday() == 0) {
                                            weekSchedule.setExamday(null);
                                            weekSchedule.setExamtime(null);
                                        }
                                        if (weekSchedule.getWeektype().equals("B") && weekSchedule.getExamday() != null && weekSchedule.getExamtime() != null) {
                                            Tweekschedule confliction = dao.getExamConfliction(weekSchedule.getTcollegian().getId(), weekSchedule.getExamday(), weekSchedule.getExamtime());
                                            if (confliction == null || confliction.getId() == weekSchedule.getId()) {
                                                weekSchedule.setEditedin(new Date());
                                                dao.updateWeekSchedule(weekSchedule);
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                                onCollegianChange();
                                                resetInputs();
                                                closeDialog("editWeekSchedule");
                                            } else {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "گروه در این ساعت امتحان دیگری دارد."));
                                            }
                                        } else {
                                            weekSchedule.setEditedin(new Date());
                                            weekSchedule.setExamday(null);
                                            weekSchedule.setExamtime(null);
                                            dao.updateWeekSchedule(weekSchedule);
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                            onCollegianChange();
                                            resetInputs();
                                            closeDialog("editWeekSchedule");
                                        }
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "گروه دانشجویان انتخاب شده در این ساعت مشغول می باشد."));
                                    }
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "استاد انتخاب شده در این ساعت مشغول می باشد."));
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس انتخاب شده در این ساعت پر می باشد."));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس و زمان انتخاب شده به گروه دیگری اختصاص دارد. لطفا در زمان مختص به خود برنامه ریزی نمایید."));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "نوع هفته را مشخص کنید."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس را انتخاب کنید."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "درس را انتخاب کنید."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "استاد را انتخاب کنید."));
        }

        return "";
    }

    public void removeWeekSchedule(Tweekschedule weekSchedule) {
        weekschedule_dao dao = new weekschedule_dao();
        weekSchedule.setDto(new Date());
        dao.updateWeekSchedule(weekSchedule);
    }

    public String insertWeekSchedule() {
        weekschedule_dao dao = new weekschedule_dao();
        collegian_dao collegianDAO = new collegian_dao();
        professorLesson_dao profDAO = new professorLesson_dao();
        room_dao roomDAO = new room_dao();
        boolean isManager;
        if (common) {
            weekSchedule.setLessontype("C");
        } else {
            weekSchedule.setLessontype(null);
        }
        if (loginController.getUserObj().getRoleid() == 2) {
            isManager = true;
        } else {
            isManager = false;
        }

        if (proffessorID != 0 && proffessorID != -1) {
            if (lessonID != 0 && lessonID != -1) {
                if (collegianID != 0 && collegianID != -1 && collegianID != -2) {
                    if (roomID != 0 && roomID != -1) {
                        if (weekSchedule.getWeektype() != null) {
                            TroomGroup checkAssignedRoomIsEmpty = roomDAO.checkAssignedRoomIsEmpty(roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                            TroomGroup checkAssignedRoomByGroupID = roomDAO.checkAssignedRoomByGroupID(loginController.getUserObj().getTeducationgroup().getId(), roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());

                            if (checkAssignedRoomByGroupID != null || checkAssignedRoomIsEmpty == null || isManager) {
                                Tweekschedule checkRoom = dao.checkWeekScheduleByRoomIDandDayAndTimeAndWeekType(roomID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                                if (checkRoom == null || common) {
                                    Tweekschedule checkProfessor = dao.checkProfessorIDByTimeAndWeekType(proffessorID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                                    if (checkProfessor == null) {
                                        Tweekschedule checkCollegian = dao.checkCollegianIDByTimeAndWeekType(collegianID, weekSchedule.getDaynum(), weekSchedule.getTimeslotnum(), weekSchedule.getWeektype());
                                        if (checkCollegian == null || gpnum == 2) {
                                            TprofLesson profLesson = profDAO.getProfLessonByProfANDLessonID(proffessorID, lessonID);
                                            Tcollegian collegian = collegianDAO.getCollegianByID(collegianID);
                                            Troom room = roomDAO.getRoomByID(roomID);

                                            weekSchedule.setTcollegian(collegian);
                                            weekSchedule.setTprofLesson(profLesson);
                                            weekSchedule.setTroom(room);

                                            if (weekSchedule.getExamday() != null && weekSchedule.getExamday() == 0) {
                                                weekSchedule.setExamday(null);
                                                weekSchedule.setExamtime(null);
                                            }
                                            if (weekSchedule.getWeektype().equals("B") && weekSchedule.getExamday() != null && weekSchedule.getExamtime() != null) {
                                                Tweekschedule confliction = dao.getExamConfliction(collegianID, weekSchedule.getExamday(), weekSchedule.getExamtime());
                                                if (confliction == null) {
                                                    weekSchedule.setCreationdate(new Date());
                                                    dao.createWeekSchedule(weekSchedule);
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                                    onCollegianChange();
                                                    resetInputs();
                                                } else {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "گروه در این ساعت امتحان دیگری دارد."));
                                                }
                                            } else {
                                                weekSchedule.setExamday(null);
                                                weekSchedule.setExamtime(null);
                                                weekSchedule.setCreationdate(new Date());
                                                dao.createWeekSchedule(weekSchedule);
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                                common = false;
                                                gpnum = 1;
                                                onCollegianChange();
                                                resetInputs();
                                            }
                                        } else {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "گروه دانشجویان انتخاب شده در این ساعت مشغول می باشد."));
                                        }
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "استاد انتخاب شده در این ساعت مشغول می باشد."));
                                    }
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس انتخاب شده در این ساعت پر می باشد."));
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس و زمان انتخاب شده به گروه دیگری اختصاص دارد. لطفا در زمان مختص به خود برنامه ریزی نمایید."));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "نوع هفته را مشخص کنید."));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "کلاس را انتخاب کنید."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "گروه دانشجو را انتخاب کنید."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "درس را انتخاب کنید."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "استاد را انتخاب کنید."));
        }

        return "";
    }

    public void resetInputs() {
        common = false;
    }

    public void onWeekTypeChange() {
        if (!weekSchedule.getWeektype().equals("B")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "هشدار", "امکان ثبت امتحان فقط برای هفته های ثابت وجود دارد."));
        }
    }

    public void searchDate() {
        weekschedule_dao dao = new weekschedule_dao();
        if (selectedDate != null) {
            weekScheduleList = dao.getWeekScheduleByDate(selectedDate, showCreated, showEdited, showDeleted);
        } else {
            getALLWeekScheduleList();
        }

    }

    public void writeToCSV() {
        String pathToHome = System.getProperty("user.home");
        String path = "C:\\";
        String filename = "";
        if (typeOfScheduleList.equals("P")) {
            filename = searchWeekScheduleList.get(0).getTprofLesson().getTprofessor().getLastname();
        } else if (typeOfScheduleList.equals("R")) {
            filename = searchWeekScheduleList.get(0).getTroom().getName();
        } else if (typeOfScheduleList.equals("C")) {
            filename = searchWeekScheduleList.get(0).getTcollegian().getName();
        } else {
            filename = "myFile";
        }

        File f = new File(pathToHome + "\\SABA");
        if (!f.exists()) {
            f.mkdir();
        }
        pathStr = pathToHome + "\\SABA\\" + filename + ".csv";
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(pathStr));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    if (schedule[i][j].equals("8-10")) {
                        schedule[i][j] = "8--10";
                    }
                    if (schedule[i][j].equals("10-12")) {
                        schedule[i][j] = "10--12";
                    }
                    if (schedule[i][j].equals("13-15")) {
                        schedule[i][j] = "13--15";
                    }
                    if (schedule[i][j].equals("15-17")) {
                        schedule[i][j] = "15--17";
                    }
                    if (schedule[i][j].equals("17-19")) {
                        schedule[i][j] = "17--19";
                    }
                    schedule[i][j] = schedule[i][j].replaceAll("ی", "ي");
                    sb.append(schedule[i][j]);
                    sb.append(",");
                }

                System.out.println();
                sb.append("\n");
            }

            br.write(sb.toString());
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(weekscheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
