package controller;

import entity.Tprofessor;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.logingu_dao;

@ManagedBean(name = "loginGUControl")
@SessionScoped
public class loginGUController implements Serializable {

    private Tprofessor userObj = new Tprofessor();

    public Tprofessor getUserObj() {
        return userObj;
    }

    public void setUserObj(Tprofessor userObj) {
        this.userObj = userObj;
    }

    String user;
    String pass;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String validateUserPass() {
        logingu_dao dao = new logingu_dao();
        Tprofessor prof = dao.getProfessorByUsernameAndPassword(user, pass);
        if (prof != null) {
            userObj = prof;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", userObj.getUsername());
            return "mainMenu?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("نام کاربری یا رمز عبور وارد شده صحیح نیست."));
            return "index?faces-redirect=true";
        }
    }

    public String logout() {
        userObj = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("username");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
}
