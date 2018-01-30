package com.interview.esphere.openweather;

public class OpenWeatherMapeError
{
    private String message;

    private String cod;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getCod ()
    {
        return cod;
    }

    public void setCod (String cod)
    {
        this.cod = cod;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", cod = "+cod+"]";
    }
}
