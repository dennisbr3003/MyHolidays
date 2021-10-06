package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentRoot {

    private String id;
    private String type;
    private String canonical;
    private List<Content> content = new ArrayList<Content>();
    private String notice;
    private String authorities[];
    private String creators[];
    private String license;
    private String rightsholders[];
    private String language;
    private String location;
    private String lastmodified;

    public ContentRoot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCanonical() {
        return canonical;
    }

    public void setCanonical(String canonical) {
        this.canonical = canonical;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "ContentRoot{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", canonical='" + canonical + '\'' +
                ", content=" + content +
                ", notice='" + notice + '\'' +
                ", authorities=" + Arrays.toString(authorities) +
                ", creators=" + Arrays.toString(creators) +
                ", license='" + license + '\'' +
                ", rightsholders=" + Arrays.toString(rightsholders) +
                ", language='" + language + '\'' +
                ", location='" + location + '\'' +
                ", lastmodified='" + lastmodified + '\'' +
                '}';
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public String[] getCreators() {
        return creators;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String[] getRightsholders() {
        return rightsholders;
    }

    public void setRightsholders(String[] rightsholders) {
        this.rightsholders = rightsholders;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }

    public Vacation get(int position, String schoolyear){
        Log.d("DENNIS_B", "(ContentRoot get) parameters " + position + schoolyear);
        List<Vacation> vacation;
        try {
            for (Content c : content) {
                Log.d("DENNIS_B", "(ContentRoot get) c.getSchoolyear() " + c.getSchoolyear());
                if (c.getSchoolyear().equals(schoolyear)) {
                    vacation = c.getVacations();
                    return vacation.get(position);
                }
            }
        }catch(Exception e){
            Log.d("DENNIS_B", "error getting vacation object " + e.getMessage());
        }
        return null;
    }

    public int getSize(String schoolyear){
        Log.d("DENNIS_B", "(ContentRoot getSize) parameters " + schoolyear);
        List<Vacation> vacation;
        try {
            for (Content c : content) {
                Log.d("DENNIS_B", "(ContentRoot getSize) c.getSchoolyear() " + c.getSchoolyear());
                if (c.getSchoolyear().equals(schoolyear)) {
                    vacation = c.getVacations();
                    return vacation.size();
                }
            }
        }catch(Exception e){
            Log.d("DENNIS_B", "error getting array size " + e.getMessage());
        }
        return 0;
    }

}
