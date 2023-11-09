package omok;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
	private int size;
	private int turn;
	private String[][] map;

	Board(int size) {
		this.size = size;
		this.turn = 0;
		map = new String[size][size];
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				map[row][col] = ".";
			}
		}
	}

	// 되고
	public boolean canPlaceStone(Position pos) {
		if (map[pos.getRow()][pos.getCol()].equals(".")) {
			return true;
		}
		return false;
	}

	// 안되고
	public void placeStone(Position pos, Player player) {
		map[pos.getRow()][pos.getCol()] = player.getStone();
		turn = (turn + 1) % 2;
	}

	public String getStone(Position pos) {
		return map[pos.getRow()][pos.getCol()];
	}

}