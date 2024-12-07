
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


    // Méthode pour corriger un examen
    private void corrigerExamen(User user) {
        System.out.print("\nEntrez l'ID de l'examen que vous souhaitez corriger : ");
        int examId = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        try {
            // Vérifier l'existence du fichier de réponses de l'étudiant
            String studentFilePath = "C:\\Users\\ramiz\\OneDrive\\Documents\\dossier_etudiant\\etudiant_" + user.getId() + "exams" + examId + ".txt";
            File studentFile = new File(studentFilePath);
            if (!studentFile.exists()) {
                System.out.println("Le fichier de l'examen de l'étudiant n'existe pas.");
                return;
            }

            // Vérifier l'existence du fichier de l'examen (réponses du professeur)
            String examFilePath = "C:\\Users\\ramiz\\OneDrive\\Documents\\dossier_professeur\\examen_" + examId + ".txt";
            File examFile = new File(examFilePath);
            if (!examFile.exists()) {
                System.out.println("Le fichier de l'examen n'existe pas.");
                return;
            }

            // Lire les réponses de l'étudiant
            List<String> studentResponses = lireFichier(studentFile);
            // Lire les bonnes réponses du professeur
            List<String> correctAnswers = lireFichier(examFile);

            // Comparer les réponses de l'étudiant avec celles du professeur
            List<String> correction = new ArrayList<>();
            int score = 0; // Initialiser le score

            for (int i = 0; i < studentResponses.size(); i++) {
                String studentAnswer = studentResponses.get(i).trim();
                String correctAnswer = correctAnswers.get(i).trim();
                String result;

                if (studentAnswer.equals(correctAnswer)) {
                    result = "Vrai : La bonne réponse est " + correctAnswer;
                    score++;  // Ajouter un point pour la bonne réponse
                } else {
                    result = "Faux : La bonne réponse est " + correctAnswer;
                }

                correction.add("Question " + (i + 1) + ": " + result);
            }

            // Ajouter le score final à la correction
            correction.add("\nScore final : " + score + " / " + studentResponses.size());

            // Enregistrer la correction dans un fichier
            String correctionFilePath = "C:\\Users\\ramiz\\OneDrive\\Documents\\dossier_correction\\correction-" + examId + "-etudiant" + user.getId() + ".txt";
            enregistrerCorrection(correctionFilePath, correction);

            System.out.println("La correction de votre examen a été enregistrée avec un score de " + score + ".");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la correction de l'examen.");
        }
    }


    // Méthode pour lire un fichier et retourner son contenu sous forme de liste de chaînes
    private List<String> lireFichier(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }


    // Méthode pour enregistrer la correction dans un fichier
    private void enregistrerCorrection(String filePath, List<String> correction) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : correction) {
                writer.write(line);
                writer.newLine();
            }
        }
    }







    // Méthode pour permettre à l'étudiant de passer un examen
    private void passerExamen(User user) {
        System.out.print("\nEntrez l'ID de l'examen que vous souhaitez passer : ");
        int examId = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Vérifier si l'examen existe
            String checkExamQuery = "SELECT * FROM exams WHERE id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkExamQuery)) {
                checkStmt.setInt(1, examId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Vous avez choisi l'examen : " + rs.getString("name"));
                        // Passer les questions
                        passerQuestions(connection, examId, user.getId());
                    } else {
                        System.out.println("Examen introuvable.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération de l'examen.");
        }
    }


    // Méthode pour passer les questions d'un examen
    private void passerQuestions(Connection connection, int examId, int userId) throws SQLException {
        // Supprimer les anciennes réponses de l'étudiant pour cet examen
        supprimerAnciennesRéponses(connection, userId, examId);

        String queryQuestions = "SELECT * FROM questions WHERE exam_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryQuestions)) {
            stmt.setInt(1, examId);
            try (ResultSet rs = stmt.executeQuery()) {
                int questionNumber = 1;
                while (rs.next()) {
                    int questionId = rs.getInt("id");
                    String questionText = rs.getString("question_text");
                    System.out.println("\nQuestion " + questionNumber + ": " + questionText);
                    int selectedOptionId = afficherOptions(connection, questionId);
                    enregistrerRéponse(userId, examId, questionId, selectedOptionId); // Enregistrer la réponse dans la BDD
                    enregistrerRéponseDansFichier(userId, examId, questionId, selectedOptionId); // Enregistrer la réponse dans un fichier
                    questionNumber++;
                }
                System.out.println("Merci d'avoir passé l'examen !");
            }
        }
    }



    // Méthode pour supprimer les anciennes réponses de l'étudiant pour cet examen
    private void supprimerAnciennesRéponses(Connection connection, int userId, int examId) {
        try {
            String deleteQuery = "DELETE FROM student_responses WHERE student_id = ? AND exam_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.setInt(2, examId);
                deleteStmt.executeUpdate();
                System.out.println("Anciennes réponses supprimées.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la suppression des anciennes réponses.");
        }
    }


    
}