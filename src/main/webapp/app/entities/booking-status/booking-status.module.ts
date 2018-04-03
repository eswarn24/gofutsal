import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GofutsalSharedModule } from '../../shared';
import {
    BookingStatusService,
    BookingStatusPopupService,
    BookingStatusComponent,
    BookingStatusDetailComponent,
    BookingStatusDialogComponent,
    BookingStatusPopupComponent,
    BookingStatusDeletePopupComponent,
    BookingStatusDeleteDialogComponent,
    bookingStatusRoute,
    bookingStatusPopupRoute,
} from './';

const ENTITY_STATES = [
    ...bookingStatusRoute,
    ...bookingStatusPopupRoute,
];

@NgModule({
    imports: [
        GofutsalSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BookingStatusComponent,
        BookingStatusDetailComponent,
        BookingStatusDialogComponent,
        BookingStatusDeleteDialogComponent,
        BookingStatusPopupComponent,
        BookingStatusDeletePopupComponent,
    ],
    entryComponents: [
        BookingStatusComponent,
        BookingStatusDialogComponent,
        BookingStatusPopupComponent,
        BookingStatusDeleteDialogComponent,
        BookingStatusDeletePopupComponent,
    ],
    providers: [
        BookingStatusService,
        BookingStatusPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalBookingStatusModule {}
