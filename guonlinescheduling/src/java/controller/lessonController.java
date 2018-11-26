package controller;

import entity.Tlesson;
import entity.Teducationgroup;
import entity.Tweekschedule;
import java.io.Serializable;
import java.util.ArrayList;
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
import model.lesson_dao;
import model.educationGroup_dao;
import model.professorLesson_dao;
import model.weekschedule_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "lessonControl")
@SessionScoped
public class lessonController implements Serializable {

    private List<Tlesson> lessonList = new ArrayList();
    private Tlesson lesson = new Tlesson();
    private int educationGroupID;

    @ManagedProperty(value = "#{loginGUControl}")
    private loginGUController loginController;

    public loginGUController getLoginController() {
        return loginController;
    }

    public void setLoginController(loginGUController loginController) {
        this.loginController = loginController;
    }

    public int getEducationGroupID() {
        return educationGroupID;
    }

    public void setEducationGroupID(int educationGroupID) {
        this.educationGroupID = educationGroupID;
    }

    public Tlesson getLesson() {
        return lesson;
    }

    public void setLesson(Tlesson lesson) {
        this.lesson = lesson;
    }

    public List<SelectItem> getAllLessonList() {
        lesson_dao dao = new lesson_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Tlesson> lessons = dao.getAllLessons();
        for (Tlesson less : lessons) {
            items.add(new SelectItem(less.getId(), less.getName()));
        }
        return items;
    }

    public List<Tlesson> getLessonList() {
        lesson_dao dao = new lesson_dao();
        if (loginController.getUserObj().getRoleid() == 1) {
            lessonList = dao.getAllLessonsByEducationGroupID(loginController.getUserObj().getTeducationgroup().getId());
        } else if (loginController.getUserObj().getRoleid() == 2) {
            lessonList = dao.getAllLessons();
        }
        return lessonList;
    }

    public void setLessonList(List lessonList) {
        this.lessonList = lessonList;
    }

    public void viewNewLessonDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createLesson", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditLessonDialog(Tlesson lesson) {
        this.lesson = lesson;
        educationGroupID = lesson.getTeducationgroup().getId();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editLesson", options, null);
    }

    public String edit(Tlesson lesson) {
        this.lesson = lesson;

        return "editLesson";
    }

    public String update() {
        lesson.setName(lesson.getName().trim());
        lesson.setSymbol(lesson.getSymbol().trim());

        if (!lesson.getName().equals("")) {
            if (lesson.getName().length() <= 50) {
                if (!lesson.getSymbol().equals("")) {
                    if (lesson.getSymbol().length() <= 50) {
                        lesson_dao dao = new lesson_dao();
                        Tlesson lessonByCode = dao.getLessonByCode(lesson.getSymbol());
                        if (lessonByCode == null || lessonByCode.getId() == lesson.getId()) {
                            educationGroup_dao eduDAO = new educationGroup_dao();
                            Teducationgroup educationGroup = eduDAO.getEducationGroupByID(educationGroupID);
                            lesson.setTeducationgroup(educationGroup);
                            dao.updateLesson(lesson);
                            closeDialog("editLesson");
                            resetInputs();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس وارد شده تکراری است."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس باید کمتر از 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام درس باید کمتر از 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام درس را وارد کنید."));
            return "";
        }
        return "";
    }

    public void removeLesson(Tlesson lesson) {
        lesson_dao dao = new lesson_dao();
        weekschedule_dao weekScheduleDAO = new weekschedule_dao();
        professorLesson_dao profLessonDAO = new professorLesson_dao();
        List<Tweekschedule> weekScheduleByLessonID = weekScheduleDAO.getWeekScheduleByLessonID(lesson.getId());
        if (weekScheduleByLessonID == null) {
            profLessonDAO.deleteByLessonID(lesson.getId());
            lesson.setDto(new Date());
            dao.updateLesson(lesson);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "از این درس در برنامه هفتگی استفاده شده است."));
        }
    }

    public String insertLesson() {
        lesson.setName(lesson.getName().trim());
        lesson.setSymbol(lesson.getSymbol().trim());

        if (!lesson.getName().equals("")) {
            if (lesson.getName().length() <= 50) {
                if (!lesson.getSymbol().equals("")) {
                    if (lesson.getSymbol().length() <= 50) {
                        lesson_dao dao = new lesson_dao();
                        Tlesson lessonByCode = dao.getLessonByCode(lesson.getSymbol());
                        if (lessonByCode == null) {
                            educationGroup_dao eduGroupDAO = new educationGroup_dao();
                            Teducationgroup educationGroup = eduGroupDAO.getEducationGroupByID(educationGroupID);
                            lesson.setTeducationgroup(educationGroup);
                            dao.createLesson(lesson);
                            closeDialog("createLesson");
                            resetInputs();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس وارد شده تکراری است."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس باید کمتر از 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره درس را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام درس باید کمتر از 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام درس را وارد کنید."));
            return "";
        }

        return "";
    }

    public void resetInputs() {
        lesson.setName("");
    }
}
