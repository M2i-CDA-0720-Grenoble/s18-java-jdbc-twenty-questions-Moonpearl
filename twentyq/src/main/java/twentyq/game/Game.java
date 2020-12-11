package twentyq.game;

import twentyq.util.Console;
import twentyq.util.QueryManager;
import twentyq.entity.Question;
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
        currentQuestion = QueryManager.findNextQuestion(currentQuestion, answeredYes);
        // S'il ne reste plus de question à poser, arrête l'application
        if (currentQuestion == null) {
            askForNewGame();
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
