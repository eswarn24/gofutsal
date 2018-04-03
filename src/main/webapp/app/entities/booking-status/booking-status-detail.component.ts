import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { BookingStatus } from './booking-status.model';
import { BookingStatusService } from './booking-status.service';

@Component({
    selector: 'jhi-booking-status-detail',
    templateUrl: './booking-status-detail.component.html'
})
export class BookingStatusDetailComponent implements OnInit, OnDestroy {

    bookingStatus: BookingStatus;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private bookingStatusService: BookingStatusService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBookingStatuses();
    }

    load(id) {
        this.bookingStatusService.find(id)
            .subscribe((bookingStatusResponse: HttpResponse<BookingStatus>) => {
                this.bookingStatus = bookingStatusResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBookingStatuses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bookingStatusListModification',
            (response) => this.load(this.bookingStatus.id)
        );
    }
}
