package twentyq.util;

import javax.persistence.*;
import javax.persistence.criteria.*;

import twentyq.entity.*;

public final class QueryManager {

    public static final String persistenceUnit = "TwentyQuestions";
    
    private static QueryManager instance;
    private EntityManager manager;

    private QueryManager()
    {
        // Crée une interface adaptée au moteur de base de données configuré dans resources/META-INF/persistence.xml
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnit);
        manager = factory.createEntityManager();
    }

    static public QueryManager getInstance()
    {
        if (instance == null) {
            instance = new QueryManager();
        }
        return instance;
    }

    /**
     * Find top node in the questions binary tree (e.g. the only question without a parent)
     * @return The first question
     */
    static public Question findFirstQuestion()
    {
        EntityManager manager = getInstance().manager;
        // Crée une interface permettant de construire une requête en base de données
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Question> queryBuilder = builder.createQuery(Question.class);
        // Ajoute une clause "SELECT * FROM" liée au nom de table défini dans la classe concernée
        Root<Question> from = queryBuilder.from(Question.class);
        queryBuilder.select(from);
        // Ajoute une clause "WHERE `parent_question_id` IS NULL"
        queryBuilder.where( builder.isNull( from.get("parentQuestion") ) );
        // Crée la requête effective à partir de l'abstraction construite précédemment
        TypedQuery<Question> query = manager.createQuery( queryBuilder );
        // Envoie la requête en base de données et récupère son résultat
        return query.getSingleResult();
    }

    
    /**
     * 
     * @param currentQuestion
     * @param currentAnswer
     * @return
     */
    static public Question findNextQuestion(Question currentQuestion, Boolean currentAnswer)
    {
        EntityManager manager = getInstance().manager;
        // Crée une interface permettant de construire une requête en base de données
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Question> queryBuilder = builder.createQuery(Question.class);
        // Ajoute une clause "SELECT * FROM" liée au nom de table défini dans la classe concernée
        Root<Question> from = queryBuilder.from(Question.class);
        queryBuilder.select(from);
        // Ajoute une clause "WHERE...
        queryBuilder.where(
            // ... `parent_question_id` = ?" avec ? = l'ID de la question actuelle
            builder.equal( from.get("parentQuestion"), currentQuestion),
            // ... AND `parent_question_answer` = ?" avec ? = la réponse de l'utilisateur
            builder.equal( from.get("parentQuestionAnswer"), currentAnswer)
        );
        // Crée la requête effective à partir de l'abstraction construite précédemment
        TypedQuery<Question> query = manager.createQuery( queryBuilder );

        try {
            // Envoie la requête en base de données et récupère son résultat
            return query.getSingleResult();
        }
        catch (NoResultException exception) {
            return null;
        }
    }

    static public Item findNextItem(Question currentQuestion, Boolean currentAnswer)
    {
        EntityManager manager = getInstance().manager;
        // Crée une interface permettant de construire une requête en base de données
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Item> queryBuilder = builder.createQuery(Item.class);
        // Ajoute une clause "SELECT * FROM" liée au nom de table défini dans la classe concernée
        Root<Item> from = queryBuilder.from(Item.class);
        queryBuilder.select(from);
        // Ajoute une clause "WHERE...
        queryBuilder.where(
            // ... `parent_question_id` = ?" avec ? = l'ID de la question actuelle
            builder.equal( from.get("parentQuestion"), currentQuestion),
            // ... AND `parent_question_answer` = ?" avec ? = la réponse de l'utilisateur
            builder.equal( from.get("parentQuestionAnswer"), currentAnswer)
        );
        // Crée la requête effective à partir de l'abstraction construite précédemment
        TypedQuery<Item> query = manager.createQuery( queryBuilder );

        try {
            // Envoie la requête en base de données et récupère son résultat
            return query.getSingleResult();
        }
        catch (NoResultException exception) {
            return null;
        }
    }

}
