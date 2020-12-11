package twentyq.entity;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id                     // Clé primaire
    @GeneratedValue         // Auto-incrément
    @Column(name = "id")    // Nom de la colonne en BDD (facultatif dès lors qu'il y a l'annotation @Id)
    private Integer id;

    @Column(name = "text")
    private String text;

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

}
