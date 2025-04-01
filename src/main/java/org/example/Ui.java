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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class Ui extends Application {
    public AtomicReference<File> pathFileRead = new AtomicReference<>(null);
    public AtomicReference<File> pathFileWrite = new AtomicReference<>(null);
    public AtomicReference<Double> penaltyWeight = new AtomicReference<>(0.5);
    public AtomicReference<Set<Map.Entry<FilterCanPair, PairCanId>>> setAtomicReference = new AtomicReference<>(null);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Label globalLabel = new Label();

        VBox selectReadFile = getSelectReadFileBox(stage);
        VBox sliderQuality = getSliderQualityBox();
        VBox calculate = getCalculateBox(globalLabel);
        VBox selectWriteFile = getSelectWriteFileBox(stage);
        VBox save = getSaveBox();

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

        Button browseButton = new Button("Рассчитать");
        browseButton.setOnAction(actionEvent -> {
            String filterMaskId = fieldFilterMaskId.getText();
            String filterId = fieldFilterId.getText();
            String text = HandlerFiltersCanId.getText(filterMaskId, filterId);
            globalLabel.setText(text);
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

    public HBox getButtonAction(String nameButton, Predicate<File> voidConsumer) {
        Label labelMessage = new Label();

        Button calculateButton = new Button(nameButton);
        calculateButton.setOnAction(actionEvent -> {
            if (WorkWithFile.checkPath(pathFileRead.get())) {
                File file = pathFileRead.get();
                boolean isSuccessfully = voidConsumer.test(file);

                if (isSuccessfully) {
                    labelMessage.setText("successfully");
                } else labelMessage.setText("not successfully");

            } else labelMessage.setText("incorrect file");
        });

        return new HBox(10, calculateButton, labelMessage);
    }

    public VBox getSelectReadFileBox(Stage primaryStage) {
        Label label = new Label("1. Выберите файл.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, pathFileRead);
        return new VBox(10, label, buttonPlusPath);
    }

    public VBox getSliderQualityBox() {
        Label label = new Label("2. Выберите точность.");

        Label labelSlider = new Label("=" + penaltyWeight.get());

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
            penaltyWeight.set(slider.getValue());
            labelSlider.setText("=" + slider.getValue());
        });
        HBox sliderPlusButton = new HBox(10, slider, labelSlider);
        return new VBox(10, label, sliderPlusButton, btnSlider);

    }

    public VBox getCalculateBox(Label globalLabel) {
        Label label = new Label("3. Нажмите рассчитать.");
        Predicate<File> predicate = (file) -> {
            List<Integer> CAN_ID_LIST = WorkWithFile.readFile(pathFileRead.get());
            if (!CAN_ID_LIST.isEmpty()) {
                CalculationFilters.process(CAN_ID_LIST, penaltyWeight.get());
                setAtomicReference.set(CalculationFilters.getResult());
                globalLabel.setText(CalculationFilters.getReport());
                return true;
            }
            return false;
        };
        HBox calculate = getButtonAction("Рассчитать", predicate);
        return new VBox(10, label, calculate);
    }

    public VBox getSelectWriteFileBox(Stage primaryStage) {
        Label label = new Label("4. Выберите файл для сохранения результата.");
        HBox buttonPlusPath = getSelectFileBox(primaryStage, pathFileWrite);
        return new VBox(10, label, buttonPlusPath);
    }

    public VBox getSaveBox() {
        Label label = new Label("5. Нажмите сохранить.");
        Predicate<File> voidConsumer = (file) -> {
            System.out.println("bbb");
            return true;
        };
        HBox calculate = getButtonAction("Сохранить", voidConsumer);
        return new VBox(10, label, calculate);
    }

}
