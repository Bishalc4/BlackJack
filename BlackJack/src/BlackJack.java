import java.util.ArrayList;
import java.util.Random;

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

    }

    ArrayList<Card> deck;
    Random random = new Random(); // shuffling deck

    BlackJack() {
        startGame();
    }

    public void startGame() {

        buildDeck(); // building deck
        shuffleDeck();

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

}
