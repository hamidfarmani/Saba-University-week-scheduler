package entity;
// Generated Dec 28, 2016 2:25:13 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tprofessor generated by hbm2java
 */
@Entity
@Table(name = "tprofessor", schema = "dbo", catalog = "scheduleDB"
)
public class Tprofessor implements java.io.Serializable {

    private int id;
    private Teducationgroup teducationgroup;
    private String firstname;
    private String lastname;
    private String nationalnumber;
    private String username;
    private String password;
    private int roleid;
    private Date dto;
    private Set<TprofLesson> tprofLessons = new HashSet<TprofLesson>(0);

    public Tprofessor() {
    }

    public Tprofessor(int id, Teducationgroup teducationgroup, String firstname, String lastname, int roleid) {
        this.id = id;
        this.teducationgroup = teducationgroup;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roleid = roleid;
    }

    public Tprofessor(int id, Teducationgroup teducationgroup, String firstname, String lastname, String nationalnumber, String username, String password, int roleid, Date dto, Set<TprofLesson> tprofLessons) {
        this.id = id;
        this.teducationgroup = teducationgroup;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationalnumber = nationalnumber;
        this.username = username;
        this.password = password;
        this.roleid = roleid;
        this.dto = dto;
        this.tprofLessons = tprofLessons;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groupid", nullable = false)
    public Teducationgroup getTeducationgroup() {
        return this.teducationgroup;
    }

    public void setTeducationgroup(Teducationgroup teducationgroup) {
        this.teducationgroup = teducationgroup;
    }

    @Column(name = "firstname", nullable = false)
    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(name = "lastname", nullable = false)
    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name = "nationalnumber")
    public String getNationalnumber() {
        return this.nationalnumber;
    }

    public void setNationalnumber(String nationalnumber) {
        this.nationalnumber = nationalnumber;
    }

    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "roleid", nullable = false)
    public int getRoleid() {
        return this.roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dto", length = 10)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tprofessor")
    public Set<TprofLesson> getTprofLessons() {
        return this.tprofLessons;
    }

    public void setTprofLessons(Set<TprofLesson> tprofLessons) {
        this.tprofLessons = tprofLessons;
    }

}