package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class Mianshiti1604 {

    public static void main(String[] args) {
        System.out.println(tictactoe(new String[]{"O X", " XO", "X O"}));
        System.out.println(tictactoe(new String[]{"OOX", "XXO", "OXO"}));
        System.out.println(tictactoe(new String[]{"OOX", "XXO", "OX "}));
        System.out.println(tictactoe(
            new String[]{"   X O  O ", " X X    O ", "X  X    O ", "X    OX O ", "X   XO  O ", "X  X O  O ",
                "O  X O  O ", "     O  OX", "     O  O ", "   X XXXO "}));
    }

    private static String tictactoe(String[] board) {
        boolean pending = false;
        int length = board.length;
        boolean xColWin = false;
        boolean oColWin = false;

        for (int lineNo = 0; lineNo < length; lineNo++) {
            String line = board[lineNo];

            char c = line.charAt(0);
            char lineResult = line.charAt(0);
            for (int i = 0; i < line.length(); i++) {
                if (' ' == line.charAt(i)) {
                    pending = true;
                }
                lineResult = (char) (line.charAt(i) & lineResult);
            }
            if (lineResult == c && lineResult != ' ') {
                return String.valueOf(c);
            }
        }
        if (pending) {
            return "Pending";
        }

        return "Draw";
    }
}
