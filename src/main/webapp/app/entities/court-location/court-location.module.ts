import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GofutsalSharedModule } from '../../shared';
import {
    CourtLocationService,
    CourtLocationPopupService,
    CourtLocationComponent,
    CourtLocationDetailComponent,
    CourtLocationDialogComponent,
    CourtLocationPopupComponent,
    CourtLocationDeletePopupComponent,
    CourtLocationDeleteDialogComponent,
    courtLocationRoute,
    courtLocationPopupRoute,
    CourtLocationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...courtLocationRoute,
    ...courtLocationPopupRoute,
];

@NgModule({
    imports: [
        GofutsalSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CourtLocationComponent,
        CourtLocationDetailComponent,
        CourtLocationDialogComponent,
        CourtLocationDeleteDialogComponent,
        CourtLocationPopupComponent,
        CourtLocationDeletePopupComponent,
    ],
    entryComponents: [
        CourtLocationComponent,
        CourtLocationDialogComponent,
        CourtLocationPopupComponent,
        CourtLocationDeleteDialogComponent,
        CourtLocationDeletePopupComponent,
    ],
    providers: [
        CourtLocationService,
        CourtLocationPopupService,
        CourtLocationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalCourtLocationModule {}
