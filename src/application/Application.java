package application;

import static application.AppConfigurator.LoggerTopics;

import java.util.concurrent.CountDownLatch;

import application.components.BuilderIntf;
import application.components.ComponentBase;
import application.components.ComponentIntf;
import application.components.RunnerIntf;
import application.repository.RepositoryBuilder;
import fxgui.FXInterface;
import misc.Logger;


/**
 * Application class containing Java's main() method. Class executes the main
 * workflow for building, running and terminating the application and all of
 * its components.
 * 
 * @author sgra64
 *
 */
public class Application {
	private static Logger logger = Logger.getInstance( Application.class );


	/**
	 * Java's main() method.
	 * 
	 * @param args arguments passed from invoking command.
	 */
	public static void main( final String ... args ) {

		CountDownLatch waitForExit = new CountDownLatch( 2 );

		/*
		 * RepositoryBuilder is a Spring @Component that builds repositories.
		 */
		final RepositoryBuilder repositoryBuilder = RepositoryBuilder.getInstance();

		/*
		 * AppBuilder builds applications components and returns a startable Runner instance.
		 */
		AppBuilder appBuilder = AppBuilder.getInstance();
		appBuilder.inject( repositoryBuilder );

		RunnerIntf appRunner = appBuilder.build();

		/*
		 * Use (hidden) JavaFX Builder to build the JavaFX GUI also returning a startable FXRunner instance.
		 */
		BuilderIntf fxBuilder = FXInterface.getBuilder();
		FXInterface.FXRunnerIntf gui = (FXInterface.FXRunnerIntf)fxBuilder.build();

		ComponentBase appComponent = appBuilder.getComponent( 0 );	// first component
		String appName = appComponent.getName();

		/*
		 * Start AppRunner instance.
		 */
		appRunner.start(

			onStart -> {
				logger.log( LoggerTopics.Info, appName + " starting..." );

				repositoryBuilder.startup();

				/*
				 * During AppRunner-start, the JavaFX GUI is started. The JavaFX GUI launches
				 * with its own thread(s) such that invoking main thread will run through fast
				 * and needs to wait for application exit at the waitForExit latch at the end
				 * before leaving the main function terminating the JVM process.
				 */
				gui.start(

					onGUIStart -> {
						logger.log( LoggerTopics.Startup, "JavaFX-GUI." );

						/*
						 * Build JavaFX GUI elements (Tabs) for each application component.
						 */
						appBuilder.iterateComponents( component -> {
							gui.injectView( component );	// inject component into JavaFX GUI.
							ComponentBase.<ComponentIntf.LogicIntf>logicIntf( component, logic -> {
								logic.startup();	// start component logic after linked with GUI.
							});
						});
					},

					onGUIExit -> {
						appRunner.exit( onGUIExit );		// Propagate exit from GUI to AppRunner instance.
					},

					onGUIError -> {
						appRunner.error( onGUIError );	// Propagate GUI error to AppRunner instance.
					}
				);

				gui.show( 1 );	// Select i-th Tab as visible.
			},

			/*
			 * Called to exit AppRunner instance, e.g. from gui.onGUIExit: app.exit(msg).
			 */
			onExit -> {
				System.out.println( onExit + "\n" + "shutting down components..." );
				AppBuilder.getInstance().iterateComponentsReverseOrder( component -> {
					ComponentBase.<ComponentIntf.LogicIntf>logicIntf( component, logic -> {
						logic.shutdown();
					});
				});

				gui.exitPlatform( onExit2 -> {
					logger.log( LoggerTopics.Shutdown, "JavaFX-GUI." );
					waitForExit.countDown();	// unblock main-thread to leave main()
				});

				waitForExit.countDown();	// unblock main-thread to leave main()

			},

			/*
			 * Called to report error during AppRunner execution.
			 */
			onError -> {
				logger.log( LoggerTopics.Error, Application.class.getName() + ".( " + onError + " )." );
				appRunner.exit( onError );
			}
		);

		/*
		 * Make main-thread wait for AppRunner's onExit before leaving the main() function
		 * terminating the JVM process.
		 */
		try {
			logger.log( LoggerTopics.Info, appName + " running..." );
			waitForExit.await();

			repositoryBuilder.shutdown();

		} catch( InterruptedException e ) {

		} finally {
			logger.log( LoggerTopics.Info, appName + " exited." );
		}
	}

}
