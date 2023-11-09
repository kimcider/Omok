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
	/*
	 * Ŭ���̾�Ʈ�� ������ ������ ServerEndPoint�� ���� �����Ǵ� �� ���δ�. board�� turn�� �� Ŭ���̾�Ʈ���� �������� �ʱ�
	 * ������ static���� �����Ͽ���.
	 */
	private static Board board;
	private static int turn;

	public ChatServer() {
		super();
		player = new Player[2];
		board = new Board(19);
		turn = 0;
	}

	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	// ������ �������� �ʰ� �����ϸ�, �ߺ� ������ �Ұ����ϴ�.

	@OnOpen // Ŭ���̾�Ʈ ���� �� ����
	public void onOpen(Session session) {
		int index = Integer.valueOf(session.getId());
		int sessionID = Integer.valueOf(session.getId());
		if (sessionID >= 2)
			return;
		clients.add(session); // ���� �߰�

		player[index] = new Player("", "0".equals(session.getId()) ? "O" : "X");
	}

	@OnMessage // �޽����� ������ ����
	public void onMessage(String message, Session session) throws IOException {
		int sessionID = Integer.valueOf(session.getId());
		if (sessionID >= 2)
			return;
		if (message.startsWith("id ")) {
			String[] tempStr = message.split(" ");
			String id = tempStr[1];

			int userCode = Integer.valueOf(session.getId());
			player[userCode].setId(id);
			return;
		}
		String[] tempPos = message.split(" ");
		int row = Integer.valueOf(tempPos[0]);
		int col = Integer.valueOf(tempPos[1]);
		int userCode = Integer.valueOf(session.getId());

		synchronized (clients) {
			Position pos = new Position(row, col);

			if (board.canPlaceStone(pos) && checkTurn(userCode)) {
				board.placeStone(pos, player[userCode]);
				turn = (turn + 1) % 2;
				// ���� �� ��� ó��
				for (Session client : clients) { // ��� Ŭ���̾�Ʈ���� �޽��� ����
					client.getBasicRemote().sendText(userCode + "|" + row + "|" + col);
				}

				// ���� �״��� �̰��� ��� ó��
				if (checkEnd(pos, player[userCode])) {
					for (Session client : clients) { // ���� �޽��� ����
						client.getBasicRemote().sendText(player[userCode].getId() + "���� �¸��Ͽ����ϴ�.");
					}
					clients.clear();
					return;
				}
			} else {
				// �ڱⰡ �� �� ���� ������ ���
			}

		}
	}

	@OnClose // Ŭ���̾�Ʈ���� ������ ����� ����
	public void onClose(Session session) {
	}

	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	public boolean checkTurn(int userCode) {
		if (userCode == turn) {
			return true;
		} else {
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