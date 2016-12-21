package org.whs542.lib.hwtest;

/**
 * A demo class to accompany {@link TestBot}
 */
public class TestBotDemo extends TestBot {

    @Override
    public void init() {
        super.init();               //Runs init() method from superclass (TestBot)
    }

    @Override
    public void loop() {
        super.loop();               //Runs loop() method from superclass (TestBot)
        setRLPower(1.0, 1.0);
    }

}
