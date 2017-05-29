package mcl;

import java.awt.Point;

import javax.swing.JButton;

import lejos.robotics.mapping.NavigationPanel;

public class ConfigDetails extends NavigationPanel {
	protected static final long serialVersionUID = 1L;

	protected static final int FRAME_WIDTH = 1000;
	protected static final int FRAME_HEIGHT = 800;
	protected static final int NUM_PARTICLES = 150;
	protected static final String TITLE = "MCL Test";
	protected static final int INITIAL_ZOOM = 150;
	protected static final Point INITIAL_VIEW_START = new Point(-150,-30);

	protected static final JButton newRefButton = new JButton("Procurar novas Referências");
	protected static final JButton getPose = new JButton("Verificar Posição");
	protected static final JButton getDesvio = new JButton("Desvio Padrão do Set");
	protected static final String mapFileName = "map.svg";
}
