package fr.caensup.licsts.smb116_imposteur_app;

public class Player {
    protected String name;
    protected String word;
    protected Role role;

    public Player() {
    }

    public final void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return this.name;
    }

    public Role getRole() {
        return this.role;
    }

    public String getWord() {
        return this.word;
    }
}
