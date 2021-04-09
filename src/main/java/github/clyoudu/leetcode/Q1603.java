package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class Q1603 {

    public static void main(String[] args) {
        ParkingSystem obj = new ParkingSystem(1, 1, 0);
        System.out.println(obj.addCar(1));
        System.out.println(obj.addCar(2));
        System.out.println(obj.addCar(3));
        System.out.println(obj.addCar(1));
    }

    static class ParkingSystem {

        private int big;
        private int medium;
        private int small;

        public ParkingSystem(int big, int medium, int small) {
            this.big = big;
            this.medium = medium;
            this.small = small;
        }

        public boolean addCar(int carType) {
            switch (carType) {
                case 1:
                    if(big > 0) {
                        big --;
                        return true;
                    }
                    return false;
                case 2:
                    if(medium > 0) {
                        medium --;
                        return true;
                    }
                    return false;
                case 3:
                    if(small > 0) {
                        small --;
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        }
    }

}
