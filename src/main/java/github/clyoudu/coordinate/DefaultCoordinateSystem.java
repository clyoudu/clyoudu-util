package github.clyoudu.coordinate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2019/8/11
 * @time 12:29
 * @desc DefaultCoordinateSystem
 */
public class DefaultCoordinateSystem extends AbstractCoordinateSystem {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultCoordinateSystem.class);

    private final static List<Coordinate> COORDINATE_LIST = new ArrayList<>(2500000);

    public DefaultCoordinateSystem(InputStream inputStream) {
        try (InputStreamReader isr = new InputStreamReader(inputStream); BufferedReader br = new BufferedReader(isr, 10 * 1024 * 1024)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split(",");
                COORDINATE_LIST.add(new Coordinate(Long.parseLong(items[0]), Double.parseDouble(items[1]), Double.parseDouble(items[2])));
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Load data error.", e);
        }
    }

    @Override
    public Coordinate create(Coordinate coordinate) {
        if (COORDINATE_LIST.contains(coordinate)) {
            throw new IllegalArgumentException("Duplicated coordinate: " + coordinate.toString());
        }

        COORDINATE_LIST.add(coordinate);

        return coordinate;
    }

    @Override
    public Coordinate update(Coordinate coordinate) {
        if(!contains(new Coordinate(coordinate.getId(), null, null))){
            throw new IllegalArgumentException("Coordinate not found: " + coordinate.getId());
        }
        if(contains(new Coordinate(null, coordinate.getX(), coordinate.getY()))){
            throw new IllegalArgumentException("Coordinate exist: " + coordinate.getX() + "," + coordinate.getY());
        }

        create(coordinate);

        return coordinate;
    }

    @Override
    public Coordinate delete(Long id) {
        int index = COORDINATE_LIST.indexOf(new Coordinate(id, null, null));
        if(index < 0){
            throw new IllegalArgumentException("Coordinate not found coordinate: " + id);
        }

        Coordinate coordinate = COORDINATE_LIST.get(index);

        COORDINATE_LIST.remove(index);

        return coordinate;
    }

    @Override
    public Coordinate search(Double x, Double y, Double distance) {
        return COORDINATE_LIST.parallelStream()
                .map(coordinate -> coordinate.setDistance(Math.sqrt(Math.pow(x - coordinate.getX(), 2) + Math.pow(y - coordinate.getY(), 2))))
                .filter(coordinate -> coordinate.getDistance() <= distance)
                .min(Comparator.comparingDouble(Coordinate::getDistance))
                .orElse(null);
    }

    public Coordinate searchSquare(Double x, Double y, Double distance) {
        return COORDINATE_LIST.parallelStream()
                .filter(coordinate -> coordinate.getX() <= x + distance && coordinate.getX() >= x - distance && coordinate.getY() <= y + distance && coordinate.getY() >= y- distance)
                .map(coordinate -> coordinate.setDistance(Math.sqrt(Math.pow(x - coordinate.getX(), 2) + Math.pow(y - coordinate.getY(), 2))))
                .filter(coordinate -> coordinate.getDistance() <= distance)
                .min(Comparator.comparingDouble(Coordinate::getDistance))
                .orElse(null);
    }

    @Override
    public Boolean contains(Coordinate coordinate) {
        return COORDINATE_LIST.contains(coordinate);
    }
}
