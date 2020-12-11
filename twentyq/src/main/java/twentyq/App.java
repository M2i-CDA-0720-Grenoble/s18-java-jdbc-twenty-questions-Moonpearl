package twentyq;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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
        Question currentQuestion = query.getSingleResult();

        // Efface la console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        // Crée un Scanner prêt à lire les saisies de l'utilisateur
        Scanner scanner = new Scanner(System.in);

        // Crée une boucle infinie
        while (true) {
            // Affiche la question actuelle
            System.out.println( currentQuestion.getText() + " [O/N]" );

            // Demande une saisie à l'utilisateur
            String userInput = scanner.nextLine().trim().toUpperCase();
            // Vérifie que la saisie de l'utilisateur correspond bien à exactement 1 caractère parmi: o, O, n ou N
            if (Pattern.matches("^[oOnN]$", userInput)) {

                Boolean answeredYes;
                if ( "O".equals(userInput) ) {
                    answeredYes = true;
                } else {
                    answeredYes = false;
                }
                
                // Crée une interface permettant de construire une requête en base de données
                queryBuilder = builder.createQuery(Question.class);
                // Ajoute une clause "SELECT * FROM" liée au nom de table défini dans la classe concernée
                from = queryBuilder.from(Question.class);
                queryBuilder.select(from);
                // Ajoute une clause "WHERE...
                queryBuilder.where(
                    // ... `parent_question_id` = ?" avec ? = l'ID de la question actuelle
                    builder.equal( from.get("parentQuestion"), currentQuestion),
                    // ... AND `parent_question_answer` = ?" avec ? = la réponse de l'utilisateur
                    builder.equal( from.get("parentQuestionAnswer"), answeredYes)
                );
                // Crée la requête effective à partir de l'abstraction construite précédemment
                query = manager.createQuery( queryBuilder );
                // Envoie la requête en base de données et récupère son résultat
                try {
                    currentQuestion = query.getSingleResult();
                }
                catch (NoResultException exception) {
                    break;
                }

            // Si la saisie de l'utilisateur n'est pas valide, affiche un avertissement
            } else {
                System.out.println("Vous devez répondre par (O)ui ou par (N)on!");
            }
        }

        // Arrête l'application avec un code de succès
        System.exit(0);

    }
}
