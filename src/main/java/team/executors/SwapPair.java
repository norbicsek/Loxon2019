package team.executors;

public class SwapPair {

    private Integer cellA;
    private Integer cellB;

    public SwapPair(Integer cellA, Integer cellB) {
        this.cellA = cellA;
        this.cellB = cellB;
    }

    public Integer getCellA() {
        return cellA;
    }

    public void setCellA(Integer cellA) {
        this.cellA = cellA;
    }

    public Integer getCellB() {
        return cellB;
    }

    public void setCellB(Integer cellB) {
        this.cellB = cellB;
    }

    public boolean hasCell(int cell) {
        return this.cellA == cell || this.cellB == cell;
    }
}
