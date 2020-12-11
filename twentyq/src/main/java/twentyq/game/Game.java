package twentyq.game;

import twentyq.util.Console;
import twentyq.util.QueryManager;
import twentyq.entity.Question;

import javax.persistence.*;

import twentyq.entity.Item;

public final class Game {

    private Question currentQuestion;
    private Boolean isRunning;

    public Game()
    {
        isRunning = true;
        init();
    }

    private void init()
    {
        // Récupère la première question de l'arbre
        currentQuestion = QueryManager.findFirstQuestion();
    }

    public Boolean isRunning()
    {
        return isRunning;
    }

    public void terminate()
    {
        isRunning = false;
    }

    public void update()
    {
        // Affiche la question actuelle
        System.out.println( "" );
        System.out.println( currentQuestion.getText() + " [O/N]" );
        // Demande à l'utilisateur une réponse en Oui ou Non
        Boolean answeredYes = Console.askYesOrNo();

        // Cherche s'il existe une proposition associée à la question à laquelle l'utilisateur
        // vient de répondre, et à la réponse qu'il a donnée
        Item item = QueryManager.findNextItem(currentQuestion, answeredYes);
        // Si oui, l'application fait une proposition à l'utilisateur
        if (item != null) {
            System.out.println("Je propose: " + item.getName() + " [O/N]");

            // Si la proposition de l'application est validée par l'utilisateur
            if ( Console.askYesOrNo() ) {
                // Affiche un message de victoire et termine l'application
                System.out.println("J'ai trouvé! :)");
                askForNewGame();
                return;
            }
        }
            
        // Récupère la question suivante, en fonction de la réponse donnée par l'utilisateur
        Question nextQuestion = QueryManager.findNextQuestion(currentQuestion, answeredYes);
        // S'il ne reste plus de question à poser, arrête l'application
        if (nextQuestion == null) {

            // Demande à l'utilisateur de créer une nouvelle proposition possible
            Item newItem = new Item();

            System.out.println("");
            System.out.println("Je donne ma langue au chat... :(");
            System.out.println("Quel est le nom de votre animal?");
            newItem.setName( Console.input() );

            // Demande à l'utilisateur de créer une nouvelle question discriminante
            Question newQuestion = new Question();

            System.out.println("");
            System.out.println("Quelle question pourrais-je poser pour distinguer '" + item.getName() + "' de '" + newItem.getName() + "'?");
            newQuestion.setText( Console.input() );


            System.out.println("");
            System.out.println("Si je pose la question: '" + newQuestion.getText() + "', la réponse '" + newItem.getName() + "' correspond-elle à 'OUI'? [O/N]");
            Boolean newItemAnswer = Console.askYesOrNo();
            
            // La nouvelle question doit être rattachée à la dernière question à laquelle l'utilisateur a répondu...
            newQuestion.setParentQuestion( currentQuestion );
            // ...ainsi qu'à la réponse qu'il avait donnée
            newQuestion.setParentQuestionAnswer( answeredYes );
            // La nouvelle proposition doit être rattachée à la question que l'utilisateur vient de créer
            newItem.setParentQuestion( newQuestion );
            // ...ainsi qu'à la réponse à laquelle elle correspond
            newItem.setParentQuestionAnswer( newItemAnswer );
            // La proposition de l'application doit être rattachée à la question que l'utilisateur vient de créer
            item.setParentQuestion( newQuestion );
            // ...ainsi qu'à l'inverse de la réponse à laquelle correspond la nouvelle proposition
            item.setParentQuestionAnswer( !newItemAnswer );

            // Crée une interface adaptée au moteur de base de données configuré dans resources/META-INF/persistence.xml
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("TwentyQuestions");
            EntityManager manager = factory.createEntityManager();

            manager.getTransaction().begin();

            manager.persist(newQuestion);
            manager.persist(newItem);
            manager.merge(item);

            manager.getTransaction().commit();

            askForNewGame();

        } else {

            currentQuestion = nextQuestion;

        }
    }

    private void askForNewGame()
    {
        System.out.println("Voulez-vous rejouer? [O/N]");
        if ( Console.askYesOrNo() ) {
            init();
        } else {
            terminate();
        }
    }
    
}
