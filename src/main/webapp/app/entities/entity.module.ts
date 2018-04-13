import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GofutsalCourtTypeModule } from './court-type/court-type.module';
import { GofutsalCourtLocationModule } from './court-location/court-location.module';
import { GofutsalCourtModule } from './court/court.module';
import { GofutsalBookingModule } from './booking/booking.module';

import { GofutsalBookingStatusModule } from './booking-status/booking-status.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GofutsalCourtTypeModule,
        GofutsalCourtLocationModule,
        GofutsalCourtModule,
        GofutsalBookingModule,
        GofutsalBookingStatusModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalEntityModule {}
