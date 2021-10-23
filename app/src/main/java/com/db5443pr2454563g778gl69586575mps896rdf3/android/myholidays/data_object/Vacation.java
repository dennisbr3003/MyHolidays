package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.data_object;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays.IVacation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Vacation implements IVacation {

    private String type;
    private String compulsorydates;
    private List<Region> regions = new ArrayList<Region>();

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private Date tempDate = null;

    public Vacation() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type.replaceAll("\\r\\n|\\r|\\n", "").trim();
        this.type = type;
    }

    public String getCompulsorydates() {
        return compulsorydates;
    }

    public void setCompulsorydates(String compulsorydates) {
        this.compulsorydates = compulsorydates;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStartDate(String region){

        Log.d("DENNIS_B", "vacation.getStartDate");

        List<Region> regions = getRegions();
        String startdate = "";

        String pattern = DATE_PATTERN;
        DateFormat df = new SimpleDateFormat(pattern);

        for(Region r:regions){
            if(r.getRegion().equalsIgnoreCase(region)||r.getRegion().equals(NETHERLANDS)){
                startdate = r.getStartdate();
                Log.d("DENNIS_B", "r.getRegion() " + r.getRegion().toLowerCase());
                Log.d("DENNIS_B", "startdate " + startdate);
                TemporalAccessor accessor = timeFormatter.parse(startdate);
                tempDate = Date.from(Instant.from(accessor));
                startdate = df.format(tempDate);
            }
        }


        return startdate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getEndDate(String region){

        Log.d("DENNIS_B", "vacation.getEndDate");

        List<Region> regions = getRegions();
        String enddate = "";

        String pattern = DATE_PATTERN;
        DateFormat df = new SimpleDateFormat(pattern);

        for(Region r:regions){
            if(r.getRegion().toLowerCase().equals(region.toLowerCase())||r.getRegion().equals(NETHERLANDS)){
                enddate = r.getEnddate();
                Log.d("DENNIS_B", "enddate "+ enddate);
                TemporalAccessor accessor = timeFormatter.parse(enddate);
                tempDate = Date.from(Instant.from(accessor));
                enddate = df.format(tempDate);
            }
        }
        return enddate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getState(String region){

        // check weather a vacation is busy, in the past or in the future
        Log.d("DENNIS_B", "vacation.getState");

        String startdate1 = "";
        String enddate1 = "";
        List<Region> regions = getRegions();

        for(Region r:regions) {
            if (r.getRegion().equalsIgnoreCase(region) || r.getRegion().equals("heel Nederland")) {

                Log.d("DENNIS_B", "r.getRegion() " + r.getRegion().toLowerCase());

                startdate1 = r.getStartdate();
                enddate1 = r.getEnddate();

                Log.d("DENNIS_B", "startdate " + startdate1);
                Log.d("DENNIS_B", "enddate "+ enddate1);

                String pattern = ISO_DATE_PATTERN;
                DateFormat df = new SimpleDateFormat(pattern);

                try {

                    TemporalAccessor accessor = timeFormatter.parse(startdate1);
                    tempDate = Date.from(Instant.from(accessor));
                    startdate1 = df.format(tempDate);
                    LocalDateTime ldtStart = LocalDateTime.parse(startdate1 + ISO_TIME);

                    accessor = timeFormatter.parse(enddate1);
                    tempDate = Date.from(Instant.from(accessor));
                    enddate1 = df.format(tempDate);
                    LocalDateTime ldtEnd = LocalDateTime.parse(enddate1 + ISO_TIME);

                    Log.d("DENNIS_B", "startdate/enddate " + startdate1 + " " + enddate1);

                    // getting the actual state
                    if (LocalDateTime.now().isBefore(ldtStart)) {
                       if (((Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(LocalDateTime.now(), ldtStart)))) >= 0) &&
                        ((Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(LocalDateTime.now(), ldtStart)))) <= 7)){
                           Log.d("DENNIS_B", "state = happy");
                           return SMILEY_HAPPY; //Starts within a week
                        }
                    }
                    if (LocalDateTime.now().isBefore(ldtStart)) {
                        if ((Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(LocalDateTime.now(), ldtStart)))) > 7) {
                            Log.d("DENNIS_B", "state = content");
                            return SMILEY_CONTENT; //Does not start within a week
                        }
                    }
                    if (LocalDateTime.now().isAfter(ldtStart) && LocalDateTime.now().isBefore(ldtEnd)){
                        Log.d("DENNIS_B", "state = vacation");
                        return SMILEY_VACATION;
                    }
                    if (LocalDateTime.now().isAfter(ldtEnd)){
                        Log.d("DENNIS_B", "state = sad");
                        return SMILEY_SAD;
                    }
                    Log.d("DENNIS_B", "state = unknown");
                    return SMILEY_UNKNOWN;
                }
                catch(Exception e){
                    Log.d("DENNIS_B", "error getting vacation state " + e.getMessage());
                    return SMILEY_UNKNOWN;
                }
            }
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNumberOfTimeUnits(String region){
        return getNumberOfTimeUnits(region,"D");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNumberOfTimeUnits(String region, String TimeUnit){
        Log.d("DENNIS_B", "vacation.getNumberOfTimeUnits for " + TimeUnit);

        String startdate = "";
        List<Region> regions = getRegions();

        String pattern = ISO_DATE_PATTERN;
        DateFormat df = new SimpleDateFormat(pattern);

        for(Region r:regions) {
            if (r.getRegion().equalsIgnoreCase(region) || r.getRegion().equals(NETHERLANDS)) {
                startdate = r.getStartdate();
                try {

                    TemporalAccessor accessor = timeFormatter.parse(startdate);
                    tempDate = Date.from(Instant.from(accessor));
                    startdate = df.format(tempDate);
                    LocalDateTime ldtStart = LocalDateTime.parse(startdate + ISO_TIME);

                    if (LocalDateTime.now().isBefore(ldtStart)) {
                        switch(TimeUnit){
                            case"D":
                                if ((Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(LocalDateTime.now(), ldtStart)))) > 0) {
                                    return String.valueOf(Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(LocalDateTime.now(), ldtStart))));
                                } else {
                                    return "0";
                                }
                            case "H":
                                if ((Integer.parseInt(String.valueOf(ChronoUnit.HOURS.between(LocalDateTime.now(), ldtStart)))) > 0) {
                                    return String.valueOf(Integer.parseInt(String.valueOf(ChronoUnit.HOURS.between(LocalDateTime.now(), ldtStart))));
                                } else {
                                    return "0";
                                }
                            case "M":
                                if ((Integer.parseInt(String.valueOf(ChronoUnit.MINUTES.between(LocalDateTime.now(), ldtStart)))) > 0) {
                                    return String.valueOf(Integer.parseInt(String.valueOf(ChronoUnit.MINUTES.between(LocalDateTime.now(), ldtStart))));
                                } else {
                                    return "0";
                                }
                            default:
                                return "0";
                        }
                    } else {
                        return "0";
                    }
                }
                catch(Exception e){
                    Log.d("DENNIS_B", "error during calculation days between start and enddate " + e.getMessage());
                }
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "type='" + type + '\'' +
                ", compulsorydates='" + compulsorydates + '\'' +
                ", regions=" + regions +
                '}';
    }
}
