package projet_java;

import java.io.*;
import java.sql.*;
import java.util.*;

public class MenuProfesseur {

    private static Scanner scanner = new Scanner(System.in);

    // Méthode pour afficher le menu du professeur
    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=============================");
            System.out.println("  Menu Professeur ");
            System.out.println("=============================");
            System.out.println("1. Ajouter un examen");
            System.out.println("2. Afficher les examens");
            System.out.println("3. Supprimer un examen");
            System.out.println("4. Quitter");
            System.out.println("=================================");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante

            switch (choice) {
                case 1:
                    ajouterExamen(); // Ajouter un examen
                    break;
                case 2:
                    afficherExamens(); // Afficher les examens
                    break;
                case 3:
                    supprimerExamen(); // Supprimer un examen
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
