package com.tdt.artlux.Class;

import android.support.design.widget.TextInputEditText;

/**
 * Created by edgar on 03/01/2018.
 */

public class CheckText {

    public boolean FullFields(TextInputEditText TIdatos[])
    {
        boolean full=true;

        for(int i=0; i<TIdatos.length && full;i++)
        {
            if(TIdatos[i].getText().toString().isEmpty())
                full=false;
        }

        if(full)
            return true;
        else
            return false;
    }

    public boolean TryParse(String edad)
    {
        try{
            Integer.parseInt(edad);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
