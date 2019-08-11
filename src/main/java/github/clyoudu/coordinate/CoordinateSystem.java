package github.clyoudu.coordinate;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2019/8/11
 * @time 12:12
 * @desc CoordinateSystem
 */
public interface CoordinateSystem {

    Coordinate create(Coordinate coordinate);

    Coordinate create(Long id, Double x, Double y);

    Coordinate update(Coordinate coordinate);

    Coordinate update(Long id, Double x, Double y);

    Coordinate delete(Long id);

    Coordinate search(Double x, Double y, Double distance);

    Boolean contains(Double x, Double y);

    Boolean contains(Long id);

    Boolean contains(Coordinate coordinate);

}
