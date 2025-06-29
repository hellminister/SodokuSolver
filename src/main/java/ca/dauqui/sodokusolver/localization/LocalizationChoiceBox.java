package ca.dauqui.sodokusolver.localization;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LocalizationChoiceBox extends ChoiceBox<String> {
    private static final Logger LOG = Logger.getLogger(LocalizationChoiceBox.class.getName());

    public LocalizationChoiceBox(boolean autoapply) {
        super();

        List<String> languages = new LinkedList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(Localize.getLocalizationFolder()), 1)){
            languages = walk.filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(s -> s.replace(".txt", "").strip())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.severe(e::toString);
        }
        setItems(FXCollections.observableList(languages));

        setValue(Localize.getLanguage());

        if (autoapply){
            setOnAction(_ -> Localize.setLanguage(getValue()));
        }
    }

    public LocalizationChoiceBox() {
        this(true);
    }

    public void apply(){
        Localize.setLanguage(getValue());
    }


}