import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int WIN_COUNT = 4; // Выигрышная комбинация
    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человек
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field; // Двуxмерный массив хранит текущее состояние игрового поля

    private static final int FIELDSIZE_X = 3; // Размерность игрового поля
    private static final int FIELDSIZE_Y = 3; // Размерность игрового поля


    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }

            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация объектов игры
     */
    private static void initialize() {
        field = new char[FIELDSIZE_X][FIELDSIZE_Y];

        for (int x = 0; x < FIELDSIZE_X; x++) {
            for (int y = 0; y < FIELDSIZE_Y; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игрового поля
     * <p>
     * +-1-2-3-
     * 1|*|X|0|
     * 2|*|*|0|
     * 3|*|*|0|
     * --------
     */
    private static void printField() {
        System.out.print("+");
        for (int x = 0; x < FIELDSIZE_X * 2 + 1; x++) {
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();

        for (int x = 0; x < FIELDSIZE_X; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < FIELDSIZE_Y; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < FIELDSIZE_X * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();

    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;

        do {

            while (true) {
                System.out.printf("Введите координату хода X (от 1 до %d): ", FIELDSIZE_X);
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }

            while (true) {
                System.out.printf("Введите координату хода Y (от 1 до %d): ", FIELDSIZE_Y);
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[y][x] = DOT_HUMAN;
    }

    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность игрового поля)
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < FIELDSIZE_X && y >= 0 && y < FIELDSIZE_Y;
    }

    /**
     * Обработка хода компьютера
     */
    private static void aiTurn() {
        int x, y;

        do {
            x = random.nextInt(FIELDSIZE_X);
            y = random.nextInt(FIELDSIZE_Y);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка состояния игры
     *
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s) {
        if (checkWin(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }

    /**
     * Проверка победы
     *
     * @param c
     * @return
     */
    private static boolean checkWin(char c) {

        // Проверка по трем горизонталям
        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;

        // Проверка по трем вертикалям
        if (field[0][0] == c && field[1][0] == c && field[2][0] == c) return true;
        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;

        // Проверка по диагоналям
        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;

        return false;
    }

    /**
     * Проверка победы
     *
     * @param c
     * @return
     */
    private static boolean checkWinV2(char c) {
        for (int x = 0; x < FIELDSIZE_X; x++) {
            for (int y = 0; y < FIELDSIZE_Y; y++) {
                //if (field[x][y] == field[x + 1][y] && field[x][y] == field[x + 2][y]) return true; // по горизонтали для 3х
                if (check1()) return true;

            }
        }

        return false;
    }

    static boolean check1() {
        int x = field.length;
        int y = field[0].length;

        for (int i = 0; i < x; i++){
            for (int j = 0; j <= y - WIN_COUNT; j++) {
                int count = 1;
                int player = field[i][j];
                for (int l = 1; l < WIN_COUNT; l++) {
                    if (player != 0 && player == field[i][j + l]) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count == WIN_COUNT) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверка на ничью
     *
     * @return
     */
    private static boolean checkDraw() {
        for (int x = 0; x < FIELDSIZE_X; x++) {
            for (int y = 0; y < FIELDSIZE_Y; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }

        return true;
    }

}
