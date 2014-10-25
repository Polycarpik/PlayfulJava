import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Polina on 21-Oct-14.
 */
public class Game {

    private GameInterface siri = new GameInterface("Siri"); // just a little joke
    private int min;  //We mean interval including min value.
    private int max;  //Same here.
    boolean gameExit = false;

    public Game() {
        this.min = 0;
        this.max = 100;
    }

    private class GameInterface {

        private String name;
        private String answer;

        private GameInterface(String name) {
            this.name = name;
        }

        //Replics of programm

        public void sayHello() {
            System.out.println("Hello user! My name is " + name + ". ^__^");
            System.out.println("I'm very glad to see you.");
            System.out.println("Let's play a game! I'll think of a number and you will have to guess it.");
        }

        public void inform() {
            System.out.println("If you print any letter in answer, i will think that you want to quit my game.");
            System.out.println("Except cases when i ask you. ^__^");
        }

        public void setInterval() {
            System.out.println("Would you like to set interval? (y/n)");
        }

        public void confirmGeneration() {
            System.out.println("I've generated the number.");
        }

        public void ask() {
            System.out.println("Would you like to play my game again?(y/n)");
        }

        public void offerGuessing() {
            System.out.println("Now you can guess.");
        }

        public void ifLessCase() {
            System.out.println("Your guess is incorrect. My number is bigger. =P");
        }

        public void ifMoreCase() {
            System.out.println("Your guess is incorrect. My number is smaller. =P");
        }

        public void ifGuessed() {
            System.out.println("You guessed my number! Congratulations! ^__^");
        }

        public void ifIdiot(int min, int max) {
            System.out.println("You are completely wrong. Try one more time. =(");
            System.out.println("You should propose numbers in interval [" + min + ";" + max + "]");
        }

        public void askIfExit() {
            System.out.println("Are you sure you want to leave? =( \n(y/n)");
        }

        public void informMinMax() {
            System.out.println("Now, please, print min(inclusivly) and max(inclusivly).");
            System.out.println("End of interval will be bigger number by default.");
        }

        public void ifIdiotInputingInterval() {
            System.out.println("You should print two different numbers.");
        }

        public void askToReprint() {
            System.out.println("Reprint your number, please.");
        }

        public void comeAgain() {
            System.out.println("I don't understand you. Try printing again and now print 'y' or 'n'");
        }

        public void preach() {
            System.out.println("You have already tried this number. Try else one.");
        }

        public void remind() {
            System.out.print("Let me remind you your guesses:");
        }

        public void sayGoodye() {
            System.out.println("It was nice to meet you!");
            System.out.println("Hope to see you again. =)");
        }

        //User behaviour analyzer
        private void getLongAnswer() {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                this.answer = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean analyzeAnswer() {
            try {
                getLongAnswer();
                int answerToInt = Integer.parseInt(answer);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        public int userAnswer() throws IOException {
            return Integer.parseInt(answer);
        }

        public boolean analyzeYNanswers() {
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                int answer = 0;
                try {
                    answer = br.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (answer == 121) {         /* 'y' represents as 121 in integer */
                    return true;
                } else if (answer == 110) {   /* 'n' represents as 110 in integer */
                    return false;
                } else {
                    comeAgain();
                }
            }
        }

    }

    private int rand(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
         /*
         * We provide shift from min to 0.
         * NextInt method generates random number in interval [0; max-min+1).
         * Then we shift it to interval [min; max+1) or [min;max], what is
         * the same to int.
         */
    }

    private int rand() {
        Random random = new Random();
        return random.nextInt();
    }

    private int checkNumber(int guess, int numberToGuess) {
        if (guess < numberToGuess) {
            min = guess;
            siri.ifLessCase();
            return -1;
        } else if (guess > numberToGuess) {
            max = guess;
            siri.ifMoreCase();
            return 1;
        } else {
            siri.ifGuessed();
            return 0;
        }
    }

    private void setInterval() {
        siri.informMinMax();
        while (true) {
            int min = getAnswer();
            int max = getAnswer();
            if (min < max) {
                this.max = max;
                this.min = min;
                return;
            } else if (min > max) {
                this.max = min;
                this.min = max;
                return;
            } else {
                siri.ifIdiotInputingInterval();
            }
        }
    }

    private int getAnswer() {
        while (true) {
            if (siri.analyzeAnswer()) try {
                return siri.userAnswer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            else {
                siri.askIfExit();
                if (siri.analyzeYNanswers()) {
                    gameExit = true;
                    return -1;
                } else {
                    siri.askToReprint();
                }
            }
        }
    }

    private void gameStart() {
        siri.sayHello();
        siri.inform();
    }

    private void gameCycle() {
        siri.setInterval();
        if (siri.analyzeYNanswers()) {
            setInterval();
        } else {
            min = 0;
            max = 100;
        }
        ArrayList userGuesses = new ArrayList();
        int numberToGuess = rand(min, max);
        siri.confirmGeneration();
        siri.offerGuessing();
        int answer;
        while (true) {
            answer = getAnswer();
            if(gameExit){
                break;
            }
            if (userGuesses.contains(answer)) {
                siri.preach();
                continue;
            }
            userGuesses.add(answer);
            if (answer < min || answer > max) {
                siri.ifIdiot(min, max);
                continue;
            }
            if (checkNumber(answer, numberToGuess) == 0) {
                break;
            }
            if (!userGuesses.isEmpty()) {
                siri.remind();
                System.out.println(" " + userGuesses.toString());
            }
        }

    }

    void playGame() throws IOException {
        gameStart();
        while(true) {
            gameCycle();
            if(!gameExit) {
                siri.ask();
                if (!siri.analyzeYNanswers()) {
                    break;
                }
            } else {
                break;
            }
        }
        siri.sayGoodye();
    }


}
