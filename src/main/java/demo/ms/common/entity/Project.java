package demo.ms.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Table(name="projects")
@Entity
@Getter
@Setter
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="owner_id")
    private Long ownerId;

    @Column(name="owner_name")
    private String ownerName;

    @Column(name="owner_display_name")
    private String ownerDisplayName;

    @Column(name="name")
    private String name;

    @Column(name="createTime")
    private Date createTime;
}
