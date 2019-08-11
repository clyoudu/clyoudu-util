package github.clyoudu.coordinate;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2019/8/11
 * @time 12:22
 * @desc AbstractCoordinateSystem
 */
public abstract class AbstractCoordinateSystem implements CoordinateSystem {

    @Override
    public Coordinate create(Long id, Double x, Double y) {
        return create(new Coordinate(id, x, y));
    }

    @Override
    public Coordinate update(Long id, Double x, Double y) {
        return update(new Coordinate(id, x, y));
    }

    @Override
    public Boolean contains(Double x, Double y) {
        return contains(new Coordinate(null, x, y));
    }

    @Override
    public Boolean contains(Long id) {
        return contains(new Coordinate(id, null, null));
    }
}
