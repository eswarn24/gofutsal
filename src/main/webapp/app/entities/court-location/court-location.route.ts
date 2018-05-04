import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CourtLocationComponent } from './court-location.component';
import { CourtLocationDetailComponent } from './court-location-detail.component';
import { CourtLocationPopupComponent } from './court-location-dialog.component';
import { CourtLocationDeletePopupComponent } from './court-location-delete-dialog.component';

@Injectable()
export class CourtLocationResolvePagingParams implements Resolve<any> {

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

export const courtLocationRoute: Routes = [
    {
        path: 'court-location',
        component: CourtLocationComponent,
        resolve: {
            'pagingParams': CourtLocationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'],
            pageTitle: 'CourtLocations'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'court-location/:id',
        component: CourtLocationDetailComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'],
            pageTitle: 'CourtLocations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courtLocationPopupRoute: Routes = [
    {
        path: 'court-location-new',
        component: CourtLocationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'],
            pageTitle: 'CourtLocations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court-location/:id/edit',
        component: CourtLocationPopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'],
            pageTitle: 'CourtLocations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court-location/:id/delete',
        component: CourtLocationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER'],
            pageTitle: 'CourtLocations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
