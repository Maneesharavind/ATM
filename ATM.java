package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ATM extends Application {
    private BankAccount userAccount;

    public static void main(String[] args) {
        launch(args);
    }

    public ATM() {
        // Empty constructor
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize user's bank account with an initial balance
        userAccount = new BankAccount(1000.0);

        // Set up layout
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 62, 80), CornerRadii.EMPTY, Insets.EMPTY)));

        // Create ATM menu
        Button withdrawButton = createStyledButton("Withdraw", Color.rgb(231, 76, 60));
        Button depositButton = createStyledButton("Deposit", Color.rgb(46, 204, 113));
        Button checkBalanceButton = createStyledButton("Check Balance", Color.rgb(52, 152, 219));

        // Create Labels
        Label menuLabel = new Label("ATM Menu");
        menuLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24;");

        Label balanceLabel = new Label("Current Balance: Rs" + userAccount.getBalance());
        balanceLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 20;");

        withdrawButton.setOnAction(event -> handleWithdrawal());
        depositButton.setOnAction(event -> handleDeposit());
        checkBalanceButton.setOnAction(event -> showBalance());

        // Add components to layout
        layout.getChildren().addAll(
                menuLabel,
                balanceLabel,
                withdrawButton,
                depositButton,
                checkBalanceButton
        );

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("ATM Machine");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, Color color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + toRGBCode(color) + "; -fx-text-fill: white; -fx-font-size: 18;");
        return button;
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void handleWithdrawal() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter the amount to withdraw:");
        dialog.showAndWait().ifPresent(amount -> {
            try {
                double withdrawalAmount = Double.parseDouble(amount);
                if (withdrawalAmount >= 0) {
                    if (userAccount.withdraw(withdrawalAmount)) {
                        showAlert("Withdrawal successful. Updated balance: Rs" + userAccount.getBalance());
                    } else {
                        showAlert("Insufficient funds. Withdrawal failed.");
                    }
                } else {
                    showAlert("Invalid amount. Withdrawal failed.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid number.");
            }
        });
    }

    private void handleDeposit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter the amount to deposit:");
        dialog.showAndWait().ifPresent(amount -> {
            try {
                double depositAmount = Double.parseDouble(amount);
                if (depositAmount >= 0) {
                    userAccount.deposit(depositAmount);
                    showAlert("Deposit successful. Updated balance: Rs" + userAccount.getBalance());
                } else {
                    showAlert("Invalid amount. Deposit failed.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid input. Please enter a valid number.");
            }
        });
    }

    private void showBalance() {
        showAlert("Current balance: Rs" + userAccount.getBalance());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        } else {
            balance -= amount;
            return true;
        }
    }
}
