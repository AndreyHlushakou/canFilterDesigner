package org.example;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.entity.FilterCanPair;
import org.example.entity.PairCanId;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.CalculationFilters.*;
import static org.example.HandlerFiltersCanId.getText;
import static org.example.WorkWithFile.*;

public class Ui extends Application {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private static final AtomicReference<File> FILE_READ_ATOMIC = new AtomicReference<>(null);
    private static final AtomicReference<File> FILE_WRITE_ATOMIC = new AtomicReference<>(null);

    private static final AtomicReference<List<Integer>> CAN_ID_LIST_ATOMIC = new AtomicReference<>(new ArrayList<>(0));
    private static final AtomicReference<Double> PENALTY_WEIGHT_ATOMIC = new AtomicReference<>(0.5);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Label globalLabel = new Label();

        VBox selectReadFile = getSelectReadFileBox(stage, globalLabel);
        VBox sliderQuality = getSliderQualityBox();
        VBox calculate = getCalculateBox(globalLabel);
        VBox selectWriteFile = getSelectWriteFileBox(stage, globalLabel);
        VBox save = getSaveBox(globalLabel);

        VBox calculateFilters = new VBox(10,
                selectReadFile,
                sliderQuality,
                calculate,
                selectWriteFile,
                save);
        VBox checkBox = getCheckBox(globalLabel);

        HBox root = new HBox(10, calculateFilters, checkBox);

        Scene scene = new Scene(root, 500, 370);
        stage.setTitle("Поиск CAN фильтров");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public VBox getCheckBox(Label globalLabel) {
        Label label = new Label("0. Проверка фильтра.");

        Label labelFilterMaskId = new Label("FilterMaskId");
        TextField fieldFilterMaskId = new TextField();
        HBox hBox1 = new HBox(10, labelFilterMaskId, fieldFilterMaskId);

        Label label2 = new Label("FilterId         ");
        TextField fieldFilterId = new TextField();
        HBox hBox2 = new HBox(10, label2, fieldFilterId);

        ScrollPane scrollPane = new ScrollPane(globalLabel);
        scrollPane.setPrefViewportHeight(210);
        scrollPane.setPrefViewportWidth(100);
        scrollPane.setVvalue(0);

        Button browseButton = new Button("Найти can id");
        browseButton.setOnAction(actionEvent -> {

            String filterMaskId = fieldFilterMaskId.getText();
            String filterId = fieldFilterId.getText();
            String text = getText(filterMaskId, filterId);
            globalLabel.setText(text);

        });

        return new VBox(10, label, hBox1, hBox2, browseButton, scrollPane);
    }

    public HBox getSelectFileBox(Stage primaryStage, Label globalLabel, AtomicReference<File> selectedFile) {
        // Создаем компоненты интерфейса
        TextField filePathField = new TextField();
        filePathField.setPromptText("Путь к файлу...");
        Button browseButton = new Button("Обзор");

        // Обработчик нажатия на кнопку
        browseButton.setOnAction(actionEvent -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");
            selectedFile.set(fileChooser.showOpenDialog(primaryStage)); // Открываем диалоговое окно для выбора файла
            if (selectedFile.get() != null) {
                filePathField.setText(selectedFile.get().getAbsolutePath());
            } else {
                globalLabel.setText("Ошибка4.\nПуть к файлу отсутствует.");
            }

        });
        return new HBox(10, browseButton, filePathField);
    }

    public VBox getSelectReadFileBox(Stage primaryStage, Label globalLabel) {
        Label label = new Label("1. Выберите файл для чтения.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, globalLabel, FILE_READ_ATOMIC);

        Button buttonReadFile = new Button("Прочитать.");
        buttonReadFile.setOnAction(actionEvent -> {

            File path = FILE_READ_ATOMIC.get();
            if (path != null) {
                if (checkPath(path)) {
                    List<Integer> listCanIdListFromFile = readFile(path);
                    if (!listCanIdListFromFile.isEmpty()) {
                        CAN_ID_LIST_ATOMIC.set(listCanIdListFromFile);
                        globalLabel.setText("Файл прочитан.\nМассив заполнен.");
                    } else {
                        globalLabel.setText("Ошибка3.\nФайл некорректный или пустой.");
                    }
                } else {
                    globalLabel.setText("Ошибка2.\nОшибка доступа к файлу.");
                }
            } else {
                globalLabel.setText("Ошибка1.\nВыберите сначала файл!");
            }

        });

        return new VBox(10, label, buttonPlusPath, buttonReadFile);
    }

    public VBox getSliderQualityBox() {
        Label label = new Label("2. Выберите точность.");

        Slider slider = new Slider(0.0, 1, 0.5);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(0.05);

        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(0.1);

        slider.setSnapToTicks(true);

        Label labelSlider = new Label("=" + PENALTY_WEIGHT_ATOMIC.get());
        Button btnSlider = new Button("Ввод");
        btnSlider.setOnAction(event -> {

            double value = slider.getValue();
            PENALTY_WEIGHT_ATOMIC.set(value);
            labelSlider.setText("=" + DECIMAL_FORMAT.format(value));

        });

        VBox vBoxSlider = new VBox(10, labelSlider, btnSlider);
        HBox sliderPlusButton = new HBox(10, slider, vBoxSlider);
        return new VBox(10, label, sliderPlusButton);
    }

    public VBox getCalculateBox(Label globalLabel) {
        Label label = new Label("3. Нажмите рассчитать.");

        Button calculateButton = new Button("Рассчитать");
        calculateButton.setOnAction(actionEvent -> {

            List<Integer> canIdList = CAN_ID_LIST_ATOMIC.get();
            if (!canIdList.isEmpty()) {
                process(canIdList, PENALTY_WEIGHT_ATOMIC.get());
                globalLabel.setText("Рассчитано.\n" + getReport());
            } else globalLabel.setText("Ошибка5.\nСначала заполните массив из файла.");

        });

        return new VBox(10, label, calculateButton);
    }

    public VBox getSelectWriteFileBox(Stage primaryStage, Label globalLabel) {
        Label label = new Label("4. Выберите файл для сохранения результата.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, globalLabel, FILE_WRITE_ATOMIC);
        return new VBox(10, label, buttonPlusPath);
    }

    public VBox getSaveBox(Label globalLabel) {
        Label label = new Label("5. Нажмите сохранить.");

        Button calculateButton = new Button("Сохранить");
        calculateButton.setOnAction(actionEvent -> {

            File path = FILE_WRITE_ATOMIC.get();
            if (path != null) {
                if (checkPath(path)) {

                    Set<Map.Entry<FilterCanPair, PairCanId>> data = getResult();
                    if (data != null) {
                        String dataStr = getData();
                        boolean isSuccessfully = writeFile(FILE_WRITE_ATOMIC.get(), dataStr);
                        if (isSuccessfully) {
                            globalLabel.setText("Сохранено\n\n" + dataStr);
                        } else globalLabel.setText("Ошибка7.\nФильтры не сохранены.");

                    } else globalLabel.setText("Ошибка6.\nФильтры еще не записаны.");

                } else {
                    globalLabel.setText("Ошибка2.\nОшибка доступа к файлу.");
                }
            } else {
                globalLabel.setText("Ошибка1.\nВыберите сначала файл!");
            }

        });

        return new VBox(10, label, calculateButton);
    }

}
