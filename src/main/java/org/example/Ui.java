package org.example;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.GenerateCanIdByFilter.getText;

public class Ui extends Application {
    public AtomicReference<File> selectedFileRead = new AtomicReference<>(null);
    public AtomicReference<File> selectedFileWrite = new AtomicReference<>(null);
    public AtomicReference<Double> value = new AtomicReference<>(0.5);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox selectReadFile = getSelectReadFileBox(stage);
        VBox sliderQuality = getSliderQualityBox();
        VBox calculate = getCalculateBox();
        VBox selectWriteFile = getSelectWriteFileBox(stage);
        VBox save = getSaveBox();

        VBox calculateFilters = new VBox(10,
                selectReadFile,
                sliderQuality,
                calculate,
                selectWriteFile,
                save);
        VBox checkBox = getCheckBox();

        HBox root = new HBox(10, calculateFilters, checkBox);

        Scene scene = new Scene(root, 500, 370);
        stage.setTitle("Поиск CAN фильтров");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public VBox getCheckBox() {
        Label label = new Label("0. Проверка фильтра.");

        Label labelFilterMaskId = new Label("FilterMaskId");
        TextField fieldFilterMaskId = new TextField();
        HBox hBox1 = new HBox(10, labelFilterMaskId, fieldFilterMaskId);

        Label label2 = new Label("FilterId         ");
        TextField fieldFilterId = new TextField();
        HBox hBox2 = new HBox(10, label2, fieldFilterId);

        Label labelButton = new Label();
        ScrollPane scrollPane = new ScrollPane(labelButton);
        scrollPane.setPrefViewportHeight(210);
        scrollPane.setPrefViewportWidth(100);
        scrollPane.setVvalue(0);

        Button browseButton = new Button("Рассчитать");
        browseButton.setOnAction(actionEvent -> {
            String filterMaskId = fieldFilterMaskId.getText();
            String filterId = fieldFilterId.getText();
            String text = getText(filterMaskId, filterId);
            labelButton.setText(text);
        });

        return new VBox(10, label, hBox1, hBox2, browseButton, scrollPane);
    }

    public HBox getSelectFileBox(Stage primaryStage, AtomicReference<File> selectedFile) {
        // Создаем компоненты интерфейса
        TextField filePathField = new TextField();
        filePathField.setPromptText("Путь к файлу...");
        Button browseButton = new Button("Обзор");

        // Обработчик нажатия на кнопку
        browseButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");

            // Открываем диалоговое окно для выбора файла
            selectedFile.set(fileChooser.showOpenDialog(primaryStage));
            if (selectedFile.get() != null) {
                filePathField.setText(selectedFile.get().getAbsolutePath());
            }
        });
        return new HBox(10, browseButton, filePathField);
    }

    public VBox getSelectReadFileBox(Stage primaryStage) {
        Label label = new Label("1. Выберите файл, либо напишите к нему путь.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, selectedFileRead);
        return new VBox(10, label, buttonPlusPath);
    }

    public VBox getSliderQualityBox() {
        Label label = new Label("2. Выберите точность.");

        Label labelSlider = new Label("=" + value.get());

        Slider slider = new Slider(0.0, 1, 0.5);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(0.05);

        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(0.1);

        slider.setSnapToTicks(true);

        Button btnSlider = new Button("Ввод");
        btnSlider.setOnAction(event -> {
            value.set(slider.getValue());
            labelSlider.setText("=" + slider.getValue());
        });
        HBox sliderPlusButton = new HBox(10, slider, labelSlider);
        return new VBox(10, label, sliderPlusButton, btnSlider);

    }

    public VBox getCalculateBox() {
        Label label = new Label("3. Нажмите рассчитать.");

        Label labelMessage = new Label();

        Button calculateButton = new Button("Рассчитать");
        calculateButton.setOnAction(actionEvent -> {
            if (selectedFileRead.get() != null) {
                labelMessage.setText("123");
            }
            else {
                labelMessage.setText("456");
            }
        });
        HBox calculate = new HBox(10, calculateButton, labelMessage);

        return new VBox(10, label, calculate);
    }

    public VBox getSelectWriteFileBox(Stage primaryStage) {
        Label label = new Label("4. Выберите файл для сохранения результата.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, selectedFileWrite);
        return new VBox(10, label, buttonPlusPath);
    }

    public VBox getSaveBox() {
        Label label = new Label("5. Нажмите сохранить.");

        Label labelMessage = new Label();
//        labelMessage.setPrefWidth(10);

        Button calculateButton = new Button("Сохранить");
        calculateButton.setOnAction(actionEvent -> {
            if (selectedFileWrite.get() != null) {
                labelMessage.setText("123");
            }
            else {
                labelMessage.setText("456");
            }
        });
        HBox calculate = new HBox(10, calculateButton, labelMessage);

        return new VBox(10, label, calculate);
    }

}
