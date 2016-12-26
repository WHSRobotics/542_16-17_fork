package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Template class for Matt's 3-part switch case idea
 */
public abstract class AutoOpSwitchCaseTemplate extends OpMode {

    //Byte for controlling which state the case will be in: init(0), loop(1-99), or exit(100)
    byte state = 0;
    int i;
    String action;

    @Override
    public void loop() {

        switch (action){
            case "action goes here (ex: shoot two particles)":

                if(state == 0){             //Init phase
                    //Code goes here
                    state = 1;
                }
                else if(state == 1){        //Loop phase
                    if(/*exit condition goes here*/true) {
                        state = 2;
                    }
                    else {
                        //code goes here
                    }
                }
                else{                       //Exit phase
                    //exit code goes here
                    state = 0;
                    action = "next action";       //Advance to the next case
                }

                break;
        }
    }

}
