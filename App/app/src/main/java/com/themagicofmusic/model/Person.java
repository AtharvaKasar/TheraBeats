package com.themagicofmusic.model;

/**
 * TheMagicOfMusic - TheraBeat
 * Created by Atharva Kasar
 */

public class Person {

    int personID;
    String LastName;
    String FirstName;
    String Hobby;
    String Issue;
    int age;

    public int getPersonID()
    {
        return personID;
    }

    public void setPersonID(int value)
    {
        personID = value;
    }

    public String getLastName()
    {
        return LastName;
    }

    public void setLastName(String value)
    {
        LastName = value;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public void setFirstName(String value)
    {
        FirstName = value;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int value)
    {
        age = value;
    }

    public String getHobby()
    {
        return Hobby;
    }

    public void setHobby(String value)
    {
        Hobby = value;
    }

    public String getIssue()
    {
        return Issue;
    }

    public void setIssue(String value)
    {
        Issue = value;
    }
}
