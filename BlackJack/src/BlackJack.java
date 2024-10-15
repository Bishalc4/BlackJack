import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BlackJack {
    private class Card {
        String value;
        String type;

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("JQKA".contains(value)) {
                if (value == "A") {
                    return 11;
                }

                return 10; // return value 10 for J,Q,K
            }

            return Integer.parseInt(value); // retrun value 2-10
        }

        public boolean isAce() {
            return value == "A";
        }

        public String getImagePath() {
            return "./cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random(); // shuffling deck

    // dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // interface
    int boardWidth = 700;
    int boardHeight = 700;
    int cardWidth = 130;
    int cardHeight = 182;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamPanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                // draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                // draw dealer hand
                for (int i = 0; i < dealerHand.size(); ++i) {
                    Card card = dealerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }

                // draw player hand
                for (int i = 0; i < playerHand.size(); ++i) {
                    Card card = playerHand.get(i);
                    Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImage, 20 + (cardWidth + 5) * i, 340, cardWidth, cardHeight, null);
                }

                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";

                    if (playerSum > 21) {
                        message = "You Lose!";
                    }

                    else if (dealerSum > 21) {
                        message = "You Win!";
                    }

                    else if (playerSum == dealerSum) {
                        message = "Push!";
                    }

                    else if (playerSum > dealerSum) {
                        message = "You Win!";
                    }

                    else if (playerSum < dealerSum) {
                        message = "You Lose!";
                    }

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 270);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttoPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamPanel.setLayout(new BorderLayout());
        gamPanel.setBackground(new Color(53, 101, 77));
        frame.add(gamPanel);

        hitButton.setFocusable(false);
        buttoPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttoPanel.add(stayButton);
        frame.add(buttoPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                if (card.isAce()) {
                    playerAceCount += 1;
                }
                playerHand.add(card);

                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }

                gamPanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    if (card.isAce()) {
                        dealerAceCount += 1;
                    }
                    dealerHand.add(card);
                }
                gamPanel.repaint();
            }
        });

        gamPanel.repaint();
    }

    public void startGame() {

        buildDeck(); // building deck
        shuffleDeck(); // shuffling the deck

        // dealer hand
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); // remove card at the last index
        dealerSum += hiddenCard.getValue();
        if (hiddenCard.isAce()) {
            dealerAceCount += 1;
        }

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        if (card.isAce()) {
            dealerAceCount += 1;
        }

        dealerHand.add(card);

        System.out.println("DEALER");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // player hand
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; ++i) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            if (card.isAce()) {
                dealerAceCount += 1;
            }
            playerHand.add(card);
        }

        System.out.println("Player");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);

    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] types = { "C", "D", "H", "S" };

        for (int i = 0; i < types.length; ++i) {
            for (int j = 0; j < values.length; ++j) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); ++i) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

}
