import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CourtLocation } from './court-location.model';
import { CourtLocationPopupService } from './court-location-popup.service';
import { CourtLocationService } from './court-location.service';

@Component({
    selector: 'jhi-court-location-dialog',
    templateUrl: './court-location-dialog.component.html'
})
export class CourtLocationDialogComponent implements OnInit {

    courtLocation: CourtLocation;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private courtLocationService: CourtLocationService,
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
        if (this.courtLocation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courtLocationService.update(this.courtLocation));
        } else {
            this.subscribeToSaveResponse(
                this.courtLocationService.create(this.courtLocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CourtLocation>>) {
        result.subscribe((res: HttpResponse<CourtLocation>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CourtLocation) {
        this.eventManager.broadcast({ name: 'courtLocationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-court-location-popup',
    template: ''
})
export class CourtLocationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtLocationPopupService: CourtLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courtLocationPopupService
                    .open(CourtLocationDialogComponent as Component, params['id']);
            } else {
                this.courtLocationPopupService
                    .open(CourtLocationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
