package demo.ms.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table
@Getter
@Setter
@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="project_id")
    private Long projectId;

    private String title;

    @OneToMany(mappedBy = "cardId")
    private List<Todo> todoList;
}
