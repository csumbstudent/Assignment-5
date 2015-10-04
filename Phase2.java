import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.lang.Comparable;
public class Phase2 {
    static int NUM_CARDS_PER_HAND = 7;
    static int NUM_PLAYERS = 2;
    static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
    static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
    public static void main(String[] args){

        CardTable table = new CardTable("Test", 20, 2);
        for(int i = 0; i < NUM_CARDS_PER_HAND; i++){
            computerLabels[i] = new JLabel();
            computerLabels[i].setIcon(GUICard.getBackCardIcon());
            humanLabels[i] = new JLabel();
            humanLabels[i].setIcon(GUICard.getIcon(generateRandomCard()));
            humanLabels[i].setMaximumSize(new Dimension(0, 0));
            table.pnlComputerHand.add(computerLabels[i]);
            table.pnlHumanHand.add(humanLabels[i]);
        }
        for(int i = 0; i < NUM_PLAYERS; i++){
            playedCardLabels[i] = new JLabel();
            playedCardLabels[i].setIcon(GUICard.getIcon(generateRandomCard()));
            playLabelText[i] = new JLabel();
            if(i == 0)
                playLabelText[i].setText("Computer");
            else
                playLabelText[i].setText("You");
            playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
            table.pnlPlayArea.add(playedCardLabels[i]);
        }
        for(JLabel label : playLabelText){
            label.setHorizontalAlignment(JLabel.CENTER);
            table.pnlPlayArea.add(label);
        }
        table.setVisible(true);
    }
    static Card generateRandomCard(){
        Card card = new Card();
        int value = (int)(Math.random()*(Card.validCardValues.length - 1));
        int suit = (int)(Math.random()*(Card.validCardSuits.length - 1));
        card.set(Card.validCardValues[value], Card.Suit.values()[suit]);
        return card;
    }
}
class GUICard{
    private static Icon[][] iconCards = new ImageIcon[14][4];
    private static Icon iconBack = new ImageIcon("../images/BK.gif");
    static boolean iconsLoaded = false;
    static final char[] VALID_SUITS = {'C', 'D', 'H', 'S'};
    public static Icon getIcon(Card card){
        if(!GUICard.iconsLoaded)
            GUICard.loadCardIcons();
        return iconCards[valueAsInt(card)][suitAsInt(card)];
    }
    private static void loadCardIcons(){
        for(int i = 0; i < Card.validCardValues.length; i++)
            for(int j = 0; j < VALID_SUITS.length; j++)
                iconCards[i][j] = new ImageIcon("../images/" + Card.validCardValues[i] + VALID_SUITS[j] + ".gif");
        GUICard.iconsLoaded = true;
    }
    private static int valueAsInt(Card card){
        String values = new String(Card.validCardValues);
        return values.indexOf(card.getValue());
    }
    private static int suitAsInt(Card card){
        return card.getSuit().ordinal();
    }
    public static Icon getBackCardIcon(){
        return GUICard.iconBack;
    }
}
class Card implements Comparable{
    //The four standard suits are supported.
    public enum Suit{clubs, diamonds, hearts, spades};
    private char value;
    private Suit suit;
    //errorFlag is set to true if the user tries to create or set a card's value
    //to one that is not in the validCardValues array. This will cause the card's
    //toString() method to indicate that the card is invalid.
    boolean errorFlag;
    //validCardValues holds values that a card is allowed to be.
    public static char[] validCardValues = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
    public static char[] validCardSuits = {'C', 'D', 'H', 'S'};
    public static char[] valueRanks = validCardValues;
    /* Card(char, Suit)
       In: A char representing the card's value, and a Suit representing the card's suit.
       Out: Nothing
       Description: This is a constructor that takes a value and a suit for a card. This will
                    create a card of the specified value and suit.
    */
    public int compareTo(Object t){
        if(t.getClass() != this.getClass())
            return 1;
        Card c = (Card)t;
        String strRanks = new String(valueRanks);
        if(strRanks.indexOf(c.getValue()) < 0)
            return 1;
        if(strRanks.indexOf(c.getValue()) < strRanks.indexOf(this.getValue()))
            return 1;
        if(strRanks.indexOf(c.getValue()) == strRanks.indexOf(this.getValue()))
            return 0;
        if(strRanks.indexOf(c.getValue()) > strRanks.indexOf(this.getValue()))
            return -1;
        return 1;
    }
    static void arraySort(Card[] cards, int arraySize){
        boolean swapped = false;
        do {
            swapped = false;
            for(int i = 1; i < arraySize; i++){
                if(cards[i-1].compareTo(cards[i]) > 0){
                    Card tmpCard = new Card(cards[i-1]);
                    cards[i-1] = new Card(cards[i]);
                    cards[i] = new Card(tmpCard);
                    swapped = true;
                }
            }
        } while(swapped);
    }

    public Card(char value, Suit suit){
        this.set(value, suit);
    }
    /* Card()
       In: Nothing
       Out: Nothing
       Description: This is a default constructor that takes no values. It will create an Ace of Spades.
    */
    public Card(){
        this.set('A', Suit.spades);
    }
    /* Card(Card)
       In: A Card object
       Out: Nothing
       Description: This is a copy constructor that returns a NEW card with the same values as the card
                    passed into it.
    */
    public Card(Card card){
        this.set(card.value, card.suit);
    }
    /* boolean set(char, Suit)
       In: A char representing the card's value and a Suit representing the card's suit.
       Out: True if the value and suit are valid, false if otherwise.
       Description: This set's the card's suit and value, if they are valid. Otherwise,
                    it sets the card's errorFlag to true.
    */
    public boolean set(char value, Suit suit){
        if(Card.isValid(value, suit)) {
            this.errorFlag = false;
            this.value = value;
            this.suit = suit;
            return true;
        }
        else{
            this.errorFlag = true;
            return false;
        }
    }
    /* boolean isValid(char, Suit)
       In: A char representing the card's value and a Suit representing its suit.
       Out: True if the value is valid, false if otherwise.
       Description: This function determines whether the value passed to it is a valid
                    value for a card. It checks the value against the valid values stored
                    in Card.validCardValues.
    */
    private static boolean isValid(char value, Suit suit){
        for(char validValue : Card.validCardValues)
            if(String.valueOf(validValue).toLowerCase().equals(String.valueOf(value).toLowerCase()))
                return true;
        return false;
    }
    /* char getValue()
       In: Nothing
       Out: A char holding the card's value.
       Description: This is an accessor for the card's value.
    */
    public char getValue(){
        return value;
    }
    /* Suit getSuit()
       In: Nothing
       Out: The card's suit type.
       Description: This is an accessor for the card's suit.
    */
    public Suit getSuit(){
        return this.suit;
    }
    /* String toString()
       In: Nothing
       Out: A String object containing the value and suit of the card,
            or [INVALID CARD] if the errorFlag is set to true.
       Description: This returns the card's value to the caller in String form.
    */
    public String toString(){
        if(this.errorFlag == true)
            return "[INVALID CARD]";
        else
            return this.value + " of " + suit.toString();
    }
    public boolean equals(Card c){
        if(this.getValue() == c.getValue() && this.getSuit() == c.getSuit())
            return true;
        return false;
    }
}
class Hand {
    public static final int MAX_CARDS = 50;
    private Card[] myCards = new Card[MAX_CARDS];
    int numCards = 0;
    /* Hand()
       In: Nothing
       Out: Nothing
       Description: The default constructor for Hand does not actually do anything.
    */
    public Hand(){
    }

    void sort(){
        Card.arraySort(this.myCards, numCards);
    }

    /* void resetHand()
       In: Nothing
       Out: Nothing
       Description: This sets the hand to its default state, containing no cards.
    */
    public void resetHand(){
        this.myCards = new Card[MAX_CARDS];
        this.numCards = 0;
    }
    /* boolean takeCard(Card)
       In: A Card object
       Out: True if there is room in the hand for the card, false if otherwise
       Description: This takes a Card object and places a copy of that object into the hand.
    */
    public boolean takeCard(Card card){
        if(this.numCards >= MAX_CARDS)
            return false;
        else {
            this.myCards[numCards] = new Card(card);
            this.numCards++;
            return true;
        }
    }
    /* Card playCard()
      In: Nothing
      Out: A Card object with the same values as the card on the top of the hand.
      Description: This creates a copy of the first card in the hand and returns it to the caller.
   */
    public Card playCard(){
        Card card = this.myCards[this.numCards-1];
        this.myCards[this.numCards-1] = null;
        this.numCards--;
        return card;
    }
    /* String toString()
      In: Nothing
      Out: A String object containing the cards in the hand.
      Description: This will provide a textual representation of the data contained in hand to the caller.
   */
    public String toString(){
        String handString = "( ";
        for(int i = 0; i <  this.numCards; i++){
            handString += this.myCards[i].toString();
            if(i != this.numCards - 1)
                handString += ", ";
        }
        handString += " )";
        return handString;
    }
    /* int getNumCards()
      In: Nothing
      Out: An integer whose value is the number of cards in the hand.
      Description: This is a basic accessor function.
   */
    public int getNumCards(){
        return this.numCards;
    }
    /* Card inspectCard(int)
       In: An integer representing the position of the card to be inspected.
       Out: A copy of the card at the specified position, or an invalid card if there is no
            card in that position.
       Description: This function returns a Card object whose values are equal to the card in
                    the specified position.
    */
    public Card inspectCard(int k){
        if(k >= this.numCards || k < 0)
            return new Card('0', Card.Suit.spades);
        else
            return new Card(this.myCards[k]);
    }
}
class CardTable extends JFrame {
    static final int MAX_CARDS_PER_HAND = 56;
    static final int MAX_PLAYERS = 2;
    private int numCardsPerHand;
    private int numPlayers;
    public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
    public CardTable(String title, int numCardsPerHand, int numPlayers){
        super();
        if(numCardsPerHand < 0 || numCardsPerHand > CardTable.MAX_CARDS_PER_HAND)
            this.numCardsPerHand = 20;
        this.numCardsPerHand = numCardsPerHand;
        if(numPlayers < 2 || numPlayers > CardTable.MAX_PLAYERS)
            this.numPlayers = numPlayers;
        if(title == null)
            title = "";
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        TitledBorder border = new TitledBorder("Computer Hand");
        pnlComputerHand = new JPanel();
        pnlComputerHand.setLayout(flowLayout);
        pnlComputerHand.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-50, 115));
        JScrollPane scrollComputerHand = new JScrollPane(pnlComputerHand);
        scrollComputerHand.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollComputerHand.setBorder(border);
        this.add(scrollComputerHand, BorderLayout.NORTH);

        border = new TitledBorder("Playing Area");
        GridLayout gridLayout = new GridLayout(2, 2);
        pnlPlayArea = new JPanel();
        pnlPlayArea.setBorder(border);
        pnlPlayArea.setLayout(gridLayout);
        this.add(pnlPlayArea, BorderLayout.CENTER);

        border = new TitledBorder("Human Hand");
        pnlHumanHand = new JPanel();
        pnlHumanHand.setLayout(flowLayout);
        pnlHumanHand.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-50, 115));
        JScrollPane scrollHumanHand = new JScrollPane(pnlHumanHand);
        scrollHumanHand.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollHumanHand.setBorder(border);
        this.add(scrollHumanHand, BorderLayout.SOUTH);


    }
}
class Deck {
    public static final short MAX_CARDS_IN_PACK = 56;
    public static final short MAX_PACKS = 6;
    public static final short MAX_CARDS = MAX_PACKS * MAX_CARDS_IN_PACK;
    //The masterPack is a pack of cards that the cards in the deck are built off of.
    //It contains one card for each value/suit combination. This is static,
    //as it does not change per object instantiated.
    private static Card[] masterPack = new Card[MAX_CARDS_IN_PACK];
    private Card[] cards; //The cards in the object's deck. Not static, as each deck object can have different cards.
    private int topCard; //The position of the card on the top of the deck.
    private int numPacks; //The deck can consist of multiple packs of cards.

    /* Deck(int)
       In: An integer specifying the number of packs to build the deck from.
       Out: Nothing
       Description: This is a constructor that will build a deck composed of the
                    specified number of packs.
    */
    public Deck(int numPacks) {
        //Build the master pack.
        this.allocateMasterPack();
        //If the user wants more packs than are available, give them the max.
        if (numPacks > Deck.MAX_PACKS)
            this.init(Deck.MAX_PACKS);
            //If the user wants 0 or less packs, give them one.
        else if (numPacks < 1)
            this.init(1);
        else
            //Otherwise, build the deck with the specified number of packs.
            this.init(numPacks);
    }
    /* Deck()
       In: None
       Out: Nothing
       Description: This default constructor builds a deck with one pack.
    */
    public Deck() {
        this.allocateMasterPack();
        this.init(1);
    }
    /* void init(int)
       In: An integer whose value is the number of packs to build the deck from.
       Out: Nothing
       Description: This will initialize the cards array data member to a complete deck built
                    from the specified number of packs.
    */
    public void init(int numPacks) {
        //Initialize the cards array.
        this.cards = new Card[numPacks * Deck.MAX_CARDS_IN_PACK];
        //Until the total number of cards are reached, keep adding cards from the
        //master pack.
        for (int i = 0; i < numPacks * Deck.MAX_CARDS_IN_PACK; i++) {
            this.cards[i] = this.masterPack[i % Deck.MAX_CARDS_IN_PACK];
        }
        //Set the top card to the last card allocated.
        this.topCard = numPacks * Deck.MAX_CARDS_IN_PACK;
    }
    /* void shuffle()
       In: Nothing
       Out: Nothing
       Description: This uses a Fisher-Yates shuffle to shuffle all of the cards in the
                    deck.
    */
    public void shuffle() {
        //Beginning with the top card, decrement i until i is 0.
        for (int i = this.topCard - 1; i >= 0; i--) {
            Card tmpCard = this.cards[i]; //Store the card at i, since it will be overwritten.
            //Choose a random card position from within the deck.
            int randomPosition = (int) (Math.random() * (this.topCard - 1));
            //Take the card from the random position and store it in the ith position.
            this.cards[i] = this.cards[randomPosition];
            //Take the card from the ith position, and put it into the randomly chosen position.
            this.cards[randomPosition] = tmpCard;
            //The cards have now been swapped.
        }
    }
    /* Card dealCard()
       In: Nothing
       Out: A copy of the Card object on the top of the deck.
       Description: This function makes a copy of the card on the top of the deck,
                    removes that card from the deck, and returns the copy to the caller.
    */
    public Card dealCard() {
        //Return an invalid card if there are no cards in the deck.
        if (this.topCard < 0)
            return new Card('0', Card.Suit.spades);
        else {
            //Create a copy of the card on the top of the deck.
            Card card = new Card(this.cards[this.topCard - 1]);
            //Set the actual card on the top of the deck to null, to destroy it.
            this.cards[this.topCard - 1] = null;
            //The topCard is now one less than it was.
            this.topCard--;
            //return the copy.
            return card;
        }
    }
    /* int getTopCard()
       In: Nothing
       Out: An integer whose value is the position of the top card in the deck.
       Description: This is a basic accessor function.
    */
    public int getTopCard() {
        return this.topCard;
    }
    /* Card inspectCard(int)
       In: An integer representing the position of the card to be inspected.
       Out: A copy of the card at the specified position, or an invalid card if there is no
            card in that position.
       Description: This function returns a Card object whose values are equal to the card in
                    the specified position.
    */
    public Card inspectCard(int k) {
        //If k is invalid, return an invalid card.
        if (k >= this.topCard || k < 0)
            return new Card('0', Card.Suit.spades);
        else
            //Otherwise, return a copy of the card in position k.
            return new Card(this.cards[k]);
    }
    /* void allocateMasterPack()
       In: Nothing
       Out: Nothing
       Description: This function fills the masterPack if it is not already filled. It fills the pack
                    with valid card values.
    */
    private static void allocateMasterPack() {
        //If Deck.masterPack is null, then it needs to be filled, otherwise, nothing needs to be done.
        if (Deck.masterPack != null) {
            //For each suit, fill the masterPack with each valid card value from that suit.
            for (int i = 0; i < Card.Suit.values().length; i++) {
                for (int j = 0; j < Card.validCardValues.length; j++) {
                    Deck.masterPack[i * Card.validCardValues.length + j] = new Card(Card.validCardValues[j], Card.Suit.values()[i]);
                }
            }
        }
    }

    public boolean addCard(Card card){
        int cardCount = 0;
        for(Card cardInDeck : this.cards)
            if(cardInDeck.equals(card))
                cardCount++;
        if(cardCount >= this.numPacks || this.topCard >= this.MAX_CARDS)
            return false;
        this.topCard++;
        this.cards[topCard - 1] = new Card(card);
        return true;
    }
    public int getNumCards(){
        return this.topCard;
    }
    public boolean removeCard(Card card){
        for(int i = 0; i < this.cards.length; i++)
            if(this.cards[i].equals(card)){
                this.cards[i] = new Card(this.cards[topCard - 1]);
                this.topCard--;
                return true;
            }
        return false;
    }
    public void sort(){
        Card.arraySort(this.cards, this.topCard);
    }
}