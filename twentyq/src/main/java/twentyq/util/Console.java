package twentyq.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public final class Console {
    
    static private Console instance;
    private Scanner scanner;

    private Console()
    {
        scanner = new Scanner(System.in);
    }

    static public Console getInstance()
    {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    public static Boolean askYesOrNo()
    {
        Scanner scanner = getInstance().scanner;

        String userInput;
        Boolean valid = false;

        do {
            // Demande une saisie à l'utilisateur
            userInput = scanner.nextLine().trim().toUpperCase();
            
            // Vérifie que la saisie de l'utilisateur correspond bien à exactement 1 caractère parmi: o, O, n ou N
            if (Pattern.matches("^[oOnN]$", userInput)) {
                valid = true;
            // Si la saisie de l'utilisateur n'est pas valide, affiche un avertissement
            } else {
                System.out.println("Vous devez répondre par (O)ui ou par (N)on!");
            }

        } while (!valid);

        // Renvoie vrai si l'utilisateur a saisi "O", ou faux s'il a saisi "N"
        return "O".equals(userInput);
    }

}
