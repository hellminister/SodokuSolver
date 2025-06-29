package ca.dauqui.sodokusolver.localization;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class is used to Localize strings and to permit change of language without having to restart the class
 * It needs to be linked to an enum that will provide a link to the localized string
 * <p>
 * It can also be used as stand alone using the constructor asking the language as parameter
 * @param <T> the enum used as labels for the localized strings
 */
public class Localize<T extends Enum<T>> {
    private static final Logger LOG = Logger.getLogger(Localize.class.getName());

    private static String LOCALIZATION_FOLDER_PATH = "src/main/resources/language/";

    /**
     * Contains the language to load
     * It is static so that when you change this value, all instance of this class updates to the new language
     * also keeping it to know which language the class is set for
     */
    private static final StringProperty language = new SimpleStringProperty("");
    private static final PathProperty localizationFolder = new PathProperty();

    /**
     * To set the localization folder path if it is different that the default
     * (src/main/resources/language/)
     * @param path the path of the localization folder
     */
    public static void setLocalizationFolderPath(String path) {
        LOCALIZATION_FOLDER_PATH = path;
    }

    /**
     * @return the localization folder string
     */
    public static String getLocalizationFolder() {
        return LOCALIZATION_FOLDER_PATH;
    }

    /**
     * Sets the language to use, affects all instances of this class
     * @param language the language to use
     */
    public static void setLanguage(String language){
        // order here is important, we want to set the path to use before triggering the updates from the listener
        localizationFolder.set(Paths.get(LOCALIZATION_FOLDER_PATH, language));
        Localize.language.set(language);
    }

    private final Map<T, StringProperty> localizedStrings;

    private final Class<T> enumType;

    // the name of the file containing the localized strings
    private final String localizeFor;

    // the language of this localization
    private final String localLanguage;

    /**
     * Use this constructor to have the localization follow the setLanguage static function
     * @param enumType the enum type used to represent the localized strings
     * @param localizeFor the name of the file containing the localized strings
     */
    public Localize(Class<T> enumType, String localizeFor) {
        this.enumType = enumType;
        this.localizeFor = localizeFor;
        localLanguage = "";
        EnumMap<T, StringProperty> temp = new EnumMap<>(enumType);
        Arrays.stream(enumType.getEnumConstants()).forEach(target -> temp.put(target, new SimpleStringProperty(target.name() + " not defined")));

        localizedStrings = Collections.unmodifiableMap(temp);

        // adding a listener to language so that this instance updates itself when the language changes
        language.addListener((_, oldV, newV) -> {
            if (!oldV.equalsIgnoreCase(newV) && !newV.isBlank()) {
                loadLanguage();
            }
        });

        // loading the localization file if a language is set
        if (!language.get().isBlank()){
            loadLanguage();
        }
    }

    /**
     * Use this constructor to have a localization unlinked from the static language
     * @param enumType the enum type used to represent the localized strings
     * @param localizeFor the name of the file containing the localized strings
     * @param language the language to use for this instance
     */
    public Localize(Class<T> enumType, String localizeFor, String language) {
        this.enumType = enumType;
        this.localizeFor = localizeFor;
        this.localLanguage = language;
        EnumMap<T, StringProperty> temp = new EnumMap<>(enumType);
        Arrays.stream(enumType.getEnumConstants()).forEach(target -> temp.put(target, new SimpleStringProperty(target.name() + " not defined")));

        localizedStrings = Collections.unmodifiableMap(temp);
        try {
            Path path = (Paths.get(LOCALIZATION_FOLDER_PATH, language, localizeFor));
            load(path);
        } catch (InvalidPathException _){
            LOG.severe(() -> "Error finding localization file : " + LOCALIZATION_FOLDER_PATH + " " + language + " " + localizeFor);
        }
    }

    public static String getLanguage() {
        return language.get();
    }



    /**
     * @return the language used by this instance or blank ("") if it is linked to the static language value
     */
    public String getLocalLanguage(){
        return localLanguage;
    }

    /**
     * @param target the wanted String name
     * @return the wanted String Property
     */
    public ReadOnlyStringProperty getString(T target){
        return localizedStrings.get(target);
    }

    private void loadLanguage() {
        try {
            Path path = localizationFolder.get().resolve(localizeFor);
            load(path);
        } catch (InvalidPathException _){
            LOG.severe(() -> "Error finding localization file : " + localizationFolder + " " + localizeFor);
        }
    }

    private void load(Path path) {
        Set<T> notLoaded = new HashSet<>(localizedStrings.keySet());
        String line = "";
        try (BufferedReader br = Files.newBufferedReader(path)) {
            line = br.readLine();

            while (line != null) {
                if (!line.isBlank() && !line.startsWith("#")) {
                    String[] text = line.split(" ", 2);

                    loadTargetAndText(text, notLoaded);
                }

                line = br.readLine();
            }

        } catch (IOException _) {
            LOG.severe(() -> "Error loading localization file : " + path);
        } catch (Exception _) {
            String finalLine = line;
            LOG.warning(() -> "tries to do something with " + finalLine);
        }

        if (!notLoaded.isEmpty()) {
            LOG.severe(() -> "Missing localization of these entries : " + notLoaded);
        }
    }

    private void loadTargetAndText(String[] text, Set<T> notLoaded) {
        try {
            T key = T.valueOf(enumType, text[0]);
            localizedStrings.get(key).setValue(text[1]);
            notLoaded.remove(key);
        } catch(IllegalArgumentException _) {
            LOG.severe(() -> "Target value not found " + text[0] + ".");
        }
    }

}