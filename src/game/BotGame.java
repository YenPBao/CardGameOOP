package game;

public class BotGame extends Client implements Runnable {
    public BotGame(int n) {
        super(n, true); // Gọi constructor của Client với isBot = true
        this.setPlayerName("Bot");
    }
    @Override
    public void run() {
        System.out.println("BotClient thread started for bot: " + this.getPlayerName());
    }


   
}