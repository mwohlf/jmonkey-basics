package net.wohlfart.ui;

import java.util.List;

import net.wohlfart.IStateContext;
import net.wohlfart.events.DoSelection;
import net.wohlfart.events.LifecycleCreate;
import net.wohlfart.events.LifecycleDestroy;
import net.wohlfart.events.UndoSelection;
import net.wohlfart.events.UpdateEvent;
import net.wohlfart.model.planets.ICelestial;
import net.wohlfart.user.IAvatar;

import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.generics.TypeReference;

public class StellarScreenPresenter extends AbstractScreenPresenter<StellarScreen, StellarScreenPresenter>  {

    // model data ----
    protected IAvatar avatar;

    // need a handler to the subscriber so they don't get gc'ed
    protected final EventSubscriber<LifecycleCreate<IAvatar>> avatarCreate;
    protected final EventSubscriber<LifecycleDestroy<IAvatar>> avatarDestroy;

    protected final EventSubscriber<DoSelection<ICelestial>> planetSelect;
    protected final EventSubscriber<UndoSelection<ICelestial>> planetUnselect;

    protected final EventSubscriber<UpdateEvent<List<ICelestial>>> planetListUpdate;


    // this constructor is never called!
    public StellarScreenPresenter(final StellarScreen view, final IStateContext context) {
        super(view, context);

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
        planetSelect = new EventSubscriber<DoSelection<ICelestial>>() {
            @Override
            public void onEvent(DoSelection<ICelestial> event) {
                getView().setSelectedPlanet(event.get());
            }
        };
        planetUnselect = new EventSubscriber<UndoSelection<ICelestial>>() {
            @Override
            public void onEvent(UndoSelection<ICelestial> event) {
                // TODO
            }
        };
        planetListUpdate = new EventSubscriber<UpdateEvent<List<ICelestial>>>() {
            @Override
            public void onEvent(UpdateEvent<List<ICelestial>> event) {
                getView().setCelestialList(event.get());
            }
        };
    }


    @Override
    public void startup() {
        super.startup();
        eventBus.subscribe(new TypeReference<LifecycleCreate<IAvatar>>() {}.getType(), avatarCreate);
        eventBus.subscribe(new TypeReference<LifecycleDestroy<IAvatar>>() {}.getType(), avatarDestroy);
        eventBus.subscribe(new TypeReference<DoSelection<ICelestial>>() {}.getType(), planetSelect);
        eventBus.subscribe(new TypeReference<UndoSelection<ICelestial>>() {}.getType(), planetUnselect);
        eventBus.subscribe(new TypeReference<UpdateEvent<List<ICelestial>>>() {}.getType(), planetListUpdate);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        eventBus.unsubscribe(LifecycleCreate.class, avatarCreate);
        eventBus.unsubscribe(LifecycleDestroy.class, avatarDestroy);
        eventBus.unsubscribe(DoSelection.class, planetSelect);
        eventBus.unsubscribe(UndoSelection.class, planetUnselect);
        eventBus.unsubscribe(UpdateEvent.class, planetListUpdate);
    }





}
