package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import card.Deck;
import message.Message;

public class Server {
    // Tên của server
    private String serverName;
    // Số người chơi tối đa
    private int maxNumOfPlayers;
    // Socket của Clients
    private Socket[] clientSockets;
    // ObjectOutputStreams của clients
    private ObjectOutputStream[] clientOutputStreams;
    // Tên của các Clients
    private String[] clientNames;
    // Trạng thái sẵn sàng
    private boolean[] clientReadyStates;
    // số người chơi hiện tại
    private int numOfPlayers = 0;
    // frame chính 
    private JFrame frame = null;
    // vùng thông báo
    private JTextArea textArea = null;
    // server đang hoạt động hay không
    private boolean serverUp = false;
    // Người chơi hiện tại
    private int currentPlayer ;

    public Server(String serverName, int maxNumOfPlayers) {
        this.serverName = serverName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        clientSockets = new Socket[maxNumOfPlayers];
        clientOutputStreams = new ObjectOutputStream[maxNumOfPlayers];
        clientNames = new String[maxNumOfPlayers];
        clientReadyStates = new boolean[maxNumOfPlayers];

        buildGUI();
    }

    // Cửa sổ Server
    private void buildGUI() {
        frame = new JFrame(serverName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 450);

        textArea = new JTextArea(350, 450);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.BLACK); // Nền đen
        textArea.setForeground(Color.GREEN); // Chữ xanh lá cây
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Font chữ Monospaced
        textArea.setCaretColor(Color.GREEN);
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(scroller);

        frame.setVisible(true);
    }

    // chạy server tại một cổng nào đó 
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            println("Server Tiến lên : " + serverSocket.getLocalPort());
            serverUp = true;
            while (serverUp) {
                Socket clientSocket = serverSocket.accept();
                addConnection(clientSocket);
            }
            serverSocket.close();
        } catch (Exception ex) {
            println("Lỗi ở server: " + port);
            ex.printStackTrace();
        }
    }

    private synchronized void parseMessage(Socket clientSocket,
                                           Message message) {
        for (int i = 0; i < maxNumOfPlayers; i++) {
            if (clientSockets[i] == clientSocket) {
                message.setPlayerID(i);
                break;
            }
        }

        switch (message.getType()) {
            case Message.JOIN:
                addPlayer(clientSocket, (String) message.getData());
                break;
            case Message.READY:
                setReadyState(clientSocket);
                break;
            case Message.MOVE:
                println("Broadcasts a \"MOVE\" message from "
                        + clientSocket.getRemoteSocketAddress());
                broadcastMessage(message);

                // Sau khi nhận MOVE từ currentPlayer, xác định người chơi tiếp theo
                currentPlayer = getNextPlayer(currentPlayer);
                System.out.print(currentPlayer);

                // Gửi YOUR_TURN tới người chơi tiếp theo
                broadcastMessage(new Message(Message.YOUR_TURN, currentPlayer, null));
                println("Gửi YOUR_TURN tới người chơi " + currentPlayer);
                break;
            case Message.MSG:
                println("Broadcasts a user message from "
                        + clientSocket.getRemoteSocketAddress());
                broadcastUserMessage(clientSocket, (String) message.getData());
                break;
            case Message.FIRST:
            	 int startingPlayer = (int) message.getData();
            	    currentPlayer = startingPlayer;
            	    broadcastMessage(new Message(Message.YOUR_TURN, currentPlayer, null));
            	    println("Starting player set to: " + currentPlayer);
            	    break;
            default:
                println("Wrong message type: " + message.getType());
                break;
        }
    }

    private synchronized void broadcastMessage(Message message) {
        if (numOfPlayers > 0) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] != null && clientOutputStreams[i] != null) {
                    try {
                        clientOutputStreams[i].writeObject(message);
                        clientOutputStreams[i].flush();
                    } catch (Exception ex) {
                        println("Error in broadcasting a message to the client at "
                                + clientSockets[i].getRemoteSocketAddress());
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized void addConnection(Socket clientSocket) {
        if (numOfPlayers < maxNumOfPlayers) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] == null) {
                    try {
                        ObjectOutputStream oostream = new ObjectOutputStream(
                                clientSocket.getOutputStream());

                        clientSockets[i] = clientSocket;
                        clientOutputStreams[i] = oostream;
                        clientNames[i] = null;
                        clientReadyStates[i] = false;
                        numOfPlayers++;
                        println("Người chơi số " + numOfPlayers + " đã tham gia");
                        Thread t = new Thread(new ClientHandler(clientSocket));
                        t.start();
                        oostream.writeObject(new Message(
                                Message.PLAYER_LIST, i, clientNames));
                        oostream.flush();
                    } catch (Exception ex) {
                        println("Lỗi kết nối");
                        ex.printStackTrace();
                    }
                    break;
                }
            }
        } else {
            println("Bàn chơi hiện đã đầy");
            Thread t = new Thread(new ClientHandler2(clientSocket));
            t.start();
        }

    }

    private synchronized void removeConnection(Socket clientSocket) {
        if (numOfPlayers > 0) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] == clientSocket) {
                    String name = clientNames[i];

                    clientSockets[i] = null;
                    clientOutputStreams[i] = null;
                    clientNames[i] = null;
                    clientReadyStates[i] = false;
                    numOfPlayers--;

                    println(name + " đã rời khỏi trò chơi");

                    String remoteAddress = clientSocket
                            .getRemoteSocketAddress().toString();

                    broadcastMessage(new Message(Message.QUIT,
                            i, remoteAddress));
                    break;
                }
            }
        }
    }

    private synchronized void addPlayer(Socket clientSocket, String name) {
        if (numOfPlayers > 0) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] == clientSocket) {
                    clientNames[i] = name;

                    println(name + " đã tham gia vào bàn chơi");
                    broadcastMessage(new Message(Message.JOIN,
                            i, name));
                    break;
                }
            }
        }
    }

    private synchronized void setReadyState(Socket clientSocket) {
        if (numOfPlayers > 0) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] == clientSocket) {
                    clientReadyStates[i] = true;
                    println(clientNames[i] + " đã sẵn sàng ");
                    broadcastMessage(new Message(Message.READY,
                            i, null));
                    break;
                }
            }
        }
        if (numOfPlayers == maxNumOfPlayers) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (!clientReadyStates[i]) {
                    return;
                }
            }

            for (int i = 0; i < maxNumOfPlayers; i++) {
                clientReadyStates[i] = false;
            }

            Deck deck = createDeck();
            deck.shuffle();
            println("Tất cả người chơi đã sẵn sàng, bắt đầu game");
            broadcastMessage(new Message(Message.START, -1,
                    deck));

            // Xác định người chơi bắt đầu: giả sử người chơi 0
            //broadcastMessage(new Message(Message.YOUR_TURN, currentPlayer, null));
            //println("Gửi YOUR_TURN tới người chơi " + currentPlayer);
        }
    }

    private int getNextPlayer(int currentPlayer) {
        return (currentPlayer + 1) % maxNumOfPlayers;
    }

    private synchronized void startGame(Socket clientSocket) {
        Deck deck = createDeck();
        deck.shuffle();
        println("Tất cả người chơi đã sẵn sàng, bắt đầu game");
        broadcastMessage(new Message(Message.START, -1,
                deck));
    }

    public Deck createDeck() {
        return new Deck();
    }

    private synchronized void broadcastUserMessage(Socket clientSocket,
                                                   String msg) {
        if (numOfPlayers > 0) {
            for (int i = 0; i < maxNumOfPlayers; i++) {
                if (clientSockets[i] == clientSocket) {
                    String longMsg = clientNames[i] + " ("
                            + clientSocket.getRemoteSocketAddress() + "): "
                            + msg;
                    broadcastMessage(new Message(Message.MSG,
                            i, longMsg));
                    break;
                }
            }
        }
    }

    private void println(String msg) {
        textArea.append(msg + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket; // socket connection to the client
        private ObjectInputStream oistream; // ObjectInputStream of the client

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                oistream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception ex) {
                println("Error in creating an ObjectInputStream for the client at "
                        + clientSocket.getRemoteSocketAddress());
                ex.printStackTrace();
            }
        }

        public void run() {
            Message message;
            try {
                while ((message = (Message) oistream.readObject()) != null) {
                    println("Message received from "
                            + clientSocket.getRemoteSocketAddress());
                    parseMessage(clientSocket, message);
                }
            } catch (Exception ex) {
                println("Error in receiving messages from the client at "
                        + clientSocket.getRemoteSocketAddress());
                ex.printStackTrace();
                removeConnection(clientSocket);
            }
        }
    }

    private class ClientHandler2 implements Runnable {
        private Socket clientSocket;

        public ClientHandler2(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                ObjectOutputStream oostream = new ObjectOutputStream(
                        clientSocket.getOutputStream());
                oostream.writeObject(new Message(Message.FULL,
                        -1, null));
                oostream.flush();
            } catch (Exception ex) {
                println("Error in sending a FULL message to the client at "
                        + clientSocket.getRemoteSocketAddress());
                ex.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                println("Error in sleeping before closing the client socket at "
                        + clientSocket.getRemoteSocketAddress());
                ex.printStackTrace();
            }
            try {
                clientSocket.close();
            } catch (Exception ex) {
                println("Error in closing the client socket at "
                        + clientSocket.getRemoteSocketAddress());
                ex.printStackTrace();
            }
        }
    }

    private class ClearMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textArea.setText("");
        }
    }

    private class QuitMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            System.exit(0);
        }
    }
}