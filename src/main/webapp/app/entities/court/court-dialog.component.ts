import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Court } from './court.model';
import { CourtPopupService } from './court-popup.service';
import { CourtService } from './court.service';
import { CourtType, CourtTypeService } from '../court-type';
import { User, UserService } from '../../shared';
import { CourtLocation, CourtLocationService } from '../court-location';

@Component({
    selector: 'jhi-court-dialog',
    templateUrl: './court-dialog.component.html'
})
export class CourtDialogComponent implements OnInit {

    court: Court;
    isSaving: boolean;

    courttypes: CourtType[];

    users: User[];

    courtlocations: CourtLocation[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private courtService: CourtService,
        private courtTypeService: CourtTypeService,
        private userService: UserService,
        private courtLocationService: CourtLocationService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.courtTypeService
            .query({filter: 'court-is-null'})
            .subscribe((res: HttpResponse<CourtType[]>) => {
                if (!this.court.courtType || !this.court.courtType.id) {
                    this.courttypes = res.body;
                } else {
                    this.courtTypeService
                        .find(this.court.courtType.id)
                        .subscribe((subRes: HttpResponse<CourtType>) => {
                            this.courttypes = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.courtLocationService.query()
            .subscribe((res: HttpResponse<CourtLocation[]>) => { this.courtlocations = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.court, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.court.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courtService.update(this.court));
        } else {
            this.subscribeToSaveResponse(
                this.courtService.create(this.court));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Court>>) {
        result.subscribe((res: HttpResponse<Court>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Court) {
        this.eventManager.broadcast({ name: 'courtListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCourtTypeById(index: number, item: CourtType) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackCourtLocationById(index: number, item: CourtLocation) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-court-popup',
    template: ''
})
export class CourtPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courtPopupService: CourtPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courtPopupService
                    .open(CourtDialogComponent as Component, params['id']);
            } else {
                this.courtPopupService
                    .open(CourtDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
