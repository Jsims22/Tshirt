import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Main {
	
	static Controller cont;
	public static double deadzone = 0.5;
	
	public static void main(String args[]) {
		
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Controllers.poll();
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput leftForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "PinLED", PinState.LOW);
		final GpioPinDigitalOutput leftBackward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "PinLED", PinState.LOW);
		final GpioPinDigitalOutput rightForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "PinLED", PinState.LOW);
		final GpioPinDigitalOutput rightBackward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "PinLED", PinState.LOW);
		final GpioPinDigitalOutput cannonSolenoid = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "PinLED", PinState.LOW);
		
		for(int i = 0; i < Controllers.getControllerCount(); i++) {
			cont = Controllers.getController(i);
			if(cont.getName().equals("Controller (Xbox 360 Wireless Receiver for Windows)")){
				
				while (true) {
					Controllers.poll();
					if(cont.isButtonPressed(7) == true) {
						System.out.println("Exiting...");
						break;
					}
					if (cont.getAxisValue(0) * -1 > deadzone) { //left joy Y up
						System.out.println("Left Joy True");
						leftForward.high();
						leftBackward.low();
					} else if (cont.getAxisValue(0) > deadzone) { //left joy Y up
						System.out.println("Left Joy True");
						leftForward.low();
						leftBackward.high();
					} else {
						leftForward.low();
						leftBackward.low();
					}
					
					if (cont.getAxisValue(2) * -1 > deadzone) { //right joy Y
						System.out.println("Right Joy True");
						rightForward.high();
						rightBackward.low();
					} else if (cont.getAxisValue(2) > deadzone) { //right joy Y up
						System.out.println("Right Joy True");
						rightForward.low();
						rightBackward.high();
					} else {
						rightForward.low();
						rightBackward.low();
					}
					
					if (Math.abs(cont.getAxisValue(4)) > 0.3) { //either trigger but not both
						System.out.println("trigger");
						cannonSolenoid.high();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cannonSolenoid.low();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						cannonSolenoid.low();
					}
					
				}
					
					
				
			}
			
		}
	}
}