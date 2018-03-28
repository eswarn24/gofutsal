import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GofutsalSharedModule } from '../../shared';
import {
    CourtTypeService,
    CourtTypePopupService,
    CourtTypeComponent,
    CourtTypeDetailComponent,
    CourtTypeDialogComponent,
    CourtTypePopupComponent,
    CourtTypeDeletePopupComponent,
    CourtTypeDeleteDialogComponent,
    courtTypeRoute,
    courtTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...courtTypeRoute,
    ...courtTypePopupRoute,
];

@NgModule({
    imports: [
        GofutsalSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CourtTypeComponent,
        CourtTypeDetailComponent,
        CourtTypeDialogComponent,
        CourtTypeDeleteDialogComponent,
        CourtTypePopupComponent,
        CourtTypeDeletePopupComponent,
    ],
    entryComponents: [
        CourtTypeComponent,
        CourtTypeDialogComponent,
        CourtTypePopupComponent,
        CourtTypeDeleteDialogComponent,
        CourtTypeDeletePopupComponent,
    ],
    providers: [
        CourtTypeService,
        CourtTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GofutsalCourtTypeModule {}
