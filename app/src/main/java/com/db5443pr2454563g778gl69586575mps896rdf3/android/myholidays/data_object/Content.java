package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object;

import java.util.ArrayList;
import java.util.List;

public class Content {

    private String title;
    private String schoolyear;
    private List<Vacation> vacations = new ArrayList<Vacation>();

    public Content() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchoolyear() {
        return schoolyear;
    }

    public void setSchoolyear(String schoolyear) {
        schoolyear = schoolyear.replaceAll("\\s+","").trim();
        this.schoolyear = schoolyear;
    }

    @Override
    public String toString() {
        return "Content{" +
                "title='" + title + '\'' +
                ", schoolyear='" + schoolyear + '\'' +
                ", vacations=" + vacations +
                '}';
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }
}
