package utils;

import android.app.Activity;
import android.app.AlertDialog;

public class Utils {
    public static void showExitConfirmation(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Confirmer la fermeture")
                .setMessage("Voulez-vous vraiment quitter l'application ?")
                .setPositiveButton("Oui", (dialog, which) -> activity.finishAffinity())
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
