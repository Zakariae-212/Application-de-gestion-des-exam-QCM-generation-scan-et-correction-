
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




    // Méthode pour afficher les examens disponibles
    private void afficherExamens(User user) {
        System.out.println("\nExamen(s) disponible(s) :");
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Requête SQL pour récupérer les examens disponibles pour l'étudiant
            String query = "SELECT * FROM exams";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int examId = rs.getInt("id");
                    String examName = rs.getString("name");
                    System.out.println("ID: " + examId + " - Nom de l'examen: " + examName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'affichage des examens.");
        }
    }

    
}