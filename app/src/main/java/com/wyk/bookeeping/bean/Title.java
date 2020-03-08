package com.wyk.bookeeping.bean;

public class Title {
    private int num;
    private int income;
    private int expenditure;

    public Title(int num, int income, int expenditure) {
        this.num = num;
        this.income = income;
        this.expenditure = expenditure;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(int expenditure) {
        this.expenditure = expenditure;
    }
}
