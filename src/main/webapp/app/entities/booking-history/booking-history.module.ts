import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GofutsalSharedModule } from '../../shared';
import {
    BookingHistoryService,
    BookingHistoryPopupService,
    BookingHistoryComponent,
 /*   BookingHistoryDetailComponent,*/
    BookingHistoryDialogComponent,
    BookingHistoryPopupComponent,
    BookingHistoryDeletePopupComponent,
    BookingHistoryDeleteDialogComponent,
    bookingHistoryRoute,
    bookingPopupRoute,
    BookingResolvePagingParams,
} from './index';
import {BookingHistoryDetailComponent} from "./booking-history-detail.component";

const ENTITY_STATES = [
    ...bookingHistoryRoute,
    ...bookingPopupRoute,
];

@NgModule({
    imports: [
        GofutsalSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BookingHistoryComponent,
        BookingHistoryDetailComponent,
        BookingHistoryDialogComponent,
        BookingHistoryDeleteDialogComponent,
        BookingHistoryPopupComponent,
        BookingHistoryDeletePopupComponent,
    ],
    entryComponents: [
        BookingHistoryComponent,
        BookingHistoryDialogComponent,
        BookingHistoryPopupComponent,
        BookingHistoryDeleteDialogComponent,
        BookingHistoryDeletePopupComponent,
    ],
    providers: [
        BookingHistoryService,
        BookingHistoryPopupService,
        BookingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalBookingHistoryModule {}
