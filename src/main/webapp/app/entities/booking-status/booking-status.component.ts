import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BookingStatus } from './booking-status.model';
import { BookingStatusService } from './booking-status.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-booking-status',
    templateUrl: './booking-status.component.html'
})
export class BookingStatusComponent implements OnInit, OnDestroy {
bookingStatuses: BookingStatus[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private bookingStatusService: BookingStatusService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.bookingStatusService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<BookingStatus[]>) => this.bookingStatuses = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.bookingStatusService.query().subscribe(
            (res: HttpResponse<BookingStatus[]>) => {
                this.bookingStatuses = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInBookingStatuses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: BookingStatus) {
        return item.id;
    }
    registerChangeInBookingStatuses() {
        this.eventSubscriber = this.eventManager.subscribe('bookingStatusListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
