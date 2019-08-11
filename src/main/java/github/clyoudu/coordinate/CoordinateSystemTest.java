package github.clyoudu.coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2019/8/11
 * @time 14:01
 * @desc CoordinateSystemTest
 */
public class CoordinateSystemTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(CoordinateSystemTest.class);

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        DefaultCoordinateSystem system = new DefaultCoordinateSystem(Thread.currentThread().getContextClassLoader().getResourceAsStream("github/clyoudu/coordinate/map.txt"));
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("Please input:");
        String line;
        while((line = scanner.nextLine()) != null && !line.equals("exit")) {
            String[] items = line.split(",");
            run(system, Double.parseDouble(items[0]), Double.parseDouble(items[1]), Double.parseDouble(items[2]));
            LOGGER.info("Please input:");
        }
    }

    private static void run(DefaultCoordinateSystem system, Double x, Double y, Double distance) {
        long start = System.currentTimeMillis();
        //LOGGER.info("{}", system.contains(new Coordinate(2486162L,5379.09D,692.01D)));
        LOGGER.info("{}", system.searchSquare(x, y, distance));
        LOGGER.info("{}", System.currentTimeMillis() - start);
    }


}
