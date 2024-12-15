package message;

import java.io.Serializable;

public class GameMessage implements Serializable {
	private static final long serialVersionUID = -9138385504565085818L;
	private int type;
	private int playerID;
	private Object data;
	
	public GameMessage(int type, int playerID, Object data) {
		this.type = type;
		this.playerID = playerID;
		this.data = data;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getPlayerID() {
		return this.playerID;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}
