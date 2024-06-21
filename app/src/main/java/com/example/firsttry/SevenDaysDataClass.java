package com.example.firsttry;

public class SevenDaysDataClass {
    private String date;
    private String icon;
    private String maxTemp;
    private String minTemp;
    private String maxWind;
    private String humidity;
    private String chance_rain;
    private String sunrise;
    private String sunset;
    private String moon;

    public SevenDaysDataClass(String date, String icon, String maxTemp, String minTemp, String maxWind, String humidity, String chance_rain, String sunrise, String sunset, String moon) {
        this.date = date;
        this.icon = icon;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.maxWind = maxWind;
        this.humidity = humidity;
        this.chance_rain = chance_rain;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.moon = moon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxWind() {
        return maxWind;
    }

    public void setMaxWind(String maxWind) {
        this.maxWind = maxWind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getChance_rain() {
        return chance_rain;
    }

    public void setChance_rain(String chance_rain) {
        this.chance_rain = chance_rain;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }
}
