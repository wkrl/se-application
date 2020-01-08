package fxgui;

import application.components.ComponentBase;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;


/**
 * Internal interface for FXML controllers.
 *
 */
interface FXControllerIntf {

	public void inject( final ComponentBase component );

	public void inject( final Tab parentTab );

	public AnchorPane getAnchorPane();

	public void start();

}
