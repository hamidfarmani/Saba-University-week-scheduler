package controller;

import entity.Tcollegian;
import entity.Teducationgroup;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import model.collegian_dao;
import model.educationGroup_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "collegianControl")
@SessionScoped
public class collegianController implements Serializable {

    private List<Tcollegian> collegianList = new ArrayList();
    private Tcollegian collegian = new Tcollegian();
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

    public Tcollegian getCollegian() {
        return collegian;
    }

    public void setCollegian(Tcollegian collegian) {
        this.collegian = collegian;
    }

    public List<SelectItem> getAllCollegianList() {
        collegian_dao dao = new collegian_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(-2, "همه گروه های دانشجویی"));
        for (Tcollegian eduGroup : collegianList) {
            items.add(new SelectItem(eduGroup.getId(), eduGroup.getName()));
        }
        return items;
    }

    public List<Tcollegian> getCollegianList() {
        collegian_dao dao = new collegian_dao();
        if (loginController.getUserObj().getRoleid() == 1) {
            collegianList = dao.getCollegianByGroupID(loginController.getUserObj().getTeducationgroup().getId());
        } else if (loginController.getUserObj().getRoleid() == 2) {
            collegianList = dao.getAllCollegians();
        }
        return collegianList;
    }

    public void setCollegianList(List collegianList) {
        this.collegianList = collegianList;
    }

    public void viewNewCollegianDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createCollegian", options, null);
    }

    public void viewNewAllCollegianDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createAllCollegian", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void viewEditCollegianDialog(Tcollegian collegian) {
        this.collegian = collegian;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editCollegian", options, null);
    }

    public String edit(Tcollegian collegian) {
        this.collegian = collegian;

        return "editCollegian";
    }

    public String update() {
        collegian_dao dao = new collegian_dao();
        dao.updateCollegian(collegian);
        closeDialog("editCollegian");
        resetInputs();
        return "";
    }

    public void removeCollegian(Tcollegian collegian) {
        collegian_dao dao = new collegian_dao();
        dao.removeCollegian(collegian);
    }

    public String insertCollegian() {
        collegian_dao dao = new collegian_dao();
        educationGroup_dao eduGroupDAO = new educationGroup_dao();
        Teducationgroup educationGroup = eduGroupDAO.getEducationGroupByID(educationGroupID);
        collegian.setTeducationgroup(educationGroup);
        dao.createCollegian(collegian);
        closeDialog("createCollegian");
        resetInputs();
        return "";
    }

    public String insertAllCollegian() {
        collegian_dao dao = new collegian_dao();
        educationGroup_dao eduGroupDAO = new educationGroup_dao();
        List<Teducationgroup> allEducationGroups = eduGroupDAO.getAllEducationGroups();
        Integer enteryear = collegian.getEnteryear();

        for (int i = 0; i < allEducationGroups.size(); i++) {
            for (int j = 0; j < 4; j++) {
                collegian.setTeducationgroup(allEducationGroups.get(i));
                collegian.setEnteryear(enteryear);
                collegian.setName("کارشناسی " + allEducationGroups.get(i).getName() + " " + enteryear);
                dao.createCollegian(collegian);
                enteryear--;
            }
            enteryear = enteryear + 4;
            for (int j = 0; j < 2; j++) {
                collegian.setTeducationgroup(allEducationGroups.get(i));
                collegian.setEnteryear(enteryear);
                collegian.setName("کارشناسی ارشد " + allEducationGroups.get(i).getName() + " " + enteryear);
                dao.createCollegian(collegian);
                collegian.setName("دکتری " + allEducationGroups.get(i).getName() + " " + enteryear);
                dao.createCollegian(collegian);
                enteryear--;
            }
            enteryear = enteryear + 2;

        }

        closeDialog("createAllCollegian");
        resetInputs();
        return "";
    }

    public void resetInputs() {
        collegian.setName("");
    }
}
