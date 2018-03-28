import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CourtType } from './court-type.model';
import { CourtTypePopupService } from './court-type-popup.service';
import { CourtTypeService } from './court-type.service';

@Component({
    selector: 'jhi-court-type-dialog',
    templateUrl: './court-type-dialog.component.html'
})
export class CourtTypeDialogComponent implements OnInit {

    courtType: CourtType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private courtTypeService: CourtTypeService,
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
        if (this.courtType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courtTypeService.update(this.courtType));
        } else {
            this.subscribeToSaveResponse(
                this.courtTypeService.create(this.courtType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CourtType>>) {
        result.subscribe((res: HttpResponse<CourtType>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CourtType) {
        this.eventManager.broadcast({ name: 'courtTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-court-type-popup',
    template: ''
})
export class CourtTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtTypePopupService: CourtTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courtTypePopupService
                    .open(CourtTypeDialogComponent as Component, params['id']);
            } else {
                this.courtTypePopupService
                    .open(CourtTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
