package application;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.layout.VBox;

import javafx.scene.layout.HBox;

public class SalesDataPage extends Page {
    public final ArrayList<Integer> years = new ArrayList<Integer>() {
        {
            add(2019);
            add(2020);
            add(2021);
            add(2022);
            add(2023);
            add(2024);
        }

    };

    private Button returnButton;
    private Button bookApprovalButton;
    private Button adminDashboardButton;
    private Button userManagementButton;
    private VBox buttonHolder;
    public final ArrayList<String> months = new ArrayList<String>() {
        {
            add("January");
            add("February");
            add("March");
            add("April");
            add("May");
            add("June");
            add("July");
            add("August");
            add("September");
            add("October");
            add("November");
            add("December");
        }
    };
    private JSONArray purchasedBooks;

    private ArrayList<Double> salesByMonth;
    BarChart<String, Double> totalSalesBarChart;

    ObservableList<XYChart.Series<Double, String>> anotherSales;
    private ComboBox<Integer> yearMenu;
    ObservableList<PieChart.Data> categoryData;
    ObservableList<PieChart.Data> conditionData;
    PieChart booksSoldByCategory;
    PieChart booksSoldByCondition;
    public SalesDataPage(Stage stage) {
        super(stage);
        setupSalesDataPage();
    }

    public void setupSalesDataPage() {
        displayYearMenu();
        getPurchasedBooks();
        setUpRedirectButtons();

        HBox totalLayout = new HBox();

        //totalSales = new XYChart.Series();
        salesByMonth = new ArrayList<>();
        initializeSalesByMonth();

        //totalSales = new XYChart.Series();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Total Sales");

        totalSalesBarChart = new BarChart(xAxis, yAxis);
        totalSalesBarChart.setMinWidth(670);
        initializeBarChart();

        VBox pieChartLayout = new VBox();

        booksSoldByCondition = new PieChart();
        booksSoldByCondition.setLabelsVisible(true);
        booksSoldByCondition.setTitle("Sales by Condition");
        booksSoldByCondition.setClockwise(true);
        booksSoldByCondition.setStartAngle(180);
        booksSoldByCondition.setMaxHeight(400);
        booksSoldByCondition.setMaxWidth(400);

        booksSoldByCategory = new PieChart();
        booksSoldByCategory.setLabelsVisible(true);
        booksSoldByCategory.setTitle("Sales by Category");
        booksSoldByCategory.setClockwise(true);
        booksSoldByCategory.setStartAngle(180);
        booksSoldByCategory.setMaxHeight(400);
        booksSoldByCategory.setMaxWidth(400);

        categoryData = FXCollections.observableArrayList();
        conditionData = FXCollections.observableArrayList();

        pieChartLayout.getChildren().addAll(booksSoldByCategory, booksSoldByCondition);


        yearMenu.setOnAction(actionEvent -> {
            int yearChosen = yearMenu.getValue();
            generateCategoryPieChartData(yearChosen);
            generateConditionPieChartData(yearChosen);
            generateTotalSalesData(yearChosen);
        });

        totalLayout.getChildren().addAll(pieChartLayout, totalSalesBarChart, buttonHolder);

        //testing functionality so far
        Group testGroup = new Group();
        testGroup.getChildren().addAll(totalLayout, yearMenu);
        Scene scene = new Scene(testGroup, 1200, 1500);
        stage.setScene(scene);

    }


    public void setUpRedirectButtons() {
        returnButton = new Button("Return");
        returnButton.setMinWidth(120);
        userManagementButton = new Button("View Users");
        userManagementButton.setMinWidth(120);
        bookApprovalButton = new Button("Approve Books");
        bookApprovalButton.setMinWidth(120);
        adminDashboardButton = new Button("Admin Dashboard");
        adminDashboardButton.setMinWidth(120);
        
        returnButton.setOnAction(actionEvent -> {
            new LoginPage(stage).display();
        });
        
        userManagementButton.setOnAction(actionEvent -> {
            new UserManagementPage(stage).display();
        });
        
        bookApprovalButton.setOnAction(actionEvent -> {
            new BookApprovalPage(stage).display();
        });
        
        adminDashboardButton.setOnAction(actionEvent -> {
            new AdminPage(stage).display();
        });
        

        buttonHolder = new VBox();
        buttonHolder.getChildren().setAll(returnButton, userManagementButton, bookApprovalButton, adminDashboardButton);
    }
    public void initializeSalesByMonth() {
        for(int i = 0; i < 12; ++i) {
            salesByMonth.add(0.00);
        }
    }

    public void clearSalesByMonth() {
        for(int i = 0; i < 12; ++i) {
            salesByMonth.set(i, 0.00);
        }
    }

    public void initializeBarChart() {
        XYChart.Series<String, Double> startData = new XYChart.Series<>();
        for(int i = 0; i < 12; ++i) {
            startData.getData().add(new XYChart.Data(months.get(i), 0.00));
        }
        totalSalesBarChart.getData().add(startData);
    }
    public void getPurchasedBooks() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/application/purchaseRecord.json"));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            purchasedBooks = new JSONArray(jsonBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateCategoryPieChartData(Integer year) {
        categoryData.clear();
        int numOfEnglishBooks = 0;
        int numOfScienceBooks = 0;
        int numOfHistoryBooks = 0;
        int numOfMathBooks = 0;
        int numOfArtBooks = 0;

        for(int i = 0; i < purchasedBooks.length(); ++i) {
            JSONObject book = purchasedBooks.getJSONObject(i);

            if(book.getInt("year") == year) {
                //System.out.println("Match Year");
                switch(book.getString("category")) {
                    case "English":
                        ++numOfEnglishBooks;
                        break;
                    case "History":
                        ++numOfHistoryBooks;
                        break;
                    case "Mathematics":
                        ++numOfMathBooks;
                        break;
                    case "Art":
                        ++numOfArtBooks;
                        break;
                    case "Science":
                        ++numOfScienceBooks;
                        break;
                    default:
                        break;
                }
            }
        }
        categoryData.addAll(new PieChart.Data("Mathematics", numOfMathBooks), new PieChart.Data("Science", numOfScienceBooks), new PieChart.Data("English", numOfEnglishBooks), new PieChart.Data("History", numOfHistoryBooks), new PieChart.Data("Art", numOfArtBooks));
        booksSoldByCategory.setData(categoryData);
}

    public void generateConditionPieChartData(Integer year) {
        conditionData.clear();
        int numOfNewBooks = 0;
        int numOfModeratelyUsedBooks = 0;
        int numOfOldBooks = 0;


        for(int i = 0; i < purchasedBooks.length(); ++i) {
            JSONObject book = purchasedBooks.getJSONObject(i);

            if(book.getInt("year") == year) {
                //System.out.println("Match Year");
                switch(book.getString("condition")) {
                    case "Used Like New":
                        ++numOfNewBooks;
                        break;
                    case "Moderately Used":
                        ++numOfModeratelyUsedBooks;
                        break;
                    case "Heavily Used":
                        ++numOfOldBooks;
                        break;
                    default:
                        break;
                }
            }
        }

        conditionData.addAll(new PieChart.Data("Used Like New", numOfNewBooks), new PieChart.Data("Moderately Used", numOfModeratelyUsedBooks), new PieChart.Data("Heavily Used", numOfOldBooks));
        booksSoldByCondition.setData(conditionData);

    }

    public void generateTotalSalesData(Integer year) {
        XYChart.Series<String, Double> totalSales = new XYChart.Series<>();
        //totalSales.getData().clear();
        totalSalesBarChart.getData().clear();
        clearSalesByMonth();

        for(int i = 0; i < purchasedBooks.length(); ++i) {
            JSONObject book = purchasedBooks.getJSONObject(i);
            if(book.getInt("year") == year) {
                String month = book.getString("month");
                Double bookPrice = book.getDouble("price");
                int monthIndex = months.indexOf(month);
               salesByMonth.set(monthIndex, bookPrice + salesByMonth.get(monthIndex));
            }
        }

        for(int i = 0; i < 12; ++i) {
            String month = months.get(i);
            Double salesInMonth = salesByMonth.get(i);

            totalSales.getData().add(new XYChart.Data(month, salesInMonth));

        }

        totalSalesBarChart.getData().add(totalSales);
    }

    public void displayYearMenu() {
        yearMenu = new ComboBox();
        yearMenu.getItems().addAll(years);
        yearMenu.setPromptText("Select Year");
    }
}
