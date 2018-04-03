import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { BookingStatusComponent } from './booking-status.component';
import { BookingStatusDetailComponent } from './booking-status-detail.component';
import { BookingStatusPopupComponent } from './booking-status-dialog.component';
import { BookingStatusDeletePopupComponent } from './booking-status-delete-dialog.component';

export const bookingStatusRoute: Routes = [
    {
        path: 'booking-status',
        component: BookingStatusComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingStatuses'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'booking-status/:id',
        component: BookingStatusDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingStatuses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookingStatusPopupRoute: Routes = [
    {
        path: 'booking-status-new',
        component: BookingStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingStatuses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'booking-status/:id/edit',
        component: BookingStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingStatuses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'booking-status/:id/delete',
        component: BookingStatusDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingStatuses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
