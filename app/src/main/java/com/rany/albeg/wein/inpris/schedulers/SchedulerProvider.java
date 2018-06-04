package com.rany.albeg.wein.inpris.schedulers;

import io.reactivex.Scheduler;

/**
 * This file is a part of Inpris project.
 *
 * @author Rany Albeg Wein
 * @version 1.0.0
 * @since 03/06/2018
 */
public interface SchedulerProvider {

    Scheduler computation();

    Scheduler ui();

    Scheduler io();
}
