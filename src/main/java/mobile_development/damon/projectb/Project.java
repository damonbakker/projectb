package mobile_development.damon.projectb;

import java.util.Date;

/**
 * Created by Damon on 10/4/2015.
 */
public class Project
{
    private int id;
    private String name;
    private int completion_status;
    private Date deadline;
    private String[] participants;


    public Project(int id,String name,int completion_status,Date deadline,String[] participants)
    {
        this.id = id;
        this.name = name;
        this.completion_status = completion_status;
        this.deadline = deadline;
        this.participants = participants;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getCompletion_status()
    {
        return this.completion_status;
    }

    public Date getDeadline()
    {
        return this.deadline;
    }

    public String[] getParticipants()
    {
        return this.participants;
    }

}
