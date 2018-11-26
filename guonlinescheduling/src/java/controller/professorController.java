package controller;

import entity.Teducationgroup;
import entity.Tlesson;
import entity.TprofLesson;
import entity.Tprofessor;
import entity.Tweekschedule;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import model.educationGroup_dao;
import model.lesson_dao;
import model.professorLesson_dao;
import model.professor_dao;
import model.weekschedule_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

@ManagedBean(name = "professorControl")
@SessionScoped
public class professorController implements Serializable {

    private List<Tprofessor> professorList = new ArrayList();
    private Tprofessor professor = new Tprofessor();
    private List<Tlesson> selectedLessons = new ArrayList();
    private Tlesson selectedLesson = new Tlesson();
    private int educationGroupID;
    private String oldPass;
    private String newPass;
    private String passCheck;

    private DualListModel<Tlesson> listItems;

    @ManagedProperty(value = "#{loginGUControl}")
    private loginGUController loginController;

    public loginGUController getLoginController() {
        return loginController;
    }

    public void setLoginController(loginGUController loginController) {
        this.loginController = loginController;
    }

    public DualListModel<Tlesson> getListItems() {
        lesson_dao dao = new lesson_dao();

        if (listItems == null) {
            List<Tlesson> listItemsSource = new ArrayList<Tlesson>();
            List<Tlesson> listItemsTarget = new ArrayList<Tlesson>();
            listItemsSource = dao.getAllLessons();
            listItems = new DualListModel<Tlesson>(listItemsSource, listItemsTarget);
        }

        return listItems;
    }

    public void setListItems(DualListModel<Tlesson> listItems) {
        this.listItems = listItems;
    }

    public Tlesson getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Tlesson selectedLesson) {
        this.selectedLesson = selectedLesson;
    }

    public List<Tlesson> getSelectedLessons() {
        return selectedLessons;
    }

    public void setSelectedLessons(List<Tlesson> selectedLessons) {
        this.selectedLessons = selectedLessons;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public int getEducationGroupID() {
        return educationGroupID;
    }

    public void setEducationGroupID(int educationGroupID) {
        this.educationGroupID = educationGroupID;
    }

    public String getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }

    public Tprofessor getProfessor() {
        return professor;
    }

    public void setProfessor(Tprofessor professor) {
        this.professor = professor;
    }

    public List<Tprofessor> getProfessorList() {
        professor_dao dao = new professor_dao();
        //   if (loginGUController.loginedUser.getRoleid() == 1) {
        //     professorList = dao.getAllProfessorsByGroupID(loginGUController.loginedUser.getTeducationgroup().getId());
        //   } else if (loginGUController.loginedUser.getRoleid() == 2) {
        professorList = dao.getAllProfessors();
        //    }
        return professorList;
    }

    public void setProfessorList(List professorList) {
        this.professorList = professorList;
    }

    public void viewNewProfessorDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createProfessor", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditProfessorDialog(Tprofessor professor) {
        this.professor = professor;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editProfessor", options, null);
    }

    public void viewAssignLessonToProfessorDialog(Tprofessor professor) {
        this.professor = professor;
        professorLesson_dao dao = new professorLesson_dao();
        getListItems();
        List<Tlesson> lessonByProfID = dao.getLessonByProfID(professor.getId());
        if (lessonByProfID != null) {
            listItems.setTarget(lessonByProfID);
        } else {
            listItems.setTarget(new ArrayList<Tlesson>());
        }
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("assignLessonToProfessor", options, null);
    }

    public String edit(Tprofessor professor) {
        this.professor = professor;
        return "editProfessor";
    }

    public List<SelectItem> getAllProfessorList() {
        professor_dao dao = new professor_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Tprofessor> professors = dao.getAllProfessors();
        items.add(new SelectItem(-1, ""));
        for (Tprofessor prof : professors) {
            items.add(new SelectItem(prof.getId(), (prof.getFirstname()) + "  " + prof.getLastname()));
        }
        return items;
    }

    public String update() {
        professor_dao dao = new professor_dao();

        professor.setFirstname(professor.getFirstname().trim());
        professor.setLastname(professor.getLastname().trim());
        if (professor.getUsername() != null) {
            professor.setUsername(professor.getUsername().trim());
        }

        educationGroup_dao eduGroupDAO = new educationGroup_dao();

        if (!professor.getFirstname().equals("")) {
            if (!professor.getLastname().equals("")) {
                Tprofessor professorByNameLastName = dao.getProfessorByNameLastName(professor.getFirstname(), professor.getLastname());
                if (professorByNameLastName == null || professorByNameLastName.getId() == professor.getId()) {
                    professor.setTeducationgroup(loginController.getUserObj().getTeducationgroup());
                    if (loginController.getUserObj().getRoleid() == 2) {
                        Teducationgroup educationGroup = eduGroupDAO.getEducationGroupByID(educationGroupID);
                        professor.setTeducationgroup(educationGroup);
                    }
                    dao.updateProfessor(professor);
                    closeDialog("editProfessor");
                    resetInputs();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام و نام خانوادگی استاد وارد شده تکراری است."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام خانوادگی استاد را وارد کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام استاد را وارد کنید."));
            return "";
        }

        return "";
    }

    public void removeProfessor(Tprofessor professor) {
        professor_dao dao = new professor_dao();
        weekschedule_dao weekScheduleDAO = new weekschedule_dao();
        List<Tweekschedule> weekScheduleByProfessorID = weekScheduleDAO.getWeekScheduleByProfessorID(professor.getId());
        if (weekScheduleByProfessorID == null) {
            professorLesson_dao profLessonDAO = new professorLesson_dao();
            profLessonDAO.deleteByProfessorID(professor.getId());
            professor.setDto(new Date());
            dao.updateProfessor(professor);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "از این استاد در برنامه هفتگی استفاده شده است."));
        }

    }

    public void assignLesson() {
        professorLesson_dao profLessonDAO = new professorLesson_dao();
        profLessonDAO.deleteByProfID(professor.getId());
        List<Tlesson> target = (List<Tlesson>) listItems.getTarget();
        if (target.size() > 0) {
            for (int i = 0; i < target.size(); i++) {
                TprofLesson profLesson = new TprofLesson();
                profLesson.setTprofessor(professor);
                profLesson.setTlesson(target.get(i));
                profLessonDAO.createProfLesson(profLesson);
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
        closeDialog("assignLessonToProfessor");
        resetInputs();
    }

    public String insertProfessor() {
        professor.setFirstname(professor.getFirstname().trim());
        professor.setLastname(professor.getLastname().trim());
        if (professor.getUsername() != null) {
            professor.setUsername(professor.getUsername().trim());
        }

        professor_dao dao = new professor_dao();
        educationGroup_dao eduGroupDAO = new educationGroup_dao();

        if (!professor.getFirstname().equals("")) {
            if (!professor.getLastname().equals("")) {
                Tprofessor professorByNameLastName = dao.getProfessorByNameLastName(professor.getFirstname(), professor.getLastname());
                if (professorByNameLastName == null) {
                    professor.setTeducationgroup(loginController.getUserObj().getTeducationgroup());
                    if (loginController.getUserObj().getRoleid() == 2) {
                        Teducationgroup educationGroup = eduGroupDAO.getEducationGroupByID(educationGroupID);
                        professor.setTeducationgroup(educationGroup);
                    }
                    dao.createProfessor(professor);
                    List<Tlesson> target = (List<Tlesson>) listItems.getTarget();
                    if (target.size() > 0) {
                        professorLesson_dao profLessonDAO = new professorLesson_dao();
                        for (int i = 0; i < target.size(); i++) {
                            TprofLesson profLesson = new TprofLesson();
                            profLesson.setTprofessor(professor);
                            profLesson.setTlesson(target.get(i));
                            profLessonDAO.createProfLesson(profLesson);
                        }
                    }
                    closeDialog("createProfessor");
                    resetInputs();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام و نام خانوادگی استاد وارد شده تکراری است."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام خانوادگی استاد را وارد کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام استاد را وارد کنید."));
            return "";
        }

        return "";
    }

    public void resetInputs() {
        professor.setFirstname("");
        professor.setLastname("");
        professor.setNationalnumber("");
        professor.setUsername("");
        professor.setPassword("");
        professor.setRoleid(0);
        selectedLessons.clear();
        if (listItems != null && listItems.getTarget() != null) {
            lesson_dao dao = new lesson_dao();

            listItems.setSource(dao.getAllLessons());
            listItems.setTarget(new ArrayList<Tlesson>());
        }
    }

    public String changePass() {
        professor_dao dao = new professor_dao();
        if (oldPass.equals(professor.getPassword())) {
            if (newPass.equals(passCheck)) {
                professor.setPassword(newPass);
                dao.updateProfessor(professor);
            }
            closeDialog("editPassword");
            resetInputs();
        }
        return "";
    }

    public void viewChangePassDialog(Tprofessor professor) {
        this.professor = professor;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editPassword", options, null);
    }

    public String logout() {
        loginController.setUserObj(new Tprofessor());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

    public void a() throws FileNotFoundException, IOException {
        lesson_dao dao = new lesson_dao();
        educationGroup_dao eduDAO = new educationGroup_dao();
        String csvFile = "C:\\Users\\Hamid\\Desktop\\guonlinescheduling\\guonlinescheduling\\cc - Copy.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        br = new BufferedReader(new FileReader(csvFile));
        while ((line = br.readLine()) != null) {
            Tlesson lesson = new Tlesson();
            Teducationgroup edu;
            String[] country = line.split(cvsSplitBy);
            String n = country[1];
            if (n.contains("ي")) {
                n = n.replace("ي", "ی");
            }
            lesson.setSymbol(country[0]);
            lesson.setName(n);
            String a = country[2];
            if (!a.equals("1")) {
                edu = eduDAO.getEducationGroupBySymbol(Integer.valueOf(a));
            } else {
                edu = eduDAO.getEducationGroupByID(9);
            }
            lesson.setTeducationgroup(edu);
            dao.createLesson(lesson);

        }
    }

    public void b() {
        List<Tlesson> target = (List<Tlesson>) listItems.getTarget();
        lesson_dao dao = new lesson_dao();
        for (int i = 0; i < target.size(); i++) {
            Tlesson get = target.get(i);
        }
    }

}
