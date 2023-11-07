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
    
    public void print() {
    	int rowCounter = 0;
    	char colCounter = 'A';
    	
        for (int row = 0; row < size; row++) {
        	System.out.printf("%2d", rowCounter++);
            for (int col = 0; col < size; col++) {
                System.out.print(" " + map[row][col]);
            }
            System.out.println();
        }
        System.out.printf("  ");
        for(int i = 0; i < size; i++) {
        	System.out.printf("%2c", colCounter++);
        }
        System.out.println();
    }
    
    public boolean checkTurn(int turn) {
    	if(turn== this.turn) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    //되고
	public boolean canPlaceStone(Position pos) {
		if (pos.getRow() < 0 || pos.getRow() >= size || pos.getCol() < 0 || pos.getCol() >= size) {
			return false;
		}
		if (map[pos.getRow()][pos.getCol()].equals(".")) {
			return true;
		}
		return false;
	}
	
	//안되고
	public void placeStone(Position pos, Player player) {
		System.out.print("placeStone: ");
		System.out.println(pos);
		System.out.println("player: " + player);
		map[pos.getRow()][pos.getCol()] = player.getStone();
		turn = (turn + 1) % 2;
	}
	
	public String getStone(Position pos) {
		return map[pos.getRow()][pos.getCol()];
	}
	
	
}