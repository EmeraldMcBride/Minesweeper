
import java.util.ArrayList;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Minesweeper extends Application {
	int width = 16;
	int height = 16;
	int numMines = 40;
	public int minesLeftnum = 40;
	public boolean bombset;
	int time;
	int[][] board = new int[height][width];
	ArrayList<Integer> r1 = new ArrayList<Integer>();
	Button face;
	Mines mines[][];
	int state; // active game = 1, win = 2, loss = 3;
	public boolean firstClickDone;
	HBox minesRemain = new HBox ();
	HBox timeElapsed = new HBox ();
	Label timer = new Label ("000");
	long startsec;
	
	public static void main(String[] args) {
		launch(args);
	}

	public void setBoard(int minesNumber, int[][] a, Mines first) {
		//minesLeftnum = minesNumber;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (first == mines[i][j]) {
					int isub = i - 1; // boundary check
					int iadd = i + 1;
					int jsub = j - 1;
					int jadd = j + 1;
					if (isub < 0)
						isub = 0;
					if (iadd > height - 1)
						iadd = height - 1;
					if (jsub < 0)
						jsub = 0;
					if (jadd > width - 1)
						jadd = width - 1;
					for (int k = isub; k <= iadd; k++) {
						for (int m = jsub; m <= jadd; m++) {
							int t = Integer.valueOf(String.valueOf(k) + String.valueOf(m));
							r1.add(t);
						}
					}
				}
			}
		}
		for (int i = 0; i < minesNumber; i++) {
			bombset = false;
			while (!bombset) {
				int rand1 = (int) (Math.random() * width);
				int rand2 = (int) (Math.random() * width);
				int k = Integer.valueOf(String.valueOf(rand1) + String.valueOf(rand2));

				if (!r1.contains(k)) {
					a[rand1][rand2] = 9;
					if (rand1 == 0 && rand2 == 0) {
						a[rand1][rand2 + 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 + 1]++;
					} else if (rand1 == 0 && rand2 == width - 1) {
						a[rand1][rand2 - 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 - 1]++;
					} else if (rand1 == width - 1 && rand2 == 0) {
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 + 1]++;
						a[rand1][rand2 + 1]++;
					} else if (rand1 == width - 1 && rand2 == width - 1) {
						a[rand1][rand2 - 1]++;
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 - 1]++;
					} else if (rand1 == 0) {
						a[rand1][rand2 + 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 + 1]++;
						a[rand1][rand2 - 1]++;
						a[rand1 + 1][rand2 - 1]++;
					} else if (rand2 == 0) {
						a[rand1][rand2 + 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 + 1]++;
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 + 1]++;
					} else if (rand1 == width - 1) {
						a[rand1][rand2 - 1]++;
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 - 1]++;
						a[rand1][rand2 + 1]++;
						a[rand1 - 1][rand2 + 1]++;
					} else if (rand2 == width - 1) {
						a[rand1][rand2 - 1]++;
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 - 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 - 1]++;
					} else {
						a[rand1][rand2 - 1]++;
						a[rand1][rand2 + 1]++;
						a[rand1 - 1][rand2]++;
						a[rand1 - 1][rand2 - 1]++;
						a[rand1 + 1][rand2]++;
						a[rand1 + 1][rand2 - 1]++;
						a[rand1 + 1][rand2 + 1]++;
						a[rand1 - 1][rand2 + 1]++;
					}

					r1.add(k);
					bombset = true;
				}
				for (int q = 0; q < height; q++) {
					for (int p = 0; p < width; p++) {
						if (board[q][p] > 9)
							board[q][p] = 9;
					}
				}
			}
		}
	}

	public void showMines(int[][] a) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[i][j] == 9) {
					mines[i][j].setGraphic(new ImageView("file:res/mine-grey.png"));
				}
			}
		}
	}

	public void newGame() {
		face.setGraphic(new ImageView(new Image("file:res/face-smile.png")));
		state = 1;
		r1.clear();
		minesLeftnum = numMines;
		timer.setText("000");
		startsec = System.currentTimeMillis();
		newBoard();
		minesRemain.getChildren().clear();
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/4.png")));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		// System.out.println(numMines);
		//for (int i = 0; i < height; i++) {
	//	for (int j = 0; j < width; j++) {
		//System.out.print(board[i][j]);
	//}
		// System.out.println("");}
		// setBoard(numMines, board);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//mines[i][j].setValue(board[i][j]);
				mines[i][j].bState = 0;
				mines[i][j].recursionDone = false;
				 //System.out.print(board[i][j]);
			}
			//System.out.println("");
		}

	}

	public void surrounding8(Mines mine, Mines[][] mines) {
		int count = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (mines[i][j] == mine) {
					int isub = i - 1; // boundary check
					int iadd = i + 1;
					int jsub = j - 1;
					int jadd = j + 1;
					if (isub < 0)
						isub = 0;
					if (iadd > width - 1)
						iadd = width - 1;
					if (jsub < 0)
						jsub = 0;
					if (jadd > width - 1)
						jadd = width - 1;
					for (int k = isub; k <= iadd; k++) {
						for (int m = jsub; m <= jadd; m++) {
							if (mines[k][m].bState == 2)
								count++;
						}
					}
				}
			}
		}
		if (count >= mine.value) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (mines[i][j] == mine) {
						int isub = i - 1; // boundary check
						int iadd = i + 1;
						int jsub = j - 1;
						int jadd = j + 1;
						if (isub < 0)
							isub = 0;
						if (iadd > width - 1)
							iadd = width - 1;
						if (jsub < 0)
							jsub = 0;
						if (jadd > width - 1)
							jadd = width - 1;
						for (int k = isub; k <= iadd; k++) {
							for (int m = jsub; m <= jadd; m++) {
								mines[k][m].bState = 1;
								if (mines[k][m].value != 9)
									mines[k][m].setGraphic(
											new ImageView(new Image("file:res/" + mines[k][m].value + ".png")));
								else if (mines[k][m].flagged == 1) {
									mines[k][m].setGraphic(new ImageView(new Image("file:res/flag.png")));
								}else
									mines[k][m].setGraphic(new ImageView(new Image("file:res/mine-red.png")));
							}
						}
					}
				}
			}
		}
	}

	
	public void minesLeftUpdate() {
		int length = String.valueOf(minesLeftnum).length();
		minesRemain.getChildren().clear();
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		//System.out.println(minesLeftnum/10);
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/" + minesLeftnum/10 + ".png")));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/" + minesLeftnum%10 + ".png")));
			
		
	}
	
	public void timerUpdate() {
		time = Integer.valueOf(timer.getText()).intValue();
		//System.out.println(time);
		timeElapsed.getChildren().clear();
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/" + time/100 +".png")));
		//System.out.println(minesLeftnum/10);
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/" + time/10 + ".png")));
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/" + time%10 + ".png")));
			
		
	}
	
	public void zeroRecursion(Mines mine) {
		if (mine.recursionDone == false) {
			mine.recursionDone = true;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (mines[i][j] == mine) {
						int isub = i - 1; // boundary check
						int iadd = i + 1;
						int jsub = j - 1;
						int jadd = j + 1;
						if (isub < 0)
							isub = 0;
						if (iadd > width - 1)
							iadd = width - 1;
						if (jsub < 0)
							jsub = 0;
						if (jadd > width - 1)
							jadd = width - 1;
						for (int k = isub; k <= iadd; k++) {
							for (int m = jsub; m <= jadd; m++) {
								mines[k][m].bState = 1;
								mines[k][m].setGraphic(new ImageView(new Image("file:res/" + mines[k][m].value + ".png")));
								if (mines[k][m].value == 0)
									zeroRecursion(mines[k][m]);
							}
						}
					}
				}
			}
		}
	}

	public void newBoard() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = 0;
				mines[i][j].setGraphic(new ImageView("file:res/cover.png"));
				mines[i][j].setValue(0);
				firstClickDone = false;

			}
		}
	}

	public void checkWin() {
		int nonMinesUnOpened = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (mines[i][j].bState == 0 && mines[i][j].value != 9)
					nonMinesUnOpened++;
			}
		}
		if (nonMinesUnOpened == 0) {
			face.setGraphic(new ImageView(new Image("file:res/face-win.png")));
		}
	}

	public void start(Stage theStage) {
		state = 1;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = 0;

			}
		}
		mines = new Mines[height][width];
		firstClickDone = false;
		// setBoard(numMines, board);
		BorderPane outside = new BorderPane();
		GridPane inside = new GridPane();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				mines[i][j] = new Mines("", i, j, board[i][j]);
				Mines button = mines[i][j];

				button.setOnMouseClicked(event -> {
					MouseButton mouse = event.getButton();

					if (state == 1) {

						if (mouse == MouseButton.PRIMARY && button.flagged == 0) {
							if (!firstClickDone) {
								firstClickDone = true;
								setBoard(numMines, board, button);
								for (int h = 0; h < height; h++) {
									for (int o = 0; o < width; o++) {
										mines[h][o].setValue(board[h][o]);
									
											//System.out.print(board[h][o]);
										}
											 //System.out.println("");}
									
								}
							}
							if (button.bState == 1) {
								surrounding8(button, mines);

							}
							if (button.value != 9) {								
								if (button.value == 0)
									zeroRecursion(button);
								button.bState = 1;
								button.setGraphic(new ImageView(new Image("file:res/" + button.value + ".png")));

								checkWin();
							} else {
								showMines(board);
								button.setGraphic(new ImageView(new Image("file:res/mine-red.png")));
								face.setGraphic(new ImageView(new Image("file:res/face-dead.png")));
								state = 3;
							}

						} else if (mouse == MouseButton.SECONDARY) {

							if (button.flagged == 0 && minesLeftnum > 0) {
								button.flagged = 1;
								button.bState = 2;
								button.setGraphic(new ImageView(new Image("file:res/flag.png")));
								minesLeftnum--;
								//minesLeft.setText("" + minesLeftnum + "");
								minesLeftUpdate();
							} else if (button.flagged == 1 && minesLeftnum < 10) {
								button.flagged = 0;
								button.setGraphic(new ImageView(new Image("file:res/cover.png")));
								minesLeftnum++;
								//minesLeft.setText("" + minesLeftnum + "");
								minesLeftUpdate();
							}

						}
					}
				});
				inside.add(button, i, j);
			}
		}
		
		HBox top = new HBox(180);
		top.setPadding(new Insets (8,8,8,8) );
		top.setStyle("-fx-background-color: lightgray");
		top.setAlignment(Pos.CENTER);
		face = new Button();
		face.setPadding(Insets.EMPTY);
		face.setGraphic(new ImageView(new Image("file:res/face-smile.png")));
		face.setOnMouseClicked(event -> {
			newGame();
		});
		//minesLeft.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		//time.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

		startsec = System.currentTimeMillis();
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame( Duration.millis(1000),e -> {
			
		timer.setText(String.format("%03d",((System.currentTimeMillis() - startsec)/1000)));
		timerUpdate();}));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play(); 
		
		
		minesRemain.setPadding(new Insets(15,8,8,8));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/4.png")));
		minesRemain.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));

		
		timeElapsed.setPadding(new Insets(15,8,8,8));
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		timeElapsed.getChildren().add(new ImageView(new Image("file:res/digits/0.png")));
		CustomPane bottom = new CustomPane();
		
		MenuBar menuBar = new MenuBar();		//difficulty change
		Menu file = new Menu("Difficulty");
		menuBar.getMenus().add(file);
		bottom.getChildren().add(menuBar);
		MenuItem beginner = new MenuItem("Beginner");
		MenuItem intermediate = new MenuItem("Intermediate");		
		MenuItem expert = new MenuItem("Expert");
		file.getItems().addAll(beginner,intermediate,expert);
		
		/*beginner.setOnAction(e -> {
			height = 8;
			width = 8;
			numMines = 10;
			minesLeftnum = 10;
		});
		intermediate.setOnAction(e -> {
			height = 16;
			width = 16;
			numMines = 40;
			minesLeftnum = 40;
		});  */
		
		top.getChildren().add(minesRemain);
		top.getChildren().add(face);
		top.getChildren().add(timeElapsed);
		
		
		outside.setTop(top);
		outside.setCenter(inside);
		outside.setBottom(bottom);
		outside.setLeft(new CustomPane());
		outside.setRight(new CustomPane());
	
		
		Scene scene = new Scene(outside);
		theStage.setScene(scene);
		theStage.show();
	}

}

class Mines extends Button {
	public int row;
	public int col;
	public int value;
	public int bState; // 0 means covered, 1 means opened, 2 means flagged
	public int flagged; // 0 for unflagged, 1 for flagged
	public boolean recursionDone = false;

	public Mines(String name, int row, int col, int value) {
		super(name);
		this.row = row;
		this.col = col;
		this.value = value;
		flagged = 0;
		bState = 0;
		setStyle("-fx-focus-color: transparent;-fx-faint-focus-color: transparent;");
		setPadding(Insets.EMPTY);
		setGraphic(new ImageView(new Image("file:res/cover.png")));
		prefHeight(32);
		prefWidth(32);
	}

	public void setValue(int x) {
		value = x;
	}
}

class CustomPane extends StackPane{
	public CustomPane(){

		setStyle("-fx-background-color: lightgray");
		setPadding(new Insets(8,8,8,8));
	}
}
