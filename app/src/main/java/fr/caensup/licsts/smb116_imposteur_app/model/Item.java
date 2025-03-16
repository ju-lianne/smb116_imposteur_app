package fr.caensup.licsts.smb116_imposteur_app;

public class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name.toUpperCase();
    }
}
