package com.codingame.game.custom.sudoku;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;

public class MyCallback implements Callback {

	private int[][] sudokuMatrix;
	private int N = 9;

	public MyCallback(int[][] sm) {
		this.sudokuMatrix = sm;
	}

	@Override
	public void callback(Output o) {
		//Analizziamo l'answer set che in quest caso � unico e che rappresenta la soluzione
		//del Sudoku e aggiorniamo la matrice
		AnswerSets answers = (AnswerSets) o;
		for(AnswerSet a:answers.getAnswersets()){
			try {
				for(Object obj: a.getAtoms()){
					//Scartiamo tutto ci� che non � un oggetto della classe Cell
					if(!(obj instanceof Cell)) continue;
					//Convertiamo in un oggetto della classe Cell e impostiamo il valore di ogni cella 
					//nella matrice rappresentante la griglia del Sudoku
					Cell cell = (Cell) obj;					
					sudokuMatrix[cell.getRow()][cell.getColumn()] = cell.getValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		//Visualizziamo la griglia cos� ottenuta
		displayMatrix();
	}

	private void displayMatrix() {
		for(int i=0;i<N;i++){
			for(int j=0;j<N && i%3==0;j++){
				System.out.print("----");
				if(j==N-1)System.out.println();
			}
			for(int j=0;j<N;j++){
				System.out.print((j%3==0)?"||":"  ");
				System.out.print(sudokuMatrix[i][j]+" ");
				if(j==N-1)System.out.print("||");
			}
			System.out.println();
		}
		for(int j=0;j<N;j++){
			System.out.print("----");
		}
		System.out.println();
	}

}
