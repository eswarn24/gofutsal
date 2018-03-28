import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CourtComponent } from './court.component';
import { CourtDetailComponent } from './court-detail.component';
import { CourtPopupComponent } from './court-dialog.component';
import { CourtDeletePopupComponent } from './court-delete-dialog.component';

@Injectable()
export class CourtResolvePagingParams implements Resolve<any> {

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

export const courtRoute: Routes = [
    {
        path: 'court',
        component: CourtComponent,
        resolve: {
            'pagingParams': CourtResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Courts'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'court/:id',
        component: CourtDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Courts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courtPopupRoute: Routes = [
    {
        path: 'court-new',
        component: CourtPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Courts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court/:id/edit',
        component: CourtPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Courts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'court/:id/delete',
        component: CourtDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Courts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
