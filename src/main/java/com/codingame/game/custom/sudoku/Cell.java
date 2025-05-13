package com.codingame.game.custom.sudoku;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cell")
public class Cell {


	@Param(0)
	private int row;
	@Param(1)
	private int column;
	@Param(2)
	private int value;
	
	public Cell(int r,int c,int v){
		this.row=r;
		this.column=c;
		this.value=v;
	}
	
	public Cell() {
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	

}
