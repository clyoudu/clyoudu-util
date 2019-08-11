package github.clyoudu.coordinate;

import com.alibaba.fastjson.JSON;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2019/8/11
 * @time 12:58
 * @desc Coordinate
 */
public class Coordinate {

    private Long id;
    private Double x;
    private Double y;

    private Double distance;

    public Coordinate(Long id, Double x, Double y) {
        if(id == null && (x == null || y == null)){
            throw new IllegalArgumentException("Either id or xy must not be null.");
        }
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public Coordinate setId(Long id) {
        this.id = id;
        return this;
    }

    public Double getX() {
        return x;
    }

    public Coordinate setX(Double x) {
        this.x = x;
        return this;
    }

    public Double getY() {
        return y;
    }

    public Coordinate setY(Double y) {
        this.y = y;
        return this;
    }

    public Double getDistance() {
        return distance;
    }

    public Coordinate setDistance(Double distance) {
        this.distance = distance;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id == null ? 0 : id);
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Coordinate)) {
            return false;
        }

        if(id != null && ((Coordinate) obj).getId() != null){
            return id.equals(((Coordinate) obj).getId());
        }

        if(x != null && ((Coordinate) obj).getX() != null) {
            return (x.equals(((Coordinate) obj).getX())) && y.equals(((Coordinate) obj).getY());
        }

        return false;
    }
}
