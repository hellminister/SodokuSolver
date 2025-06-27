package ca.dauqui.sodokusolver.gui;

import ca.dauqui.sodokusolver.localization.Localizable;
import ca.dauqui.sodokusolver.localization.Localize;
import ca.dauqui.sodokusolver.localization.Localized;
import javafx.beans.property.ReadOnlyStringProperty;

@Localized
public enum LocalizedText implements Localizable {
    TITLE,
    SOLVE_BUTTON,
    RESET;

    private static final Localize<LocalizedText> local = new Localize<>(LocalizedText.class, "text.txt");

    @Override
    public ReadOnlyStringProperty localizedProperty() {
        return local.getString(this);
    }

    @Override
    public String toString() {
        return local.getString(this).get();
    }
}