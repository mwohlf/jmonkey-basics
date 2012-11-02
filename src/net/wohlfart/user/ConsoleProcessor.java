package net.wohlfart.user;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.screen.Screen;

public class ConsoleProcessor {


	public void init(Screen screen, Nifty nifty) {
		// get the console control (this assumes that there is a console in the current screen with the id="console"
		Console console = screen.findNiftyControl("console", Console.class);

		// output hello to the console
		console.output("Hello :)");

		// create the console commands class and attach it to the console
		ConsoleCommands consoleCommands = new ConsoleCommands(nifty, console);

		// create a simple command (see below for implementation) this class will be called when the command is detected
		// and register the command as a command with the console
		ConsoleCommand simpleCommand = new SimpleCommand();
		consoleCommands.registerCommand("simple", simpleCommand);

		// create another command (this time we can even register arguments with nifty so that the command completion will work with arguments too)
		ConsoleCommand showCommand = new ShowCommand();
		consoleCommands.registerCommand("show a", showCommand);
		consoleCommands.registerCommand("show b", showCommand);
		consoleCommands.registerCommand("show c", showCommand);

		// finally enable command completion
		consoleCommands.enableCommandCompletion(true);

		////////////////////////
	}


	private class SimpleCommand implements ConsoleCommand {
		@Override
		public void execute(final String[] args) {
			System.out.println(args[0]); // this is always the command (in this case 'simple')
			if (args.length > 1) {
				for (String a : args) {
					System.out.println(a);
				}
			}
		}
	}

	private class ShowCommand implements ConsoleCommand {
		@Override
		public void execute(final String[] args) {
			System.out.println(args[0] + " " + args[1]);
		}
	}


}
