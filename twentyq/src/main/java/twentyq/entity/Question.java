package twentyq.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public final class Question {

    @Id                     // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Auto-incrément
    @Column(name = "id")    // Nom de la colonne en BDD (facultatif dès lors qu'il y a l'annotation @Id)
    private Integer id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Question parentQuestion;

    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "parentQuestion")
    private Collection<Question> childrenQuestions = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "parentQuestion")
    private Collection<Item> childItem = new ArrayList<>();

    @Column(name = "parent_question_answer")
    private Boolean parentQuestionAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Collection<Question> getChildrenQuestions() {
        return childrenQuestions;
    }

    public void setChildrenQuestions(Collection<Question> childrenQuestions) {
        this.childrenQuestions = childrenQuestions;
    }

    public Collection<Item> getChildItem() {
        return childItem;
    }

    public void setChildItem(Collection<Item> childItem) {
        this.childItem = childItem;
    }

}
