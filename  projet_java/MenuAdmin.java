
 package projet_java;

import java.sql.*;
import java.util.Scanner;

public class MenuAdmin {

    private static Scanner scanner = new Scanner(System.in);

    // Méthode pour afficher le menu de l'admin
    public void showMenu() {
        boolean running = true;
        while (running) {
            // Menu principal sans affichage coloré
            System.out.println("\n=============================");
            System.out.println("  Menu Admin");
            System.out.println("=============================");
            System.out.println("1. Créer un utilisateur");
            System.out.println("2. Supprimer un utilisateur par ID");
            System.out.println("3. Afficher les utilisateurs");
            System.out.println("4. Quitter");
            System.out.println("=================================");
            System.out.print("Choisissez une option : ");

            // Lire l'option choisie par l'admin
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante

            switch (choice) {
                case 1:               	
                    createUser(); // Créer un utilisateur
                    break;
                case 2:                	
                    deleteUserById(); // Supprimer un utilisateur
                    break;
                case 3:               	
                    displayUsers(); // Afficher les utilisateurs
                    break;
                case 4:              	
                    System.out.println("Au revoir ! À bientôt !");
                    running = false; // Quitter le menu
                    break;
                default:
                	
                    System.out.println(ConsoleColors.RED + "Choix invalide, veuillez réessayer." + ConsoleColors.RESET);
            }
        }
    }


    
    
}