package controller;

import entity.Tcollegian;
import entity.Teducationgroup;
import entity.Tlesson;
import entity.Tprofessor;
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
import model.collegian_dao;
import model.educationGroup_dao;
import model.lesson_dao;
import model.professor_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "educationGroupControl")
@SessionScoped
public class educationGroupController implements Serializable {

    @ManagedProperty(value = "#{loginGUControl}")
    private loginGUController loginController;

    private List<Teducationgroup> educationGroupList = new ArrayList();
    private Teducationgroup educationGroup = new Teducationgroup();
    private int educationGroupID;
    private String passCheck;

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

    public String getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }

    public Teducationgroup getEducationGroup() {
        return educationGroup;
    }

    public void setEducationGroup(Teducationgroup educationGroup) {
        this.educationGroup = educationGroup;
    }

    public List<SelectItem> getAllEducationGroupList() {
        educationGroup_dao dao = new educationGroup_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Teducationgroup> ownersList = dao.getAllEducationGroups();
        for (Teducationgroup eduGroup : ownersList) {
            if (eduGroup.getId() == loginController.getUserObj().getTeducationgroup().getId() || eduGroup.getId() == 9 || loginController.getUserObj().getRoleid() == 2) {
                items.add(new SelectItem(eduGroup.getId(), eduGroup.getName()));
            }
        }
        return items;
    }

    public List<Teducationgroup> getEducationGroupList() {
        educationGroup_dao dao = new educationGroup_dao();
        return dao.getAllEducationGroups();
    }

    public void setEducationGroupList(List educationGroupList) {
        this.educationGroupList = educationGroupList;
    }

    public void viewNewEducationGroupDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createEducationGroup", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditEducationGroupDialog(Teducationgroup educationGroup) {
        this.educationGroup = educationGroup;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editEducationGroup", options, null);
    }

    public String edit(Teducationgroup educationGroup) {
        this.educationGroup = educationGroup;
        return "editEducationGroup";
    }

    public String update() {
        educationGroup_dao dao = new educationGroup_dao();

        educationGroup.setName(educationGroup.getName().trim());
        educationGroup.setSymbol(educationGroup.getSymbol().trim());

        if (!educationGroup.getName().equals("")) {
            if (educationGroup.getName().length() <= 50) {
                if (!educationGroup.getSymbol().equals("")) {
                    if (educationGroup.getSymbol().length() <= 50) {
                        Tlesson eduGroupBySymbol = dao.getEducationGroupByCode(educationGroup.getSymbol());
                        if (eduGroupBySymbol == null || eduGroupBySymbol.getId() == educationGroup.getId()) {
                            dao.updateEducationGroup(educationGroup);
                            closeDialog("editEducationGroup");
                            resetInputs();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی وارد شده تکراری است."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره گروه آموزشی باید کمتر از 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره گروه آموزشی را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی باید کمتر از 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی را وارد کنید."));
            return "";
        }

        return "";
    }

    public void removeEducationGroup(Teducationgroup educationGroup) {
        educationGroup_dao dao = new educationGroup_dao();
        lesson_dao lessonDAO = new lesson_dao();
        professor_dao profDAO = new professor_dao();
        collegian_dao collegianDAO = new collegian_dao();

        List<Tlesson> lessonByGroupID = lessonDAO.getLessonByGroupID(educationGroup.getId());
        if (lessonByGroupID == null) {
            List<Tprofessor> professorsByGroupID = profDAO.getProfessorsByGroupID(educationGroup.getId());
            if (professorsByGroupID == null) {
                List<Tcollegian> collegianByGroupID = collegianDAO.getCollegianByGroupID(educationGroup.getId());
                if (collegianByGroupID == null) {
                    educationGroup.setDto(new Date());
                    dao.updateEducationGroup(educationGroup);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "ورودی دانشجویی به این گروه آموزشی منتسب شده است."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "استادی به این گروه آموزشی منتسب شده است."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "امکان حذف وجود ندارد", "درسی به این گروه آموزشی منتسب شده است."));
        }

    }

    public String insertEducationGroup() {
        educationGroup_dao dao = new educationGroup_dao();

        educationGroup.setName(educationGroup.getName().trim());
        educationGroup.setSymbol(educationGroup.getSymbol().trim());

        if (!educationGroup.getName().equals("")) {
            if (educationGroup.getName().length() <= 50) {
                if (!educationGroup.getSymbol().equals("")) {
                    if (educationGroup.getSymbol().length() <= 50) {
                        Tlesson eduGroupBySymbol = dao.getEducationGroupByCode(educationGroup.getSymbol());
                        if (eduGroupBySymbol == null) {
                            dao.createEducationGroup(educationGroup);
                            closeDialog("createEducationGroup");
                            resetInputs();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی وارد شده تکراری است."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره گروه آموزشی باید کمتر از 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "شماره گروه آموزشی را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی باید کمتر از 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام گروه آموزشی را وارد کنید."));
            return "";
        }

        return "";
    }

    public void resetInputs() {
        educationGroup.setName("");
        educationGroup.setSymbol("");
    }
}
