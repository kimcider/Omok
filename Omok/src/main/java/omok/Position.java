package omok;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Position {
	private int row;
	private int col;
	Position(String row, String col){
		this.row = Integer.valueOf(row);
		this.col = Integer.valueOf(col);
	}
	Position(int row, int col){
		this.row = row;
		this.col = col;
	}
}