/*************************************************************************
 *  Andres J. Aguirre G.
 *  February 25, 2013
 *
 *  Compilation:  javac main.java
 *  Execution:    java main
 *
 *  This game is a simplified version of the  "Gun Bound" game but adding avatars with
 *  a oscillator motion for each player. A periodical tornado is added. The speed and
 *  angle are modified with a vertical bars at the left (for the left player) and the right
 *  and right (for the right player) outside the field battle. There is not resistive media
 *  and wind force, the motion is only submit to the gravitational field.
 *
 *  Programming details
 *  -------
 *    For the draw and graphics, the Standard drawing library is used (see StdDraw.java). 
 *
 *  Key codes
 *  -------
 *    1 -> 49
 *    2 -> 50
 *       .
 *       .
 *       .
 *    8 -> 56
 *    9 -> 57
 *
 *************************************************************************/

import java.util.*;
import java.text.DecimalFormat;

public class main {
	
	public static double a=-9.81;                // Gravity
	public static char l;
	public static char z=l;
	
	public static void main(String argv[]) {
		
		int xmin=0, xmax=1000, ymin=0, ymax=600; // Dimensions of the window
		
		double to=1000, t=to, dt=0.08;           // Counter "time", Time step
		double n1=0, n2=0;                       // Counters for winner
		
		DecimalFormat TwoDecimal = new DecimalFormat("0.00");
		
		// Button 1
		double WB1=60, HB1=35;                                     // Width, Height
		double xoB1=300, yoB1=0.5*HB1+10;                          // Position
		double xminB1=xoB1-0.5*WB1, xmaxB1=xoB1+0.5*WB1, yminB1=yoB1-0.5*HB1, ymaxB1=yoB1+0.5*HB1;
		// Button 2
		double WB2=WB1, HB2=HB1;                                   // Width, Height
		double xoB2=xmax-xoB1, yoB2=yoB1;                          // Position
		double xminB2=xoB2-0.5*WB2, xmaxB2=xoB2+0.5*WB2, yminB2=yoB2-0.5*HB2, ymaxB2=yoB2+0.5*HB2;
		
		// Launcher 1
		double AL1=60, wL1=0.2, dL1=0;                             // Amplitude, Frequency, Phase difference
		double WL1=60, HL1=80;                                     // Width, Height
		double xL1=1, yL1=1, xoL1=120, yoL1=AL1+HL1+80;                // Position, Initial position
		// Launcher 2
		double AL2=AL1, wL2=wL1, dL2=Math.PI/2.0;                  // Amplitude, Frequency, Phase difference
		double WL2=WL1, HL2=HL1;                                   // Width, Height
		double xL2=xmax-1, yL2=ymax-1, xoL2=xmax-xoL1, yoL2=yoL1;  // Position, Initial position
		
		// Floor 1
		double toutL1=0, toutL2=0;                                 // Times for the free falling condition (the launcher leave the floor)
		double xF1=xoL1, yF1, xF2=xoL2, yF2;                       // x and y position
		
		// Particle 1
		double rP1=5;                                              // Radius
		double xP1, yP1, xoP1=0, yoP1=0, tclickP1=0;               // Position, Initial position
		double vP1=100, vxP1=0, vyP1=0, OP1=Math.PI/4.0;           // Speed, Angle
		// Particle 2
		double rP2=rP1;                                            // Radius
		double xP2, yP2, xoP2=0, yoP2=0, tclickP2=0;               // Position, Initial position
		double vP2=vP1, vxP2=0, vyP2=0, OP2=Math.PI - Math.PI/4.0; // Speed, Angle
		
		// Storm
		double AS=60, wS=0.7;                                      // Amplitude, Frequency
		double WS=120, HS=320;                                     // Width, Height
		double xS, yS, xoS=0.5*(xmin+xmax), yoS=250;               // Position, Initial position
		double waS=0.1;                                            // Frequency for appearance of tornado
		
		// Life bar
		int life=200; // Lenght
		
		// Speed and Angle bar
		double xoSpeedBar=50, xfSpeedBar=250, yoSpeedBar=30, yfSpeedBar=45;                // xy-cordinates of the speed bar
		double xoAngleBar=xoSpeedBar, xfAngleBar=xfSpeedBar, yoAngleBar=10, yfAngleBar=25; // xy-cordinates of the angle bar
		
		// Some useful variables refer to data bars
		double xMvP1=vP1+xoSpeedBar, xMOP1=OP1*200/(Math.PI/2.0)+xoAngleBar, xMvP2=vP2+xoSpeedBar, xMOP2=(Math.PI-OP2)*200/(Math.PI/2.0)+xoAngleBar;
		
		double go1=0, go2=0;
		
		// Scale of the window
		StdDraw.setCanvasSize(xmax, ymax);
		StdDraw.setXscale(xmin-10, xmax+10);
		StdDraw.setYscale(ymin-10, ymax+10);
		
		while (t>=0) {
			
			// Clear the window at each iteration
			StdDraw.clear(StdDraw.ORANGE);
			//StdDraw.setPenColor(StdDraw.WHITE);
			//StdDraw.filledRectangle(0.5*(xmin+xmax), 0.5*(ymin+ymax), 0.5*(xmax-xmin), 0.5*(ymax-ymin));
			StdDraw.picture(0.5*(xmin+xmax), 0.5*(ymin+ymax), "images/background.jpg", xmax-xmin, ymax-ymin, 0);
			
			// The x-cordinate of the launcher is entered via keyboard
			if (StdDraw.hasNextKeyTyped()==true) {
				if (StdDraw.isKeyPressed(49)==true) xoL1=xoL1-1; // Number 1 is pressed
				if (StdDraw.isKeyPressed(50)==true) xoL1=xoL1+1; // Number 2 is pressed
				if (StdDraw.isKeyPressed(56)==true) xoL2=xoL2-1; // Number 8 is pressed
				if (StdDraw.isKeyPressed(57)==true) xoL2=xoL2+1; // Number 9 is pressed
			}
			
			// xy-cordinate of the launchers
			// If the launcher (1 or 2) don't leave the floor, the launcher keep in the battle
			if (xL1>0 && xL1<200) {
				xL1 = XL(xoL1);
				yL1 = YL(yoL1,AL1,wL1,t,dL1);
			}
			if (xL2>xmax-200 && xL2<xmax-0) {
				xL2 = XL(xoL2);
				yL2 = YL(yoL2,AL2,wL2,t,dL2);
			}
			// Save the time when the launcher leave the floor
			if (xoL1==0 || xoL1==200) toutL1=t;
			if (xoL2==xmax-200 || xoL2==xmax-0) toutL2=t;
			// When the launcher leave the floor, the xy-cordinates change to free falling cordinates
			if (xL1<=0 || xL1>=200) {
				xL1 = XL(xL1);
				yL1 = YP(yL1,-10,0.1*(t-toutL1));
			}
			if (xL2<=xmax-200 || xL2>=xmax-0) {
				xL2 = XL(xL2);
				yL2 = YP(yL2,-10,0.1*(t-toutL2));
			}
			// Loser condition
			if (yL1<=ymin) {
				n2=100;
			}
			if (yL2<=ymin) {
				n1=100;
			}
			
			// y-cordinate of the floor (sinusoidal motion)
			yF1 = YL(yoL1,AL1,wL1,t,dL1);
			yF2 = YL(yoL2,AL2,wL2,t,dL2);
			
			// Borders or limits for the launchers
			double xminL1=xL1-0.5*WL1, xmaxL1=xL1+0.5*WL1, yminL1=yL1-0.5*HL1, ymaxL1=yL1+0.5*HL1;
			double xminL2=xL2-0.5*WL2, xmaxL2=xL2+0.5*WL2, yminL2=yL2-0.5*HL2, ymaxL2=yL2+0.5*HL2;
			
			// Shootting and Set speed and angle of the particles
			if (StdDraw.mousePressed()==true) {
				// Shot player 1 and Set speed and angle of particle 1
				if (StdDraw.mouseX()>=xminB1 && StdDraw.mouseX()<=xmaxB1 && StdDraw.mouseY()>=yminB1 && StdDraw.mouseY()<=ymaxB1) {
					tclickP1 = t;
					xoP1     = xL1+36;
					yoP1     = yL1-13;
					go1      = 1;
				}
				if (StdDraw.mouseX()>=xoSpeedBar && StdDraw.mouseX()<=xfSpeedBar && StdDraw.mouseY()>=yoSpeedBar && StdDraw.mouseY()<=yfSpeedBar) {
					xMvP1 = StdDraw.mouseX();
					vP1   = xMvP1-xoSpeedBar;
				}
				if (StdDraw.mouseX()>=xoAngleBar && StdDraw.mouseX()<=xfAngleBar && StdDraw.mouseY()>=yoAngleBar && StdDraw.mouseY()<=yfAngleBar) {
					xMOP1 = StdDraw.mouseX();
					OP1   = ((xMOP1-xoAngleBar)/200.0)*(Math.PI/2.0);
				}
				// Shot player 2 and Set speed and angle of particle 2
				if (StdDraw.mouseX()>=xminB2 && StdDraw.mouseX()<=xmaxB2 && StdDraw.mouseY()>=yminB2 && StdDraw.mouseY()<=ymaxB2) {
					tclickP2 = t;
					xoP2     = xL2-35;
					yoP2     = yL2+13;
					go2      = 1;
				}
				if (StdDraw.mouseX()<=xmax-xoSpeedBar && StdDraw.mouseX()>=xmax-xfSpeedBar && StdDraw.mouseY()>=yoSpeedBar && StdDraw.mouseY()<=yfSpeedBar) {
					xMvP2 = xmax-StdDraw.mouseX();
					vP2   = xMvP2-xoSpeedBar;
				}
				if (StdDraw.mouseX()<=xmax-xoAngleBar && StdDraw.mouseX()>=xmax-xfAngleBar && StdDraw.mouseY()>=yoAngleBar && StdDraw.mouseY()<=yfAngleBar) {
					xMOP2 = xmax-StdDraw.mouseX();
					OP2   = Math.PI - ((xMOP2-xoAngleBar)/200.0)*(Math.PI/2.0);
				}
			
				// Speed and angle of the particles
				vxP1 = vP1*Math.cos(OP1);
				vyP1 = vP1*Math.sin(OP1);
				vxP2 = vP2*Math.cos(OP2);
				vyP2 = vP2*Math.sin(OP2);
			}
			
			// xy-cordinate of the particles in the launcher
			xP1 = xL1+36;
			yP1 = yL1-13;
			xP2 = xL2-35;
			yP2 = yL2+13;
			
			// xy-cordinate of the particles when the players shoots
			if (go1==1) {
				xP1 = XP(xoP1,vxP1,t-tclickP1);
				yP1 = YP(yoP1,vyP1,t-tclickP1);
			}
			if (go2==1) {
				// xy-cordinate of the particles
				xP2 = XP(xoP2,vxP2,t-tclickP2);
				yP2 = YP(yoP2,vyP2,t-tclickP2);
			}
			
			// Boundary conditions for the particles
			if (xP1<=xmin || xP1>=xmax || yP1<=ymin || yP1>=ymax) { // Go back to launcher
				go1=0;
			}
			if (xP2<=xmin || xP2>=xmax || yP2<=ymin || yP2>=ymax) { // Go back to launcher
				go2=0;
			}
			
			// Display tornado
			if (-Math.cos(waS*t)+0.1>=0) {
				
				// (x,y) position of the tornado (same as for the launchers, but x oscillates and y is fixed)
				xS = YL(xoS,AS,wS,t,0); // horizontal position describes harmonic oscillations
				yS = XL(yoS);           // vertical position is fixed
				
				// limits of the tornado
				double xminS=xS-0.5*WS, xmaxS=xS+0.5*WS, yminS=yS-0.5*HS, ymaxS=yS+0.5*HS;
			
				// draw tornado
				StdDraw.picture(0.5*(xminS+xmaxS), 0.5*(yminS+ymaxS), "images/tornado1.gif", 3*(xmaxS-xminS), 1.1*(ymaxS-yminS)+10, 0);
				//StdDraw.rectangle(0.5*(xminS+xmaxS), 0.5*(yminS+ymaxS), 0.5*(xmaxS-xminS), 0.5*(ymaxS-yminS));
				
				// Nonuniform motion of te particle
				if (xP1>=xmaxS-rP1 && xP1<=xmaxS-rP1+12 && yP1>=yminS && yP1<=ymaxS) {
					tclickP1 = t;
					xoP1     = xP1;
					yoP1     = yP1;
					vxP1     = -vxP1;
				}
				if (xP1<=xminS+rP1 && yP1>=yminS && yP1<=ymaxS && vxP1<0) {
					tclickP1 = t;
					xoP1     = xP1;
					yoP1     = yP1;
					vxP1     = -vxP1;
				}
				if (xP2<=xminS+rP2 && xP2>=xminS-12 && yP2>=yminS && yP2<=ymaxS) {
					tclickP2 = t;
					xoP2     = xP2;
					yoP2     = yP2;
					vxP2     = -vxP2;
				}
				if (xP2>=xmaxS-rP2 && yP2>=yminS && yP2<=ymaxS && vxP2>0) {
					tclickP2 = t;
					xoP2     = xP2;
					yoP2     = yP2;
					vxP2     = -vxP2;
				}
			}
			
			// Conditions for death
			if (xP1>=xminL2 && xP1<=xmaxL2 && yP1>=yminL2 && yP1<=ymaxL2) { // Go back to launcher
				xP1  = xL1;
				yP1  = yL1;
				vxP1 = Math.abs(vxP1);
				n1 += 3;
			}
			if (xP2>=xminL1 && xP2<=xmaxL1 && yP2>=yminL1 && yP2<=ymaxL1) { // Go back to launcher
				xP2  = xL2;
				yP2  = yL2;
				vxP2 = -Math.abs(vxP2);
				n2 += 3;
			}
			
			// Draw
				StdDraw.setPenColor(StdDraw.BLACK);
				//StdDraw.textRight(xmax+40, ymax+20, "" + (int)(t-to) + " s");
				StdDraw.textRight(xmax+40, ymin-25, "GAME by Andres J. Aguirre G.");
			// Speeds, Angles
				StdDraw.setPenColor(StdDraw.GREEN);
				// For V1
				double[] xV1 = { xoSpeedBar, xoSpeedBar, xMvP1, xMvP1 };
				double[] yV1 = { yoSpeedBar, yfSpeedBar, yfSpeedBar, yoSpeedBar };
				StdDraw.filledPolygon(xV1, yV1);
				// For O1
				double[] xO1 = { xoAngleBar, xoAngleBar, xMOP1, xMOP1 };
				double[] yO1 = { yoAngleBar, yfAngleBar, yfAngleBar, yoAngleBar };
				StdDraw.filledPolygon(xO1, yO1);
				// For V2
				double[] xV2 = { xmax-xoSpeedBar, xmax-xoSpeedBar, xmax-xMvP2, xmax-xMvP2 };
				double[] yV2 = { yoSpeedBar, yfSpeedBar, yfSpeedBar, yoSpeedBar };
				StdDraw.filledPolygon(xV2, yV2);
				// For O2
				double[] xO2 = { xmax-xoAngleBar, xmax-xoAngleBar, xmax-xMOP2, xmax-xMOP2 };
				double[] yO2 = { yoAngleBar, yfAngleBar, yfAngleBar, yoAngleBar };
				StdDraw.filledPolygon(xO2, yO2);
			// Borders for the speed and angle bars
				StdDraw.setPenColor(StdDraw.BLACK);
				double[] xSpeedBar1 = { xoSpeedBar, xoSpeedBar, xfSpeedBar, xfSpeedBar };
				double[] xAngleBar1 = { xoAngleBar, xoAngleBar, xfAngleBar, xfAngleBar };
				double[] xSpeedBar2 = { xmax-xoSpeedBar, xmax-xoSpeedBar, xmax-xfSpeedBar, xmax-xfSpeedBar };
				double[] xAngleBar2 = { xmax-xoAngleBar, xmax-xoAngleBar, xmax-xfAngleBar, xmax-xfAngleBar };
				// For V1
				StdDraw.polygon(xSpeedBar1, yV1);
				// For O1
				StdDraw.polygon(xAngleBar1, yO1);
				// For V2
				StdDraw.polygon(xSpeedBar2, yV2);
				// For O2
				StdDraw.polygon(xAngleBar2, yO2);
			/* Labels
				// 1
				StdDraw.text(xmin-30, 0.5*(yoSpeedBar+yfSpeedBar), (int)vP1 + " m/s");
				StdDraw.text(xmin-30, 0.5*(yoAngleBar+yfAngleBar), (int)(OP1*(180/Math.PI)) + " grades");
				// 2
				StdDraw.text(xmax+30, 0.5*(yoSpeedBar+yfSpeedBar), (int)vP2 + " m/s");
				StdDraw.text(xmax+30, 0.5*(yoAngleBar+yfAngleBar), (int)((Math.PI-OP2)*(180/Math.PI)) + " grades");
				*/
				StdDraw.picture(30, 30, "images/Labels.png", 25, 45);
				StdDraw.picture(xmax-30, 30, "images/Labels.png", 25, 45);
			// Buttons
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.filledRectangle(0.5*(xminB1+xmaxB1), 0.5*(yminB1+ymaxB1), 0.5*(xmaxB1-xminB1), 0.5*(ymaxB1-yminB1));
				StdDraw.filledRectangle(0.5*(xminB2+xmaxB2), 0.5*(yminB2+ymaxB2), 0.5*(xmaxB2-xminB2), 0.5*(ymaxB2-yminB2));
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0.5*(xminB1+xmaxB1), 0.5*(yminB1+ymaxB1), "Fire!");
				StdDraw.text(0.5*(xminB2+xmaxB2), 0.5*(yminB2+ymaxB2), "Fire!");
			// Floor (rocks)
				// 1
				StdDraw.picture(xF1, yF1-60, "images/Floor.jpg", 220, 80, 5);
				// 2
				StdDraw.picture(xF2, yF2-55, "images/Floor.jpg", 220, 80, 5);
			// Particles
				StdDraw.setPenColor(StdDraw.YELLOW);
				StdDraw.filledCircle(xP1, yP1, rP1);
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.filledCircle(xP2, yP2, rP2);
			// Launchers
				StdDraw.setPenColor(StdDraw.BLACK);
				// 1
				StdDraw.picture(0.5*(xminL1+xmaxL1), 0.5*(yminL1+ymaxL1), "images/Arm.gif", 80, ymaxL1-yminL1, 0);
				//StdDraw.rectangle(0.5*(xminL1+xmaxL1), 0.5*(yminL1+ymaxL1), 0.5*(xmaxL1-xminL1), 0.5*(ymaxL1-yminL1));
				// 2
				StdDraw.picture(0.5*(xminL2+xmaxL2), 0.5*(yminL2+ymaxL2), "images/BigFoot.gif", 80, ymaxL2-yminL2, 0);
				//StdDraw.rectangle(0.5*(xminL2+xmaxL2), 0.5*(yminL2+ymaxL2), 0.5*(xmaxL2-xminL2), 0.5*(ymaxL2-yminL2));
			// Life bars
				StdDraw.setPenColor(StdDraw.RED);
				// 1
				double[] xLB1 = { 20, life-life*(n2/100.0)+20, life-life*(n2/100.0)+20, 20 };
				double[] yLB1 = { ymax-20, ymax-20, ymax-30, ymax-30 };
				StdDraw.filledPolygon(xLB1, yLB1);
				// 2
				double[] xLB2 = { xmax-20, xmax-(life-life*(n1/100.0))-20, xmax-(life-life*(n1/100.0))-20, xmax-20 };
				double[] yLB2 = { ymax-20, ymax-20, ymax-30, ymax-30 };
				StdDraw.filledPolygon(xLB2, yLB2);
			// Percent of life
				StdDraw.textLeft(20, ymax-40, " " + (int)(100-n2) + "%");
				StdDraw.textRight(xmax-20, ymax-40, " " + (int)(100-n1) + "%");
			
			// Winner
			if (n1>=100) { // Wins player 1
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledRectangle(0.75*(xmin+xmax), 0.5*(ymin+ymax), 0.25*(xmax-xmin), 0.5*(ymax-ymin));
				StdDraw.text(0.25*(xmin+xmax), 0.5*(ymin+ymax), "Winner!");
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0.75*(xmin+xmax), 0.5*(ymin+ymax), "Loser!");
				t = -1;
			}
			if (n2>=100) { // Wins player 2
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledRectangle(0.25*(xmin+xmax), 0.5*(ymin+ymax), 0.25*(xmax-xmin), 0.5*(ymax-ymin));
				StdDraw.text(0.75*(xmin+xmax), 0.5*(ymin+ymax), "Winner!");
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0.25*(xmin+xmax), 0.5*(ymin+ymax), "Loser!");
				t = -1;
			}
			
			t+=dt;
			StdDraw.show(0);
		
		}
	}
	
		
	// x component of the motion of the particles
	public static double XP(double xo, double vx, double t) {
		double x;
		x = xo + vx*t;
		return x;
	}
	
	// y component of the motion of the particles
	public static double YP(double yo, double vy, double t) {
		double y;
		y = yo + vy*t + 0.5*a*t*t;
		return y;
	}
	
	// x component of the motion of the launchers
	public static double XL(double xoL) {
		double xL;
		xL = xoL;
		return xL;
	}
	
	// y component of the motion of the launchers
	public static double YL(double yoL, double A, double w, double t, double dL) {
		double yL;
		yL = yoL + A*Math.sin(w*t + dL);
		return yL;
	}
}

