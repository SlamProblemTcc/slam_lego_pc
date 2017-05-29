package mcl;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;
import lejos.robotics.mapping.MenuAction;
import lejos.robotics.mapping.NavigationModel.NavEvent;
import lejos.robotics.RangeReading;
import lejos.robotics.localization.*;

/**
 * Localization - Class that process and control NXT Robot.
 * @author Rafael Fazzolino
 *
 */
public class Localization extends ConfigDetails {
	
	private static final long serialVersionUID = 1L;
	
	private static MCLPoseProvider mcl;
  
  	/**
   	* Create a Frame with options to connect to the NXT.
   	*/
  	public static void main(String[] args) throws Exception {
  		(new Localization()).run();
  	}
  	
  	/*Constructor that create a Panel that contains all options
  	 * to use MCL with NXT.*/
  	public Localization() {
  		buildGUI();
  	}
  
  	/**
  	 * Create the GUI
  	 */
  	@Override
  	protected void buildGUI() {		
	    super.buildGUI();
	    
	    /* Adding buttons get Pose and New References */
		commandPanel.add(getPose);
		commandPanel.add(newRefButton);
		commandPanel.add(getDesvio);
		
		/* Until connected, the buttons must be disable */
		getPose.setEnabled(false);
		newRefButton.setEnabled(false);
		getDesvio.setEnabled(false);
	
		/* When Get pose is pressed, we call the MCL Pose
		* to read the environment and get the actual pose.
		*/
		getPose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				/* Get the actual pose, reading the environment. */
				model.getPose();
				/* Get actual particles */
				model.getRemoteParticles();
				/* Estimated the actual pose */
				model.getEstimatedPose();
				
				//System.out.println("Max weight:" + model.getParticles().getMaxWeight());
				/* Get readings of the particles */
				model.getRemoteReadings();
				
				/* Disable the button */
				getPose.setEnabled(false);
			}
		});
		
		/* When the New Reference button is pressed: */
		newRefButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				/* Make a random move */
				model.randomMove();
				/* Get the new particles */
				model.getRemoteParticles();
			}
		});
		
		/* When the get Standard Deviation button is pressed: */
		getDesvio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				/* Print Standard Deviation */
				System.out.println("Erro em X: " + model.getMCL().getXRange());
				System.out.println("Erro em Y: " + model.getMCL().getYRange());
				System.out.print("Real Frente: "); 
				float read = model.getReadings().get(1).getRange();
				System.out.println(read);
			}
		});
  	}
  
  	/**
  	 * When the mouse is clicked in the map
  	 */
  	@Override
	protected void popupMenuItems(Point p, JPopupMenu menu) { 
	    /* Get information of the particle closest to the local clicked */
	    menu.add(new MenuAction(NavEvent.FIND_CLOSEST, "Informações da Partícula", p, model, this));
	}
	
	/**
	 * When an information is received from the robot
	 */
	@Override
	public void eventReceived(NavEvent navEvent) {
		/* If the estimated pose has been sent, we can enable the getPose button*/
		if (navEvent == NavEvent.ESTIMATED_POSE) {
			getPose.setEnabled(true);
		}
	}
	
	/**
	 * When the connection is OK
	 */
	@Override
	public void whenConnected() {
		/* Enable debug mode */
		model.setDebug(true);
		/* Load the map and generate the first particles sending both to the robot */
		model.loadMap(mapFileName);
		zoomSlider.setValue(INITIAL_ZOOM);
		mapPanel.viewStart = INITIAL_VIEW_START;
		model.generateParticles();
		
		/* Enable buttons */
		getPose.setEnabled(true);
		newRefButton.setEnabled(true);
		getDesvio.setEnabled(true);
	}
	
	/**
	 * Execute the program
	 */
	public void run() throws Exception {
		/* Instantiate the MCLPoseProvider */
		mcl = new MCLPoseProvider(null,NUM_PARTICLES,0);
		
		/* Adding the MCLPoseProvider into the model */
		model.setMCL(mcl);
		
		/* Open it in a JFrame window */
	    openInJFrame(this, FRAME_WIDTH, FRAME_HEIGHT, TITLE, Color.white);
	}
}