package javabean;


import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaBean
{
  public String getDateString()
  {
    return (new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss")).format(new Date()) + " h";
  }
}