import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Booking } from './booking-history.model';
import { BookingHistoryPopupService } from './booking-history-popup.service';
import { BookingHistoryService } from './booking-history.service';
/*import { Court, CourtService } from '../court';*/

@Component({
    selector: 'jhi-booking-dialog',
    templateUrl: './booking-history-dialog.component.html'
})
export class BookingHistoryDialogComponent implements OnInit {

    booking: Booking;
    isSaving: boolean;

    bookings: Booking[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private bookingService: BookingHistoryService,
/*        private courtService: CourtService,*/
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.bookingService
            .query({filter: 'booking-is-null'})
            .subscribe((res: HttpResponse<Booking[]>) => {
                if (!this.booking.court || !this.booking.court.id) {
                    this.bookings = res.body;
                } else {
                    this.bookingService
                        .find(this.booking.court.id)
                        .subscribe((subRes: HttpResponse<Booking>) => {
                            this.bookings = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.booking.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bookingService.update(this.booking));
        } else {
            this.subscribeToSaveResponse(
                this.bookingService.create(this.booking));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Booking>>) {
        result.subscribe((res: HttpResponse<Booking>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Booking) {
        this.eventManager.broadcast({ name: 'bookingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCourtById(index: number, item: Booking) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-booking-popup',
    template: ''
})
export class BookingHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bookingPopupService: BookingHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bookingPopupService
                    .open(BookingHistoryDialogComponent as Component, params['id']);
            } else {
                this.bookingPopupService
                    .open(BookingHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
