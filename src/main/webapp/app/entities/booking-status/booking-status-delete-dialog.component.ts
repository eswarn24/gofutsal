import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BookingStatus } from './booking-status.model';
import { BookingStatusPopupService } from './booking-status-popup.service';
import { BookingStatusService } from './booking-status.service';

@Component({
    selector: 'jhi-booking-status-delete-dialog',
    templateUrl: './booking-status-delete-dialog.component.html'
})
export class BookingStatusDeleteDialogComponent {

    bookingStatus: BookingStatus;

    constructor(
        private bookingStatusService: BookingStatusService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bookingStatusService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bookingStatusListModification',
                content: 'Deleted an bookingStatus'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-booking-status-delete-popup',
    template: ''
})
export class BookingStatusDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bookingStatusPopupService: BookingStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bookingStatusPopupService
                .open(BookingStatusDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
