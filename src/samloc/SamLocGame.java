package samloc;

import javax.swing.JOptionPane;

import game.BotGame;
import game.Client;

public class SamLocGame {
	public static void main(String[] args) {
        String input = JOptionPane.showInputDialog(null, "Nhập số lượng người chơi:");
        int numberOfPlayers = 0;
        try {
            numberOfPlayers = Integer.parseInt(input); 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập một số nguyên hợp lệ.");
            return; 
        }
        String input2 = JOptionPane.showInputDialog(null, "Nhập số lượng máy chơi:");
        int numberOfBots = 0;
        try {
            numberOfBots = Integer.parseInt(input2); 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập một số nguyên hợp lệ.");
            return; 
        }

        SamLocServer server = new SamLocServer(numberOfPlayers);
        new Thread(() -> server.start(1234)).start();  
        
        Client[] clients = new Client[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers - numberOfBots; i++) {
            clients[i] = new SamLocClient(numberOfPlayers);
        }

        for (int i = numberOfPlayers - numberOfBots; i < numberOfPlayers; i++) {
           clients[i] = new SamLocBot(numberOfPlayers);
        }
    }
}
