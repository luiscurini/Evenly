package com.luisrubenrodriguez.evenly.deps;

import com.luisrubenrodriguez.evenly.main_activity.MainActivity;
import com.luisrubenrodriguez.evenly.service.FoursquareAPIModule;
import com.luisrubenrodriguez.evenly.venue_activity.VenueActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by GamingMonster on 22.05.2017.
 * Component for Dagger2
 */
@Singleton
@Component(modules = {FoursquareAPIModule.class})
public interface Dependencies {
    void inject(MainActivity mainActivity);
    void inject(VenueActivity venueActivity);
}
