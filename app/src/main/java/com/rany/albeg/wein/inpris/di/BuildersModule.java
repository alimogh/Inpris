package com.rany.albeg.wein.inpris.di;

import com.rany.albeg.wein.inpris.read.ReadActivity;
import com.rany.albeg.wein.inpris.write.WriteActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector()
    abstract WriteActivity bindPlacesActivity();
    @ContributesAndroidInjector()
    abstract ReadActivity bindReadActivity();
}