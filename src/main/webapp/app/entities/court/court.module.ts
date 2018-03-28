import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GofutsalSharedModule } from '../../shared';
import {
    CourtService,
    CourtPopupService,
    CourtComponent,
    CourtDetailComponent,
    CourtDialogComponent,
    CourtPopupComponent,
    CourtDeletePopupComponent,
    CourtDeleteDialogComponent,
    courtRoute,
    courtPopupRoute,
    CourtResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...courtRoute,
    ...courtPopupRoute,
];

@NgModule({
    imports: [
        GofutsalSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CourtComponent,
        CourtDetailComponent,
        CourtDialogComponent,
        CourtDeleteDialogComponent,
        CourtPopupComponent,
        CourtDeletePopupComponent,
    ],
    entryComponents: [
        CourtComponent,
        CourtDialogComponent,
        CourtPopupComponent,
        CourtDeleteDialogComponent,
        CourtDeletePopupComponent,
    ],
    providers: [
        CourtService,
        CourtPopupService,
        CourtResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalCourtModule {}
