package org.example.programminglab9;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NoughtsAndCrossesApp extends Application {

    private Button[][] buttons; // 버튼 배열
    private GameBoard gameboard; // 게임 보드
    private Label score; // 점수
    private Label turn; // 턴
    private int xwin = 0; // X 승리 횟수
    private int owin = 0; // O 승리 횟수
    private int draw = 0; // 무승부 횟수
    private StringProperty Xplayer = new SimpleStringProperty("Player X"); // X 플레이어 이름
    private StringProperty Oplayer = new SimpleStringProperty("Player O"); // O 플레이어 이름
    private Stage primaryStage; // 메인 스테이지

    public static void main(String[] args) { // 메인
        launch(args);
    }

    public void start(Stage stage) { // 스테이지 설정, 로딩화면
        this.primaryStage = stage;
        Loading(); // 로딩
    }

    // 로딩화면
    private void Loading() { // 로딩화면 코드
        // 점수 초기화
        xwin = 0;
        owin = 0;
        draw = 0;
        VBox loadingLayout = new VBox();
        // 라벨
        Label Xnamelabel = new Label("Player X Name"); // X 플레이어 이름   
        TextField Xname = new TextField(); // X 플레이어 이름 입력 필드
        Label Onamelabel = new Label("O Player Name"); // O 플레이어 이름
        TextField Oname = new TextField(); // O 플레이어 이름 입력 필드

        Button startbutton = new Button("Start Game"); // 게임 시작 버튼
        startbutton.setDisable(true); // 이름 입력 안하면 못누르게

        // 이름입력버튼 활성화체크
        Xname.textProperty().addListener((obs, oldValue, newValue) -> {
            startbutton.setDisable(newValue.trim().isEmpty() || Oname.getText().trim().isEmpty());
        });
        Oname.textProperty().addListener((obs, oldValue, newValue) -> {
            startbutton.setDisable(newValue.trim().isEmpty() || Xname.getText().trim().isEmpty());
        });

        // 시작 버튼 누르면 게임화면
        startbutton.setOnAction(e -> {
            Xplayer.set(Xname.getText());
            Oplayer.set(Oname.getText());
            Gamescreen(); // 게임화면 표시
        });
        
        loadingLayout.getChildren().addAll(
            Xnamelabel, Xname, Onamelabel, Oname, startbutton
        );
        Scene loadingScene = new Scene(loadingLayout, 400, 400); // 로딩화면 크기
        // 로딩화면 표시
        primaryStage.setScene(loadingScene);
        primaryStage.show();
    }

    //게임화면
    private void Gamescreen() {
        gameboard = new GameBoard(3);
        buttons = new Button[3][3];
        GridPane grid = new GridPane(); // 게임임화면 그리드
        // 간격 줘서 다 안붙어있게
        grid.setHgap(5);
        grid.setVgap(5);
        // 점수판 라벨
        score = new Label();
        score.textProperty().bind(
            new SimpleStringProperty("")
                .concat(Xplayer)
                .concat(" Wins: ")
                .concat(xwin)
                .concat(" / ")
                .concat(Oplayer)
                .concat(" Wins: ")
                .concat(owin)
                .concat(" / Draws: ")
                .concat(draw)
        );
        turn = new Label();
        boolean isXTurn = gameboard.currentplayer().equals("X"); // X 플레이어 턴 체크
        String currentPlayerName;
        String currentPlayerMark;
        if (isXTurn) {
            currentPlayerName = Xplayer.get();
            currentPlayerMark = " (X)";
        } else {
            currentPlayerName = Oplayer.get();
            currentPlayerMark = " (O)";
        }
        turn.setText("Turn: " + currentPlayerName + currentPlayerMark); // 표시
        Button resetbutton = new Button("Reset"); // 리셋버튼
        Button scoreboardbutton = new Button("Scoreboard"); // 점수판
        Button homeButton = new Button("Home"); // Home 버튼

        // Home 버튼 로딩 화면으로 이동
        homeButton.setOnAction(e -> Loading());

        // 리셋
        resetbutton.setOnAction(event -> reset());
        // 점수판 버튼
        scoreboardbutton.setOnAction(event -> showScoreboard());

        // vbox로 수직 배치 
        VBox root = new VBox(turn, score, resetbutton, scoreboardbutton, homeButton, grid);

        // 버튼 그리드 생성
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) { 
                Button button = new Button();
                button.setPrefSize(100, 100);
                buttons[row][col] = button;
                grid.add(button, col, row);

                final int lastrow = row;
                final int lastcol = col;
                button.setOnAction(e -> Click(button, lastrow, lastcol)); // 버튼 클릭 이벤트 설정
            }
        }
        // 게임화면 표시
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 버튼클릭 처리
    private void Click(Button button, int row, int col) {
        // 유효 클릭 체크
        if (gameboard.doClick(row, col)) {
            // 버튼 OX 업데이트
            button.setText(gameboard.currentplayer());
            // 승자 확인
            String winner = gameboard.checkwin();
            // 승자 있으면
            if (!winner.isEmpty()) {
                Label status = new Label();
                String winnerName;
                // X 승자 있으면
                if (winner.equals("X")) {
                    xwin++;
                    winnerName = Xplayer.get();
                    status.setText(winnerName + " wins");
                } else {
                    // O 승자 있으면
                    owin++;
                    winnerName = Oplayer.get();
                    status.setText(winnerName + " wins");
                }
                update();
                disablebutton();
                String message = winnerName + " wins";
                GameOver(message);
                // 무승부면
            } else if (gameboard.checkboardfull()) {
                draw++;
                update();
                disablebutton();
                GameOver("draw");
            } else {
                // 플레이어 변경
                gameboard.switchplayer();
                boolean isXTurn = gameboard.currentplayer().equals("X");
                String currentPlayerName;
                String currentPlayerMark;
                // X 플레이어 턴이면
                if (isXTurn) {
                    currentPlayerName = Xplayer.get();
                    currentPlayerMark = " (X)";
                // O 플레이어 턴이면
                } else {
                    currentPlayerName = Oplayer.get();
                    currentPlayerMark = " (O)";
                }
                // 턴 표시
                turn.setText("Turn: " + currentPlayerName + currentPlayerMark);
            }
        }
    }

    // 게임종료 화면 표시
    private void GameOver(String message) {
        VBox gameoverlayout = new VBox();

        Label gameoverlabel = new Label("Game Over");
        Label result = new Label(message);
        Button homeButton = new Button("Home");
        Button resetButton = new Button("Reset");

        Stage gameoverstage = new Stage();

        // Home 버튼, 로딩화면으로 이동 플레이어 이름 새로 입력력
        homeButton.setOnAction(e -> {
            Loading();
            gameoverstage.close();
        });

        // Reset 버튼, 플레이어 변경 X
        resetButton.setOnAction(e -> {
            Gamescreen();
            gameoverstage.close();
        });

        gameoverlayout.getChildren().addAll(gameoverlabel, result, homeButton, resetButton);
        Scene gameoverscene = new Scene(gameoverlayout, 250, 200);
        gameoverstage.setScene(gameoverscene);
        gameoverstage.show();
    }

    // 점수판 표시
    private void showScoreboard() {
        VBox scoreboardLayout = new VBox();

        Label title = new Label("Scoreboard");
        Label scores = new Label(
            "===== Scoreboard =====\n" +
            Xplayer.get() + " (X): " + xwin + "승\n" +
            Oplayer.get() + " (O): " + owin + "승\n" +
            "Draws " + draw + "\n" +
            "======================"
        );
        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> closeButton.getScene().getWindow().hide());
        scoreboardLayout.getChildren().addAll(title, scores, closeButton);
        Scene scoreboardScene = new Scene(scoreboardLayout);
        Stage scoreboardStage = new Stage();
        scoreboardStage.setScene(scoreboardScene);
        scoreboardStage.show();
    }

    // 버튼 비활성화
    private void disablebutton() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!buttons[row][col].isDisabled()) {
                    buttons[row][col].setDisable(true); // 버튼 비활성화
                }
            }
        }
    }

    // 버튼 초기화
    private void reset() {
        gameboard.boardreset(); // 게임 보드 초기화
        for (int row = 0; row < 3; row++) { // 버튼 초기화화
            for (int col = 0; col < 3; col++) {
                if (!buttons[row][col].getText().isEmpty()) {
                    buttons[row][col].setText(""); // 텍스트 지우기
                }
                if (buttons[row][col].isDisabled()) {
                    buttons[row][col].setDisable(false); // 버튼 활성화
                }
            }
        }
        // 턴 표시 초기화
        boolean isXTurn = gameboard.currentplayer().equals("X");
        String currentPlayerName;
        String currentPlayerMark;
        // X 플레이어 턴이면
        if (isXTurn) {
            currentPlayerName = Xplayer.get();
            currentPlayerMark = " (X)";
        } else {
        // O 플레이어 턴이면
            currentPlayerName = Oplayer.get();
            currentPlayerMark = " (O)";
        }
        turn.setText("Turn: " + currentPlayerName + currentPlayerMark);
    }

    // 점수 표기 업데이트
    private void update() {
        score.textProperty().bind(
            new SimpleStringProperty("")
                .concat(Xplayer)
                .concat(" Wins: ")
                .concat(xwin)
                .concat(" / ")
                .concat(Oplayer)
                .concat(" Wins: ")
                .concat(owin)
                .concat(" / Draws: ")
                .concat(draw)
        );
    }
}