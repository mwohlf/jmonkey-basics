package net.wohlfart.ui;

import net.wohlfart.events.DoSelection;
import net.wohlfart.events.LifecycleCreate;
import net.wohlfart.events.LifecycleDestroy;
import net.wohlfart.events.UndoSelection;
import net.wohlfart.model.planets.IPlanet;
import net.wohlfart.user.IAvatar;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.generics.TypeReference;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class StellarScreenController implements ScreenController {
    protected final EventService eventBus;
    protected IAvatar avatar;

    protected Screen screen;
    protected Nifty nifty;

    // the label with the selected planets name
    protected Label planetLabel;

    // need a handler to the subscriber so they don't get gc'ed
    protected EventSubscriber<LifecycleCreate<IAvatar>> avatarCreate;
    protected EventSubscriber<LifecycleDestroy<IAvatar>> avatarDestroy;
    protected EventSubscriber<DoSelection<IPlanet>> planetSelect;
    protected EventSubscriber<UndoSelection<IPlanet>> planetUnselect;

    // this constructor is never called!
    public StellarScreenController() {
        eventBus = EventServiceLocator.getEventBusService();
        avatarCreate = new EventSubscriber<LifecycleCreate<IAvatar>>() {
            @Override
            public void onEvent(LifecycleCreate<IAvatar> event) {
                // TODO
            }
        };
        avatarDestroy = new EventSubscriber<LifecycleDestroy<IAvatar>>() {
            @Override
            public void onEvent(LifecycleDestroy<IAvatar> event) {
                // TODO
            }
        };
        planetSelect = new EventSubscriber<DoSelection<IPlanet>>() {
            @Override
            public void onEvent(DoSelection<IPlanet> event) {
                setSelectedPlanet(event.get());
            }
        };
        planetUnselect = new EventSubscriber<UndoSelection<IPlanet>>() {
            @Override
            public void onEvent(UndoSelection<IPlanet> event) {
                // TODO
            }
        };
        eventBus.subscribe(new TypeReference<LifecycleCreate<IAvatar>>() {
        }.getType(), avatarCreate);
        eventBus.subscribe(new TypeReference<LifecycleDestroy<IAvatar>>() {
        }.getType(), avatarDestroy);
        eventBus.subscribe(new TypeReference<DoSelection<IPlanet>>() {
        }.getType(), planetSelect);
        eventBus.subscribe(new TypeReference<UndoSelection<IPlanet>>() {
        }.getType(), planetUnselect);
    }

    @Override
    public void bind(final Nifty nifty, final Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        Window window = screen.findNiftyControl("window-1", Window.class);
        window.setTitle("Selected Planet");
        planetLabel = screen.findNiftyControl("window-1-text", Label.class);
        planetLabel.setText("- none -");
        
        final Console console = screen.findNiftyControl("console", Console.class);
        console.output("Hello :)");
        ConsoleCommands consoleCommands = new ConsoleCommands(nifty, console);

        ConsoleCommand simpleCommand = new ConsoleCommand() {
            @Override
            public void execute(String[] arg0) {
                console.output("jo");
            }};
        consoleCommands.registerCommand("hello", simpleCommand);

    }

    protected void setSelectedPlanet(final IPlanet planet) {
        if (planet != null) {
            planetLabel.setText(planet.getName());
        } else {
            planetLabel.setText("- none -");
        }
    }

    public void onWindowClick() {
        // TODO
    }

    @Override
    public void onEndScreen() {
        // TODO
    }

}
