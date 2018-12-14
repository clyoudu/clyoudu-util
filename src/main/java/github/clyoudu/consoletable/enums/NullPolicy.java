package github.clyoudu.consoletable.enums;

import github.clyoudu.consoletable.table.Cell;

/**
 * Create by IntelliJ IDEA
 * do what when cell is null or cell value is null
 * @author chenlei
 * @dateTime 2018/12/14 11:44
 * @description NullPolicy
 */
public enum NullPolicy {

    THROW {
        @Override
        public Cell getCell(Cell cell) {
            throw new IllegalArgumentException("cell or value is null: " + cell);
        }
    },
    NULL_STRING {
        @Override
        public Cell getCell(Cell cell) {
            if(cell == null){
                return new Cell("null");
            }
            cell.setValue("null");
            return cell;
        }
    },
    EMPTY_STRING {
        @Override
        public Cell getCell(Cell cell) {
            if(cell == null){
                return new Cell("");
            }
            cell.setValue("");
            return cell;
        }
    };

    public abstract Cell getCell(Cell cell);

}
