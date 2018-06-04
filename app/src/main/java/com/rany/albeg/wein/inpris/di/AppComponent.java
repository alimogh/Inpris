package com.rany.albeg.wein.inpris.di;

import com.rany.albeg.wein.inpris.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
@Singleton
@Component(modules = { AndroidSupportInjectionModule.class, AppModule.class, BuildersModule.class })
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    void inject(App application);
}
