package twentyq;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import twentyq.entity.Question;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws Exception {
        
        // Crée une interface adaptée au moteur de base de données configuré dans resources/META-INF/persistence.xml
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("TwentyQuestions");
        EntityManager manager = factory.createEntityManager();

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
        Question firstQuestion = query.getSingleResult();

        // Efface la console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        // Affiche la première question
        System.out.println( firstQuestion.getText() );

    }
}
