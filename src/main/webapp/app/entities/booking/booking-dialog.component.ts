import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Booking } from './booking.model';
import { BookingPopupService } from './booking-popup.service';
import { BookingService } from './booking.service';
import { Court, CourtService } from '../court';

import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'jhi-booking-dialog',
    templateUrl: './booking-dialog.component.html'
})
export class BookingDialogComponent implements OnInit {

    booking: Booking;
    isSaving: boolean;
    court: Court;/*
    courts: Court[];*/
    dateDp: any;

    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private bookingService: BookingService,
        private courtService: CourtService,
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourts();
    }

    load(id) {
        this.courtService.find(id)
            .subscribe((courtResponse: HttpResponse<Court>) => {
                this.court = courtResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courtListModification',
            (response) => this.load(this.court.id)
        );
    }

   /* ngOnInit() {
        this.isSaving = false;
        this.courtService
            .query({filter: 'booking-is-null'})
            .subscribe((res: HttpResponse<Court[]>) => {
                if (!this.booking.court || !this.booking.court.id) {
                    this.courts = res.body;
                } else {
                    this.courtService
                        .find(this.booking.court.id)
                        .subscribe((subRes: HttpResponse<Court>) => {
                            this.courts = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }*/

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

    trackCourtById(index: number, item: Court) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-booking-popup',
    template: ''
})
export class BookingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bookingPopupService: BookingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bookingPopupService
                    .open(BookingDialogComponent as Component, params['id']);
            } else {
                this.bookingPopupService
                    .open(BookingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
