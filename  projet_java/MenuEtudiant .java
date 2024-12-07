
package projet_java;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class MenuEtudiant {

    private static Scanner scanner = new Scanner(System.in);

    // Méthode pour afficher le menu de l'étudiant
    public void showMenu(User user) {
        boolean running = true;

        while (running) {
        	System.out.println("\n=============================");
            System.out.println("      Menu Étudiant ");
            System.out.println("=============================");
            System.out.println("1. Voir les examens disponibles");
            System.out.println("2. Passer un examen");
            System.out.println("3. Corriger un examen");
            System.out.println("4. Quitter");
            System.out.println("=================================");
            System.out.print("Choisissez une option : ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante
            
            switch (choice) {
                case 1:
                    afficherExamens(user); // Afficher les examens disponibles
                    break;
                case 2:
                    passerExamen(user); // Passer un examen
                    break;
                case 3:
                    corrigerExamen(user); // Corriger un examen
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    
}