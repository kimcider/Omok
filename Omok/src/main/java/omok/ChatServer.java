package omok;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ChatingServer")
public class ChatServer {
	private Player[] player;
	private static Board board;
	private static int turn;
	
	public ChatServer() {
		super();
		player = new Player[2];
		board = new Board(19);
		turn = 0;
		System.out.println("�̰Ŵµǳ�");
	}
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	// ������ �������� �ʰ� �����ϸ�, �ߺ� ������ �Ұ����ϴ�.
	
	
	@OnOpen // Ŭ���̾�Ʈ ���� �� ����
	public void onOpen(Session session) {
		int index = Integer.valueOf(session.getId());
		System.out.println("sessionID: " + session.getId());
		if("2".equals(session.getId())) {
			return;
		}
		clients.add(session); // ���� �߰�
		System.out.println("������ ����: " + session.getId());
		
		player[index] = new Player("", "0".equals(session.getId()) ? "O" : "X");
		System.out.println("index: " + index);
		System.out.println("player[index]: " + player[index]);
		System.out.println(player[index].getStone());
	}

	@OnMessage // �޽����� ������ ����
	public void onMessage(String message, Session session) throws IOException {
		if(message.startsWith("id ")) {
			System.out.println(message);
			String[] tempStr = message.split(" ");
			String id = tempStr[1];
			System.out.println("id: " + id);
			
			int userCode = Integer.valueOf(session.getId());
			player[userCode].setId(id);
			return;
		}
		System.out.println("�޽��� ����: " + session.getId() + ": " + message);
		String[] tempPos = message.split(" ");
		int row = Integer.valueOf(tempPos[0]);
		int col = Integer.valueOf(tempPos[1]);
		int userCode = Integer.valueOf(session.getId()); 
		
		synchronized (clients) {
			Position pos = new Position(row, col);
			System.out.println("turn: " + userCode + " row: " + pos.getRow() + " col: " + pos.getCol());
			
			if(board.canPlaceStone(pos) && checkTurn(userCode)) {
				System.out.println("������� ��");
				board.placeStone(pos, player[userCode]);
				System.out.println("turn ���� ��: " + turn);
				turn = (turn + 1) % 2;
				System.out.println("turn ���� ��: " + turn);
//				���� �� ��� ó��
				for (Session client : clients) { // ��� Ŭ���̾�Ʈ���� �޽��� ����
					client.getBasicRemote().sendText(userCode+"|"+row+"|"+col);
				}
				
				if(checkEnd(pos, player[userCode])) {
					System.exit(0);
//					���� �״��� �̰��� ��� ó��
					return;
				}
			}else {
				// �ڱⰡ �� �� ���� ������ ���
			}

		}
	}

	@OnClose // Ŭ���̾�Ʈ���� ������ ����� ����
	public void onClose(Session session) {
		clients.remove(session);
		System.out.println("������ ����: " + session.getId());
	}

	@OnError
	public void onError(Throwable e) {
		System.out.println("���� �߻�");
		e.printStackTrace();
	}
	
    public boolean checkTurn(int userCode) {
    	System.out.println("�޾ƿ� turn: "+ userCode);
    	System.out.println("BOARD�� turn: "+ turn);
    	if(userCode== turn) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
	private boolean checkEnd(Position now, Player player) {
		int minRow, maxRow, minCol, maxCol;

		if (now.getRow() - 4 >= 0) {
			minRow = now.getRow() - 4;
		} else {
			minRow = 0;
		}

		if (now.getRow() + 4 < board.getSize()) {
			maxRow = now.getRow() + 4;
		} else {
			maxRow = board.getSize() - 1;
		}

		if (now.getCol() - 4 >= 0) {
			minCol = now.getCol() - 4;
		} else {
			minCol = 0;
		}

		if (now.getCol() + 4 < board.getSize()) {
			maxCol = now.getCol() + 4;
		} else {
			maxCol = board.getSize() - 1;
		}

		// ����
		int counter = 0;
		for (int i = minCol; i <= maxCol; i++) {
			Position pos = new Position(now.getRow(), i);
			if (board.getStone(pos).equals(player.getStone())) {
				counter++;
			} else {
				counter = 0;
			}
			if (counter == 5)
				return true;
		}
		// ����
		counter = 0;
		for (int i = minRow; i <= maxRow; i++) {
			Position pos = new Position(i, now.getCol());
			if (board.getStone(pos).equals(player.getStone())) {
				counter++;
			} else {
				counter = 0;
			}
			if (counter == 5)
				return true;
		}
		// �������밢
		counter = 0;
		for (int i = 1; i < 5; i++) {
			if (now.getRow() - i >= minRow && now.getCol() - i >= minCol) {
				Position pos = new Position(now.getRow() - i, now.getCol() - i);
				if (board.getStone(pos).equals(player.getStone())) {
					counter++;
				} else {
					break;
				}
			}

		}
		if (counter == 4)
			return true;
		else {
			for (int i = 1; i < 5; i++) {
				if (now.getRow() + i <= maxRow && now.getCol() + i <= maxCol) {
					Position pos = new Position(now.getRow() + i, now.getCol() + i);
					if (board.getStone(pos).equals(player.getStone())) {
						counter++;
					} else {
						break;
					}
				}
			}
		}
		if (counter == 4)
			return true;

		// ���ʾƷ��밢
		counter = 0;
		for (int i = 1; i < 5; i++) {
			if (now.getRow() - i >= minRow && now.getCol() + i <= maxCol) {
				Position pos = new Position(now.getRow() - i, now.getCol() + i);
				if (board.getStone(pos).equals(player.getStone())) {
					counter++;
				} else {
					break;
				}
			}
		}
		if (counter == 4)
			return true;
		for (int i = 1; i < 5; i++) {
			if (now.getRow() + i <= maxRow && now.getCol() - i >= minRow) {
				Position pos = new Position(now.getRow() + i, now.getCol() - i);
				if (board.getStone(pos).equals(player.getStone())) {
					counter++;
				} else {
					break;
				}
			}
		}
		if (counter == 4)
			return true;
		return false;
	}
}