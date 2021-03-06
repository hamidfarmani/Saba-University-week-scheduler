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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Troom generated by hbm2java
 */
@Entity
@Table(name = "troom", schema = "dbo", catalog = "scheduleDB"
)
public class Troom implements java.io.Serializable {

    private int id;
    private String name;
    private Integer capacity;
    private Date dto;
    private Set<Tweekschedule> tweekschedules = new HashSet<Tweekschedule>(0);
    private Set<TroomGroup> troomGroups = new HashSet<TroomGroup>(0);

    public Troom() {
    }

    public Troom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Troom(int id, String name, Integer capacity, Date dto, Set<Tweekschedule> tweekschedules, Set<TroomGroup> troomGroups) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.dto = dto;
        this.tweekschedules = tweekschedules;
        this.troomGroups = troomGroups;
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

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "capacity")
    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dto", length = 10)
    public Date getDto() {
        return this.dto;
    }

    public void setDto(Date dto) {
        this.dto = dto;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "troom")
    public Set<Tweekschedule> getTweekschedules() {
        return this.tweekschedules;
    }

    public void setTweekschedules(Set<Tweekschedule> tweekschedules) {
        this.tweekschedules = tweekschedules;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "troom")
    public Set<TroomGroup> getTroomGroups() {
        return this.troomGroups;
    }

    public void setTroomGroups(Set<TroomGroup> troomGroups) {
        this.troomGroups = troomGroups;
    }

}
