package com.gajek.casinogame.Controllers;

import com.gajek.casinogame.Command.Command;
import com.gajek.casinogame.Command.HitCommand;
import com.gajek.casinogame.Command.StandCommand;
import com.gajek.casinogame.Models.Card;
import com.gajek.casinogame.Models.Deck;
import com.gajek.casinogame.Models.Player;
import com.gajek.casinogame.State.GameContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BlackjackGameController {
    @FXML
    private Button returnToMenuButton;
    private Deck deck;
    private Player player;
    private Player dealer;
    @FXML
    private Label playerHandValueLabel;
    @FXML
    private Label dealerHandValueLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button hitButton;
    @FXML
    private Button standButton;
    @FXML
    private HBox playerHand;
    @FXML
    private HBox dealerHand;
    @FXML
    private TextField betAmountField;
    private GameContext gameContext;
    private Command hitCommand;
    private Command standCommand;
    private boolean showDealerHandValue = false;
    @FXML
    public void initialize() {
        deck = new Deck();
        player = new Player(1000);
        dealer = new Player(0);
        gameContext = new GameContext(player, dealer, deck);

        deck.shuffle();
        updateUI();

        gameContext.setResultHandler(this::updateStatusLabel);

        hitCommand = new HitCommand(player, deck, gameContext);
        standCommand = new StandCommand(gameContext);
    }

    @FXML
    private void onHit() {
        hitCommand.execute();
        if (player.getHand().getValue() > 21) {
            disablePlayerActions();
        }
        updateUI();
    }

    @FXML
    private void onStand() {
        standCommand.execute();
        disablePlayerActions();

        updateUI();
    }

    @FXML
    private void placeBet() {
        gameContext.resetGame();
        enablePlayerActions();
        showDealerHandValue = false;
        try {
            int betAmount = Integer.parseInt(betAmountField.getText());
            if (betAmount > 0 && betAmount <= player.getBalance()) {
                player.setBalance(player.getBalance() - betAmount);
                dealInitialCards();
            } else {
                statusLabel.setText("Invalid bet amount");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid number");
        }
        int betAmount = Integer.parseInt(betAmountField.getText());
        gameContext.setCurrentBet(betAmount);
        updateUI();
    }
    public void addCardToHand(Card card, HBox hand) {
        hand.getChildren().add(createCardView(card));
    }

    private ImageView createCardView(Card card) {
        ImageView cardView = new ImageView();
        Image cardImage = new Image(getImagePathForCard(card), 90, 130.68, false, true);

        cardView.setImage(cardImage);
        return cardView;
    }

    private String getImagePathForCard(Card card) {
        return "/images/cards/" + card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
    }


    private void disablePlayerActions() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        showDealerHandValue = true;
    }

    private void enablePlayerActions() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
    }

    private void updateStatusLabel(String result) {
        Platform.runLater(() -> statusLabel.setText(result));
    }

    public void dealInitialCards() {
        player.getHand().addCard(deck.dealCard());
        player.getHand().addCard(deck.dealCard());

        dealer.getHand().addCard(deck.dealCard());
        Card dealerSecondCard = deck.dealCard();
        dealer.getHand().addCard(dealerSecondCard);

        addCardToHand(player.getHand().getCards().get(0), playerHand);
        addCardToHand(player.getHand().getCards().get(1), playerHand);
        addCardToHand(dealer.getHand().getCards().get(0), dealerHand);
        ImageView faceDownCard = new ImageView(new Image("/images/cards/back.png"));
        dealerHand.getChildren().add(faceDownCard);

        updateUI();
    }

    public void updateUI() {
        playerHand.getChildren().clear();
        dealerHand.getChildren().clear();
        System.out.println(showDealerHandValue);

        for (Card card : player.getHand().getCards()) {
            ImageView cardView = createCardView(card);
            playerHand.getChildren().add(cardView);
        }

        for (Card card : dealer.getHand().getCards()) {
            ImageView cardView = createCardView(card);
            dealerHand.getChildren().add(cardView);
        }

        if (dealerHand.getChildren().size() > 1 && !showDealerHandValue) {
            ((ImageView) dealerHand.getChildren().get(1)).setImage(new Image("/images/cards/back.png", 90, 130.68, false, true));
        }

        int playerHandValue = player.getHand().getValue();

        if (showDealerHandValue) {
            int dealerHandValue = dealer.getHand().getValue();
            dealerHandValueLabel.setText("Dealer Hand Value: " + dealerHandValue);
        } else {
            dealerHandValueLabel.setText("Dealer Hand Value");
        }
        playerHandValueLabel.setText("Player Hand Value: " + playerHandValue);

        balanceLabel.setText("Balance: " + player.getBalance());
    }


    @FXML
    private void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gajek/casinogame/casinoDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnToMenuButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 800);
            stage.setScene(scene);
            stage.show();

            MainMenuController controller = loader.getController();
            if (controller instanceof IStageAwareController) {
                ((IStageAwareController) controller).setMainStage(stage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
