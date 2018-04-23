import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BookingHistoryComponent } from './booking-history.component';
import { BookingHistoryDetailComponent } from './booking-history-detail.component';
import { BookingHistoryPopupComponent } from './booking-history-dialog.component';
import { BookingHistoryDeletePopupComponent } from './booking-history-delete-dialog.component';

@Injectable()
export class BookingResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const bookingHistoryRoute: Routes = [
    {
        path: 'booking-history',
        component: BookingHistoryComponent,
        resolve: {
            'pagingParams': BookingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'booking-history/:id',
        component: BookingHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookingPopupRoute: Routes = [
    /*{
        path: 'booking-new',
        component: BookingPopupComponent,
        data: {
            authorities: ['ROLE_USER',  'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'booking-history/:id/approve',
        component: BookingHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER',  'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'booking-history/:id/reject',
        component: BookingHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER',  'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }/*,
    {
        path: 'booking/:id/approve',
        component: BookingHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER',  'ROLE_ADMIN'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }*/
];
