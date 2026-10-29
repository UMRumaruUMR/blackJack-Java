import java.util.Random;
import java.util.Scanner;

class blackJack {
    static Scanner scan = new Scanner(System.in);
    static Random rand = new Random();
    // [Color][Number]
    static int[][] trumpCards = new int[4][13];

    // 0 = ♠ / 1 = ♧ / 2 = ♡ / 3 = ♦

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println();
            System.out.println("<ブラックジャック>");
            System.out.println("ゲーム終了: 0 ");
            System.out.println("ルールを確認: 1 ");
            System.out.println("ゲームスタート: 2");
            choice = readInt();
            if (choice == 1)
                ruleInformation();
            else if (choice == 2)
                gameStart();
            else if (choice == 0)
                System.out.println("ゲームを終了します。");
            else
                System.out.println("0, 1, 2の中で選んでください");
        } while (choice != 0);
    }

    static void ruleInformation() {
        System.out.println();
        System.out.println();
        System.out.println("BlackJackへようこそ");
        System.out.println("BlackJackのルール説明: ");
        System.out.println("カードの数字の合計が21以下の範囲の中でディーラーより21に近づけば勝ちです。");
        System.out.println("合計がディーラーより21から離れていたり、21を超えたら負けです。");
        System.out.println("詳細: ");
        System.out.println("1. コインの枚数を選びます。 ");
        System.out.println("2. ディーラーがカード2枚を引きます。");
        System.out.println("3. プレイヤー(自分)もカードを2枚引きます。");
        System.out.println("4. ディーラーが持っているカード合計とプレイヤーのカードの合計を確認した後, ");
        System.out.println("   カードをもう一枚引くか、ストップするか決めます。 ");
        System.out.println("5. ディーラーのカードが21と差が大きい場合、ディーラーはもう一枚カードを引きます。 ");
        System.out.println("6. ディーラーのカードとプレイヤーのカードを比較し、勝敗が決められます。");
        System.out.println();
        System.out.println("この時、Aceカードは1または11の中で都合のいい数字で使うことができます。");
        System.out.println("ディーラーとプレイヤーどっちも21を超えたら引き分けです。");
        System.out.println();
        System.out.println();
        waitEnter();
        clearScreen();
    }

    static void gameStart() {
        int chip;
        int betChips;
        int dealerCards, playerCards;
        int sumDealerCards = 0;
        int sumPlayerCards = 0;
        int dealerAce = 0;
        int playerAce = 0;
        int winOrLose;
        int playerChoice = 0;

        do {
            System.out.println("チップを何枚で始まりますか?(最大20枚)");
            chip = readInt();
            if (chip <= 0 || chip >= 21) {
                System.out.println("1~20の中で選んでください。");
            }
        } while (chip <= 0 || chip >= 21);

        do {
            // 初期化
            sumDealerCards = 0;
            sumPlayerCards = 0;
            dealerAce = 0;
            playerAce = 0;
            betChips = 0;
            playerChoice = 0;

            //カードの初期化
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 13; j++)
                    trumpCards[i][j] = j + 1;

            do {
                System.out.println();
                System.out.println("現在のチップ数: " + chip);
                System.out.println("ベットするコイン数を入力してください。//0 = 終了");
                betChips = readInt();
                if (betChips == 0) {
                    System.out.println("メニューに戻ります。");
                    return; // gameStart() → main()
                }
                if (betChips < 1 || betChips > chip)
                    System.out.println("1~" + chip + "枚数の中で選んでください");
            } while (betChips < 1 || betChips > chip);

            // ディーラーのカード引き
            System.out.println();
            System.out.print("ディーラーのカード: ");
            dealerCards = drawCard();
            sumDealerCards += dealerCards;
            if (dealerCards == 1)
                dealerAce++;
            System.out.print(", ");
            dealerCards = drawCard();
            sumDealerCards += dealerCards;
            if (dealerCards == 1)
                dealerAce++;
            System.out.println();
            System.out.println("ディーラーのカード合計: " + bestSum(sumDealerCards, dealerAce));
            waitEnter(); // Enter押したら次へ

            // プレイヤーのカード引き
            System.out.print("プレイヤーのカード: ");
            playerCards = drawCard();
            sumPlayerCards += playerCards;
            if (playerCards == 1)
                playerAce++;
            System.out.print(", ");
            playerCards = drawCard();
            sumPlayerCards += playerCards;
            if (playerCards == 1)
                playerAce++;
            System.out.println();
            System.out.println("プレイヤーのカード合計: " + bestSum(sumPlayerCards, playerAce));
            System.out.println();

            // プレイヤーカードをもう一枚引くか確認
            while (bestSum(sumPlayerCards, playerAce) < 21) {
                System.out.println("カードをもう一枚引く: 1  /  やめる: 0");
                playerChoice = readInt();
                if (playerChoice == 1) {
                    System.out.print("プレイヤーがカードをもう一枚引きます: ");
                    playerCards = drawCard();
                    sumPlayerCards += playerCards;
                    if (playerCards == 1)
                        playerAce++;
                    System.out.println();
                    System.out.println("プレイヤーのカード合計: " + bestSum(sumPlayerCards, playerAce));
                    System.out.println();
                } else if (playerChoice == 0)
                    break;
                else
                    System.out.println("0か1の中で選んでください。");
                waitEnter();
            }

            int dealerBest = bestSum(sumDealerCards, dealerAce);
            int limit = rand.nextInt(10) + 1; // 1~10

            if (21 - dealerBest > limit) {
                System.out.print("ディーラーがカードをもう一枚引きます: ");
                dealerCards = drawCard();
                sumDealerCards += dealerCards;
                if (dealerCards == 1)
                    dealerAce++;
                System.out.println();
                System.out.println("ディーラーのカード合計: " + bestSum(sumDealerCards, dealerAce));
                System.out.println();
            }
            waitEnter();
            sumDealerCards = bestSum(sumDealerCards, dealerAce);
            sumPlayerCards = bestSum(sumPlayerCards, playerAce);

            // 勝敗出力
            winOrLose = winLose(sumPlayerCards, sumDealerCards);
            if (winOrLose == -1) {
                System.out.println("プレイヤーのカードの合計は" + sumPlayerCards + "で、");
                System.out.println("ディーラーの合計は" + sumDealerCards + "で、ディーラーの勝ちです。");
                chip = chip - betChips;
            } else if (winOrLose == 0) {
                System.out.println("プレイヤーのカードの合計は" + sumPlayerCards + "で、");
                System.out.println("ディーラーの合計は" + sumDealerCards + "で、引き分けです。");
            } else {
                System.out.println("プレイヤーのカードの合計は" + sumPlayerCards + "で、");
                System.out.println("ディーラーの合計は" + sumDealerCards + "で、プレイヤーの勝ちです。");
                chip = chip + betChips;
            }
            System.out.println();
            waitEnter();
            clearScreen();
        } while (chip >= 1);
        System.out.println("チップがなくなったので終了します。");
        waitEnter();
        clearScreen();
    }

    static int drawCard() {
        int row, col;

        do {
            row = rand.nextInt(4);
            col = rand.nextInt(13);
        } while (trumpCards[row][col] == -1);

        trumpCards[row][col] = -1;

        String[] suits = { "♠", "♣", "♥", "♦" };
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

        System.out.print(suits[row] + ranks[col]);
        return Math.min(col + 1, 10);
    }

    // Aceがあって、11で使っても21以下だったら +10
    static int bestSum(int sum, int aceCount) {
        if (aceCount > 0 && sum + 10 <= 21)
            return sum + 10;
        return sum;
    }

    static int winLose(int player, int dealer) {
        if (player > 21) {
            if (dealer > 21)
                return 0;
            return -1;
        }
        if (dealer > 21)
            return 1;
        if (player > dealer)
            return 1;
        if (player < dealer)
            return -1;
        return 0;
    }

    static void waitEnter() {
        System.out.print("(Enterキーで次へ)");
        scan.nextLine();
    }

    static int readInt() {
        while (true) {
            String line = scan.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("数字(整数)を入力してください: ");
            }
        }
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}