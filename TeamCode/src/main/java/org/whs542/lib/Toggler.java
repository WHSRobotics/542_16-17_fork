package org.whs542.lib;

/**
 * Created by Moses.
 */


//TODO: Perhaps change this to include a subsystem state enum that's passed in

public class Toggler
{
    private boolean pressedDec = false;
    private boolean pressedInc = false;
    private boolean limited = false;
    private int state = 0;
    private int numberOfStates = 0;

    public Toggler(int stateNum, int initState)
    {
        numberOfStates = stateNum;
        limited = true;
        state = (initState < stateNum) && (initState > -1) ? initState: 0;
    }

    public Toggler(int stateNum)
    {
        numberOfStates = stateNum;
    }

    public int currentState()
    {
        return state;
    }

    public int howManyStates()
    {
        return numberOfStates;
    }

    //This boolean trigger can have an expression with && or || for further functionality
    public void changeState(boolean inc, boolean dec)
    {
        if(inc)
        {
            if(!pressedInc)
            {
                if(limited)
                {
                    state = ((state + 1) != numberOfStates)
                            ? (state + 1) % numberOfStates
                            :state;
                }
                else {
                    state = (state + 1) % numberOfStates;
                }
            }
            pressedInc = true;
        }
        else {
            pressedInc = false;
        }

        if(dec)
        {
            if(!pressedDec)
            {
                if(limited)
                {
                    state = ((state - 1) != -1)
                            ? ((state - 1) % numberOfStates+numberOfStates)%numberOfStates
                            :state;
                }
                else {
                    state = ((state - 1) % numberOfStates+numberOfStates)%numberOfStates;
                }
            }
            pressedDec = true;
        }
        else
        {
            pressedDec = false;
        }
    }

    //This boolean trigger can have an expression with && or || for further functionality
    public void changeState(boolean inc)
    {
        if(inc)
        {
            if(!pressedInc)
            {
                if(limited)
                {
                    state = ((state + 1) != numberOfStates)
                            ? (state + 1) % numberOfStates
                            :state;
                }
                else {
                    state = (state + 1) % numberOfStates;
                }
            }
            pressedInc = true;
        }
        else
        {
            pressedInc = false;
        }
    }

    public int setState(int stateL)
    {
        if(stateL < numberOfStates & stateL >= 0)
        {
            state = stateL;
        }
        return state;
    }
}
