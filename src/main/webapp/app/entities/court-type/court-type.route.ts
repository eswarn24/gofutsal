import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CourtTypeComponent } from './court-type.component';
import { CourtTypeDetailComponent } from './court-type-detail.component';
import { CourtTypePopupComponent } from './court-type-dialog.component';
import { CourtTypeDeletePopupComponent } from './court-type-delete-dialog.component';

export const courtTypeRoute: Routes = [
    {
        path: 'court-type',
        component: CourtTypeComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'CourtTypes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'court-type/:id',
        component: CourtTypeDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'CourtTypes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courtTypePopupRoute: Routes = [
    {
        path: 'court-type-new',
        component: CourtTypePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'CourtTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court-type/:id/edit',
        component: CourtTypePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'CourtTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court-type/:id/delete',
        component: CourtTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'CourtTypes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
