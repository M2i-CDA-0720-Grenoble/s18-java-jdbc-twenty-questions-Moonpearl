package twentyq.entity;

import javax.persistence.*;

@Entity
@Table(name = "items")
public final class Item {
    
    @Id                     // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Auto-incrément
    @Column(name = "id")    // Nom de la colonne en BDD (facultatif dès lors qu'il y a l'annotation @Id)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Question parentQuestion;

    @Column(name = "parent_question_answer")
    private Boolean parentQuestionAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Question getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(Question parentQuestion) {
        this.parentQuestion = parentQuestion;
    }

    public Boolean getParentQuestionAnswer() {
        return parentQuestionAnswer;
    }

    public void setParentQuestionAnswer(Boolean parentQuestionAnswer) {
        this.parentQuestionAnswer = parentQuestionAnswer;
    }

}
