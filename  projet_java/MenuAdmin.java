
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


    // Méthode pour créer un utilisateur
    private void createUser() {
        System.out.println("===============================================");
        System.out.println("         C R E A T I O N  D' U T I L I S A T E U R");
        System.out.println("===============================================");
        System.out.print("Entrez le login de l'utilisateur : ");
        String username = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur : ");
        String password = scanner.nextLine();
        System.out.print("Entrez le rôle de l'utilisateur (professeur, etudiant) : ");
        String role = scanner.nextLine();

        // Vérification si le login existe déjà
        if (isUsernameExists(username)) {
            System.out.println(ConsoleColors.RED + "Le login existe déjà. Impossible de créer un nouvel utilisateur avec ce login." + ConsoleColors.RESET);
            return;
        }

        // Vérification si le rôle est admin
        if (role.equalsIgnoreCase("admin")) {
            System.out.println(ConsoleColors.RED + "Vous ne pouvez pas créer un autre utilisateur avec le rôle d'admin." + ConsoleColors.RESET);
            return;
        }

        // Création de l'utilisateur
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, role);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(ConsoleColors.GREEN + "Utilisateur créé avec succès !" + ConsoleColors.RESET);
                } else {
                    System.out.println(ConsoleColors.RED + "Erreur lors de la création de l'utilisateur." + ConsoleColors.RESET);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ConsoleColors.RED + "Erreur de connexion à la base de données." + ConsoleColors.RESET);
        }
    }


    
    
}