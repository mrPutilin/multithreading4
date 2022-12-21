import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static ArrayBlockingQueue<String> letterA = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> letterB = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> letterC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {


        new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    letterA.put(text);
                    letterB.put(text);
                    letterC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        new Thread(() -> {
            int maxA = 0;
            try {
                maxA = maxThisLetter(letterA, 'a');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Максимальное кол-во символов 'a' составляет %d \n", maxA);
        }).start();


        new Thread(() -> {
            int maxB = 0;
            try {
                maxB = maxThisLetter(letterB, 'b');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Максимальное кол-во символов 'b' составляет %d \n", maxB);
        }).start();

        new Thread(() -> {
            int maxC = 0;
            try {
                maxC = maxThisLetter(letterC, 'c');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Максимальное кол-во символов 'c' составляет %d \n", maxC);
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxThisLetter(ArrayBlockingQueue<String> letter, char myChar) throws InterruptedException {
        int count = 0;
        int max = 0;
        for (int i = 0; i < 10_000; i++) {
            String u = letter.take();
            for (char a : u.toCharArray()) {
                if (a == myChar)
                    count++;
                if (count > max)
                    max = count;
            }
            count = 0;
        }
        return max;
    }
}
