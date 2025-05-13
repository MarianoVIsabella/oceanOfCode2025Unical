package com.codingame.game.custom.sudoku;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class MainClass {

	public MainClass() {}
	
	private static int[][] sudokuMatrix = { 
			{ 1, 0, 0, 0, 0, 7, 0, 9, 0 }, 
			{ 0, 3, 0, 0, 2, 0, 0, 0, 8 },
			{ 0, 0, 9, 6, 0, 0, 5, 0, 0 }, 
			{ 0, 0, 5, 3, 0, 0, 9, 0, 0 }, 
			{ 0, 1, 0, 0, 8, 0, 0, 0, 2 },
			{ 6, 0, 0, 0, 0, 4, 0, 0, 0 }, 
			{ 3, 0, 0, 0, 0, 0, 0, 1, 0 }, 
			{ 0, 4, 1, 0, 0, 0, 0, 0, 7 },
			{ 0, 0, 7, 0, 0, 0, 3, 0, 0 }};
	
	private static int N=9;
	
	private static String encodingResource="encodings/sudoku";
	
	private static Handler handler;
	
	public static void main(String[] args) {
		//Visualizziamo la griglia iniziale del Sudoku
		displayMatrix();
		
		//Creazione dell'oggetto handler che si occuperà di gestire l'invocazione
		//del sistema ASP da utilizzare
		
		//Se si esegue la demo su Windows 64bit scommentare la seguente istruzione:
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

		//Se si esegue la demo su Linux 64bit scommentare la seguente istruzione:
		//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		

		//In alternativa, aggiungere nella cartella lib l'eseguibile di DLV2 
		//appropriato in base al proprio sistema e sostituire a "nome_exe_dlv2" 
		//il nome dell'eseguibile di DLV2 nella seguente istruzione e scommentarla
		//handler = new DesktopHandler(new DLV2DesktopService("lib/nome_exe_dlv2"));
		
		//Specifichiamo i fatti in input, in questo caso tramite oggetti della 
		//classe Cell che viene prima registrata all'ASPMapper
		try {
			ASPMapper.getInstance().registerClass(Cell.class);
		} catch (ObjectNotValidException | IllegalAnnotationException e1) {

		}
		InputProgram facts= new ASPInputProgram();
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(sudokuMatrix[i][j]!=0){
					try {
						facts.addObjectInput(new Cell(i, j, sudokuMatrix[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			}			
		}
		
		//Aggiungiamo all'handler i fatti 
		handler.addProgram(facts);
		
		//Specifichiamo il programma logico tramite file
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath(encodingResource);
		
		//Aggiungiamo all'handler il programma logico
		handler.addProgram(encoding);
		
		//L'handler invoca DLV2 in modo SINCRONO dando come input il programma logico e i fatti
		Output o =  handler.startSync();

		//Analizziamo l'answer set che in quest caso � unico e che rappresenta la soluzione
		//del Sudoku e aggiorniamo la matrice
		AnswerSets answersets = (AnswerSets) o;
		for(AnswerSet a:answersets.getAnswersets()){
			try {
				for(Object obj:a.getAtoms()){
					//Scartiamo tutto ci� che non � un oggetto della classe Cell
					if(!(obj instanceof Cell)) continue;
					//Convertiamo in un oggetto della classe Cell e impostiamo il valore di ogni cella 
					//nella matrice rappresentante la griglia del Sudoku
					Cell cell= (Cell) obj;					
					sudokuMatrix[cell.getRow()][cell.getColumn()] = cell.getValue();
				}
			} catch (Exception e) {

			} 
			
		}
		//Visualizziamo la griglia cos� ottenuta
		displayMatrix();
		
		//In alternativa l'handler pu� invocare DLV2 in modo ASINCRONO.
		//Scommentare la seguente linea e commentare le linee 89-110
		//handler.startAsync(new MyCallback(sudokuMatrix));
	}
	
	private static void displayMatrix() {
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
