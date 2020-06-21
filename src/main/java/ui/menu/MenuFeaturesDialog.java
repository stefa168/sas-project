package ui.menu;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;

public class MenuFeaturesDialog {
    public static class FeatureRow {
        public StringProperty featureName;
        public BooleanProperty featureValue;

        public FeatureRow() {
            featureName = new SimpleStringProperty("");
            featureValue = new SimpleBooleanProperty(false);
        }

        public void setFeatureName(String n) {
            featureName.setValue(n);
        }

        public String getFeatureName() {
            return featureName.getValue();
        }

        public void setFeatureValue(boolean b) {
            featureValue.setValue(b);
        }

        public boolean getFeatureValue() {
            return featureValue.getValue();
        }
    }

    @FXML
    TableView<FeatureRow> featuresTable;
    ObservableList<FeatureRow> rows;

    Stage myStage;



    public void initialize() {
        ObservableMap<String, Boolean> features = CatERing.getInstance().getMenuManager().getCurrentMenu().getFeatures();

        // Creo un table model a partire dalla mappa di features
        ArrayList<String> fnames = new ArrayList<>();
        fnames.addAll(features.keySet());
        Collections.sort(fnames);
        rows = FXCollections.observableArrayList();
        for (String s: fnames) {
            FeatureRow row = new FeatureRow();
            row.featureName = new SimpleStringProperty(s);
            row.featureValue = new SimpleBooleanProperty(features.get(s));
            rows.add(row);
        }

        featuresTable.setItems(rows);
        TableColumn<FeatureRow, String> featureNameCol = new TableColumn<>("Feature");
        featureNameCol.setCellValueFactory(new PropertyValueFactory<>("featureName"));
        TableColumn<FeatureRow, Boolean> featureValCol = new TableColumn<>("Value");
        featureValCol.setCellValueFactory(new PropertyValueFactory<>("featureValue"));
        featureValCol.setCellFactory(c -> new CheckBoxTableCell<>(new Callback<Integer, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Integer integer) {
                return rows.get(integer).featureValue;
            }
        }));

        featuresTable.getColumns().add(featureNameCol);
        featuresTable.getColumns().add(featureValCol);
        featuresTable.setEditable(true);
        featureNameCol.setEditable(false);
        featureValCol.setEditable(true);
    }

    public void setOwnStage(Stage stage) {
        myStage = stage;
    }

    @FXML
    public void okButtonPressed() {
        String[] features = new String[rows.size()];
        boolean[] vals = new boolean[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            FeatureRow fr = rows.get(i);
            features[i] = fr.featureName.getValue();
            vals[i] = fr.featureValue.getValue();
        }
        try {
            CatERing.getInstance().getMenuManager().setAdditionalFeatures(features, vals);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }
        this.myStage.close();
    }

    @FXML public void annullaButtonPressed() {
        this.myStage.close();
    }
}
