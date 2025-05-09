package org.example.programminglab9;


public class GameBoard {
    private int size; // 보드 크기
    private String[][] board; // 2D 보드 배열
    private String player; // 현재 플레이어

    // 생성자: 보드 크기 설정 및 초기화
    public GameBoard(int size) {
        this.size = size;
        this.board = new String[size][size];
        this.player = "X"; // 플레이어 X부터 시작
        boardreset(); // 보드 초기화
    }

    // 보드 초기화 메소드
    public void boardreset() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = ""; // 보드리셋
            }
        }
        player = "X"; // 플레이어 X로 초기화
    }

    // 플레이어 변경 메소드
    public void switchplayer() {
        if (player.equals("X")) {
            player = "O";
        } else {
            player = "X";
        }
    }

    // 이동 메소드
    public boolean doClick(int row, int col) {
        // 칸이 비어있는지 확인
        if (row >= 0 && row < size && col >= 0 && col < size && board[row][col].isEmpty()) {
            board[row][col] = player; // 현재 플레이어  O X를 칸에 입력
            return true; // 성공
        }
        return false; // 실패
    }

    // 승자 확인 메소드
    public String checkwin() {
        // 가로 줄 확인
        for (int row = 0; row < size; row++) {
            String firstbox = board[row][0]; // 첫 번째 칸
            boolean same = true;
            for (int col = 1; col < size; col++) { // 가로줄 한줄씩 내리면서 확인
                if (!firstbox.equals(board[row][col]) || firstbox.isEmpty()) {
                    same = false;
                    break;
                }
            }
            if (same && !firstbox.isEmpty()) {
                return firstbox; // 승자 반환
            }
        }

        // 세로 줄 확인
        for (int col = 0; col < size; col++) {
            String firstCell = board[0][col]; // 첫 번째 칸
            boolean same = true;
            for (int row = 1; row < size; row++) { // 세로줄 한줄씩 내리면서 확인
                if (!firstCell.equals(board[row][col]) || firstCell.isEmpty()) {
                    same = false;
                    break;
                }
            }
            if (same && !firstCell.isEmpty()) {
                return firstCell; // 승자 반환
            }
        }

        // 대각선 확인 1
        String topLeft = board[0][0];
        boolean same = true;
        for (int i = 1; i < size; i++) { // 대각선 왼쪽 위 -> 오른쪽 아래 확인
            if (!topLeft.equals(board[i][i]) || topLeft.isEmpty()) {
                same = false;
                break;
            }
        }
        if (same && !topLeft.isEmpty()) {
            return topLeft; // 승자 반환
        }

        // 대각선 확인 2
        String topRight = board[0][size - 1];
        same = true;
        for (int i = 1; i < size; i++) { // 대각선 오른쪽 위 -> 왼쪽 아래 확인
            if (!topRight.equals(board[i][size - 1 - i]) || topRight.isEmpty()) {
                same = false;
                break;
            }
        }
        if (same && !topRight.isEmpty()) {
            return topRight; // 승자 반환
        }

        // 승자 없음
        return "";
    }

    // 보드 가득 찼는지 확인 (무승부 체크)
    public boolean checkboardfull() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col].isEmpty()) {
                    return false; // 빈칸 있음
                }
            }
        }
        return true; // 보드 가득 참
    }

    // 현재 플레이어 반환
    public String currentplayer() {
        return player;
    }
}