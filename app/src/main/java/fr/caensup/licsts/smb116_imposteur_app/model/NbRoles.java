package fr.caensup.licsts.smb116_imposteur_app;

public class NbRoles {
    public final int nbPlayers;
    public final int nbCivilian;
    public final int nbSpy;
    public final int nbWhitePage;

    public NbRoles(int nbPlayers, int nbCivilian, int nbSpy, int nbWhitePage) {
        this.nbPlayers = nbPlayers;
        this.nbCivilian = nbCivilian;
        this.nbSpy = nbSpy;
        this.nbWhitePage = nbWhitePage;
        validateRoles();
    }

    private void validateRoles() {
        int totalRoles = nbCivilian + nbSpy + nbWhitePage;
        if (totalRoles != nbPlayers) {
            throw new IllegalArgumentException(
                "La somme des rôles (" + totalRoles + ") ne correspond pas au nombre de joueurs (" + nbPlayers + ")");
        }
        if (nbCivilian <= 0) {
            throw new IllegalArgumentException("Il doit y avoir au moins un civil");
        }
        if (nbSpy < 0 || nbWhitePage < 0) {
            throw new IllegalArgumentException("Le nombre d'espions et de pages blanches ne peut pas être négatif");
        }
    }

    @Override
    public String toString() {
        return String.format("Joueurs: %d (Civils: %d, Espions: %d, Pages blanches: %d)",
            nbPlayers, nbCivilian, nbSpy, nbWhitePage);
    }
}
