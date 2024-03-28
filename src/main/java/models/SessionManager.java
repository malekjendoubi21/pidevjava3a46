package models;
public class SessionManager {
    private static user currentUser;

    // Méthode pour définir l'utilisateur actuellement connecté
    public static void setCurrentUser(user user) {
        currentUser = user;
    }

    // Méthode pour récupérer l'utilisateur actuellement connecté
    public static user getCurrentUser() {
        return currentUser;
    }
}
