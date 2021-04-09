package github.clyoudu.sqlrewrite;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class MonteCarloPi {

    public static void main(String[] args) throws Exception {

        /*long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            pi(1000000);
        }
        System.out.println("total cost " + (System.currentTimeMillis() - start) + " ms");*/

        CompletableFuture[] futures = new CompletableFuture[200];
        for (int i = 0; i < 200; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> {
                pi(10);
                return null;
            }, ForkJoinPool.commonPool());
        }
        CompletableFuture.allOf(futures).join();
    }

    public static void pi(int numberOfDarts) {
        long start = System.currentTimeMillis();
        double radius = 1.0;
        Dartboard d = new Dartboard(radius);

        for (int i = 1; i <= numberOfDarts; i++) {
            Toss t = Toss.getRandom(radius);
            d.strike(t);
        }

        double fractionIn = d.getFractionIn();
        double pi = 4.0 * fractionIn;
        System.out.println(
            "(" + numberOfDarts + ")Pi is approximately " + pi + ", cost " + (System.currentTimeMillis() - start) +
                " ms.");
    }

}

class Dartboard {

    private double radius;

    private int insideCircle, outsideCircle;

    public Dartboard(double radius) {
        this.radius = radius;
        insideCircle = 0;
        outsideCircle = 0;
    }

    public void strike(Toss toss) {
        double x = toss.getX();
        double y = toss.getY();

        if (Math.sqrt(x * x + y * y) < radius) {
            insideCircle++;
        } else {
            outsideCircle++;
        }
    }

    public double getFractionIn() {
        double total = (double) (insideCircle + outsideCircle);
        return (double) insideCircle / total;
    }
}

class Toss {

    private double x, y;

    public Toss(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static Toss getRandom(double radius) {

        double x, y;
        double size = Math.random();
        double sign = Math.random();
        size = size * radius;

        if (sign > 0.5) {
            x = size;
        } else {
            x = -size;
        }

        size = Math.random();
        sign = Math.random();
        size = size * radius;

        if (sign > 0.5) {
            y = size;
        } else {
            y = -size;
        }

        return new Toss(x, y);
    }
}
