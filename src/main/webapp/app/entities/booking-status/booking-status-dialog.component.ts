import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BookingStatus } from './booking-status.model';
import { BookingStatusPopupService } from './booking-status-popup.service';
import { BookingStatusService } from './booking-status.service';

@Component({
    selector: 'jhi-booking-status-dialog',
    templateUrl: './booking-status-dialog.component.html'
})
export class BookingStatusDialogComponent implements OnInit {

    bookingStatus: BookingStatus;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private bookingStatusService: BookingStatusService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bookingStatus.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bookingStatusService.update(this.bookingStatus));
        } else {
            this.subscribeToSaveResponse(
                this.bookingStatusService.create(this.bookingStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BookingStatus>>) {
        result.subscribe((res: HttpResponse<BookingStatus>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BookingStatus) {
        this.eventManager.broadcast({ name: 'bookingStatusListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-booking-status-popup',
    template: ''
})
export class BookingStatusPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bookingStatusPopupService: BookingStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bookingStatusPopupService
                    .open(BookingStatusDialogComponent as Component, params['id']);
            } else {
                this.bookingStatusPopupService
                    .open(BookingStatusDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
